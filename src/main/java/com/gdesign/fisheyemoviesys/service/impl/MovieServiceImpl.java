package com.gdesign.fisheyemoviesys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.MovieDO;
import com.gdesign.fisheyemoviesys.entity.MovieTypeDO;
import com.gdesign.fisheyemoviesys.entity.TypeDO;
import com.gdesign.fisheyemoviesys.entity.dto.MovieDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.TypeDTO;
import com.gdesign.fisheyemoviesys.entity.enums.CodeEnum;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.entity.param.MovieQuery;
import com.gdesign.fisheyemoviesys.mapper.MovieMapper;
import com.gdesign.fisheyemoviesys.service.MovieService;
import com.gdesign.fisheyemoviesys.service.MovieTypeService;
import com.gdesign.fisheyemoviesys.service.TypeService;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ycy
 */
@Service
@Slf4j
public class MovieServiceImpl extends ServiceImpl<MovieMapper, MovieDO> implements MovieService {
    @Resource
    private TypeService typeService;

    @Resource
    private MovieTypeService movieTypeService;

    /**
     * 分页条件查询
     *
     * @param query 查询条件
     * @return 电影集
     */
    @Override
    public ResponseMessageDTO<PageResultDTO<MovieDTO>> pageQueryMovieByCondition(MovieQuery query) {
        PageResultDTO<MovieDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<MovieDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<MovieDO> queryWrapper = new LambdaQueryWrapper<>();
            //查询条件为空值或nll时，查询全部未被删除的电影记录
            queryWrapper.eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .like(StringUtils.isNotBlank(query.getName()), MovieDO::getName, query.getName())
                    .like(StringUtils.isNotBlank(query.getDirector()), MovieDO::getDirector, query.getDirector())
                    .like(StrUtil.isNotBlank(query.getStarring()), MovieDO::getStarring, query.getStarring())
                    .eq(query.getArea() != null, MovieDO::getArea, query.getArea())
                    .orderByDesc(MovieDO::getYear);
            //获取type_id 在 query.getType()(List类型)中的 movie_id
            List<Long> movieId = this.getMovieByTypeId(query.getType()).getResult();
            //如果没有该类型的电影，则直接返回，否则再对queryWrapper进行拼接
            if (CollectionUtils.isEmpty(movieId)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<MovieDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            } else {
                queryWrapper.in(MovieDO::getId, movieId);
            }
            IPage<MovieDO> movieDOPage = this.page(page, queryWrapper);
            List<MovieDO> movieDORecords = movieDOPage.getRecords();
            if (CollectionUtils.isEmpty(movieDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<MovieDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            List<MovieDTO> movieDTOList = ConversionUtils.transformList(movieDORecords, MovieDTO.class);
            //设置返回的电影类型
            movieDTOList.forEach(
                    item -> item.setType(typeService.getTypeById(item.getId()).getResult().stream().map(TypeDTO::getName).collect(Collectors.toList()))
            );
            pageResultDTO.setRows(movieDTOList);
            pageResultDTO.setTotal(movieDOPage.getTotal());
            pageResultDTO.setPageSize(movieDOPage.getSize());
            pageResultDTO.setPage(movieDOPage.getCurrent());
            pageResultDTO.setTotalPage(movieDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<MovieDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("电影分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<List<Long>> getMovieByTypeId(List<String> typeList) {
        //如果typeList为空,则直接查询出所有未被删除的电影id
        if (CollectionUtils.isEmpty(typeList)) {
            LambdaQueryWrapper<MovieDO> wrapper = new LambdaQueryWrapper<MovieDO>()
                    .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
            List<Long> movieId = this.list(wrapper).stream().map(MovieDO::getId).collect(Collectors.toList());
            return ResponseMessageDTO.success(movieId);
        } else {
            //把类型名转换为类型id =>在type中查询出id集合
            LambdaQueryWrapper<TypeDO> typeQueryWrapper = new LambdaQueryWrapper<TypeDO>()
                    .in(TypeDO::getName, typeList);
            List<Long> typeLongIds = typeService.list(typeQueryWrapper).stream().map(TypeDO::getId).collect(Collectors.toList());
            //在movie_type 表中根据 类型id 查询出 电影id

            /**
             * 错误的数据库设计，将类型id设计为Long，没有考虑修改时减少类型等的情况
             */
            //inSql需要的参数为(1,2,3) 这种形式，而List<Long>为[1,2,3]这种类型，所以需要进行转换
            //将List<Long>转化为一串字符串,()外扩号直接写在sql语句中
//            String typeIds=typeLongIds.stream().map(Objects::toString).collect(Collectors.joining(","));
//            log.info("typeId:"+typeIds);
//            LambdaQueryWrapper<MovieDO> movieQueryWrapper=new LambdaQueryWrapper<MovieDO>()
//                    .eq(MovieDO::getDeleted,DeleteEnum.NO_DELETE.getCode())
//                    .inSql(MovieDO::getId,"SELECT distinct(movie_id) FROM movie_type WHERE type_id IN ("+typeIds +")");
//            List<Long> movieIds=this.list(movieQueryWrapper).stream().map(MovieDO::getId).collect(Collectors.toList());
//            return ResponseMessageDTO.success(movieIds);

            // regexp的 | 可以搜索多个字符串之一，相当于 or
            String typeIds = typeLongIds.stream().map(Objects::toString).collect(Collectors.joining("|"));
            log.info("typeId:" + typeIds);
            LambdaQueryWrapper<MovieDO> movieQueryWrapper = new LambdaQueryWrapper<MovieDO>()
                    .inSql(MovieDO::getId, "SELECT movie_id FROM movie_type WHERE type_id REGEXP ('" + typeIds + "')");
            List<Long> movieIds = this.list(movieQueryWrapper).stream().map(MovieDO::getId).collect(Collectors.toList());
            return ResponseMessageDTO.success(movieIds);
        }
    }

    @Override
    public ResponseMessageDTO<String> deleteMovie(Long[] ids) {
        //删除电影的同时删除 movie_type表
        List<Long> newIds = Arrays.asList(ids);
        LambdaUpdateWrapper<MovieDO> movieUpdateWrapper = new LambdaUpdateWrapper<MovieDO>()
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(MovieDO::getId, newIds)
                .set(MovieDO::getDeleted, DeleteEnum.DELETE.getCode());
        //保证了有修改时间
        MovieDO movieDO = new MovieDO();
        LambdaUpdateWrapper<MovieTypeDO> movieTypeUpdateWrapper = new LambdaUpdateWrapper<MovieTypeDO>()
                .eq(MovieTypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(MovieTypeDO::getMovieId, newIds)
                .set(MovieTypeDO::getDeleted, DeleteEnum.DELETE.getCode());
        if (this.update(movieDO, movieUpdateWrapper) && movieTypeService.update(movieTypeUpdateWrapper)) {
            return ResponseMessageDTO.success("删除成功");
        }
        return ResponseMessageDTO.success("删除失败");
    }

    @Override
    public ResponseMessageDTO<String> updateMovie(MovieDTO movieDTO) {
        LambdaUpdateWrapper<MovieDO> movieUpdateWrapper = new LambdaUpdateWrapper<MovieDO>()
                .eq(MovieDO::getId, movieDTO.getId())
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .set(MovieDO::getDirector, movieDTO.getDirector())
                .set(MovieDO::getName, movieDTO.getName())
                .set(MovieDO::getStarring, movieDTO.getStarring())
                .set(MovieDO::getSynopsis, movieDTO.getSynopsis())
                .set(MovieDO::getYear, movieDTO.getYear())
                .set(MovieDO::getDuration, movieDTO.getDuration())
                .set(MovieDO::getArea, movieDTO.getArea());
        MovieDO movieDO = new MovieDO();
        //修改type
        /**
         //根据传入的类型名获取传入的类型id 即 List<String> =>String
         String typeName = movieDTO.getType().stream().map(Objects::toString).collect(Collectors.joining("|"));
         LambdaQueryWrapper<TypeDO> typeQueryWrapper = new LambdaQueryWrapper<TypeDO>()
         .inSql(TypeDO::getId, "SELECT id FROM type WHERE name REGEXP ('" + typeName + "')");
         //List<Long> =>String
         List<Long> typeLongIds=typeService.list(typeQueryWrapper).stream().map(TypeDO::getId).collect(Collectors.toList());
         String type = typeLongIds.stream().map(Objects::toString).collect(Collectors.joining(","));
         */
        String type = getTypeStringByTypeList(movieDTO.getType());
        log.info("type:" + type);
        //根据movie_id 修改movie_type表的type_id
        LambdaUpdateWrapper<MovieTypeDO> movieTypeUpdateWrapper = new LambdaUpdateWrapper<MovieTypeDO>()
                .set(MovieTypeDO::getTypeId, type)
                .eq(MovieTypeDO::getMovieId, movieDTO.getId());
        if (this.update(movieDO, movieUpdateWrapper) && movieTypeService.update(movieTypeUpdateWrapper)) {
            return ResponseMessageDTO.success("修改成功");
        }
        return ResponseMessageDTO.success("修改异常");
    }

    @Override
    public ResponseMessageDTO<String> addMovie(MovieDTO movieDTO) {
        MovieDO movieDO = new MovieDO();
        BeanUtils.copyProperties(movieDTO, movieDO);
        //新增movie_type
        /**
         //根据类型名获取类型id
         String typeName = movieDTO.getType().stream().map(Objects::toString).collect(Collectors.joining("|"));
         LambdaQueryWrapper<TypeDO> typeQueryWrapper = new LambdaQueryWrapper<TypeDO>()
         .inSql(TypeDO::getId, "SELECT id FROM type WHERE name REGEXP ('" + typeName + "')");
         //List<Long>  =>String
         List<Long> typeLongIds=typeService.list(typeQueryWrapper).stream().map(TypeDO::getId).collect(Collectors.toList());
         String type = typeLongIds.stream().map(Objects::toString).collect(Collectors.joining(","));
         */
        String type = getTypeStringByTypeList(movieDTO.getType());
        if (this.save(movieDO)) {
            //获取新增的电影id
            log.info("新增电影的id为" + movieDO.getId());
            MovieTypeDO movieTypeDO = new MovieTypeDO();
            movieTypeDO.setMovieId(movieDO.getId());
            movieTypeDO.setTypeId(type);
            if (movieTypeService.save(movieTypeDO)) {
                return ResponseMessageDTO.success("新增成功");
            }
            return ResponseMessageDTO.success("新增失败");
        }
        return ResponseMessageDTO.success("新增失败");
    }

    private String getTypeStringByTypeList(List<String> typeList) {
        //根据传入的类型名获取传入的类型id 即 List<String> =>String
        String typeName = typeList.stream().map(Objects::toString).collect(Collectors.joining("|"));
        LambdaQueryWrapper<TypeDO> typeQueryWrapper = new LambdaQueryWrapper<TypeDO>()
                .inSql(TypeDO::getId, "SELECT id FROM type WHERE name REGEXP ('" + typeName + "')");
        //List<Long>  =>String
        List<Long> typeLongIds = typeService.list(typeQueryWrapper).stream().map(TypeDO::getId).collect(Collectors.toList());
        String type = typeLongIds.stream().map(Objects::toString).collect(Collectors.joining(","));
        return type;
    }
}
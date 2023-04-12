package com.gdesign.fisheyemoviesys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.*;
import com.gdesign.fisheyemoviesys.entity.dto.*;
import com.gdesign.fisheyemoviesys.entity.enums.*;
import com.gdesign.fisheyemoviesys.entity.param.MovieQuery;
import com.gdesign.fisheyemoviesys.entity.param.SpecialMovieQuery;
import com.gdesign.fisheyemoviesys.entity.param.UserCollectQuery;
import com.gdesign.fisheyemoviesys.mapper.MovieMapper;
import com.gdesign.fisheyemoviesys.service.*;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import com.gdesign.fisheyemoviesys.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private UserCollectService userCollectService;

    @Resource
    private UserService userService;

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
                    .like(StringUtils.isNotBlank(query.getStarring()), MovieDO::getStarring, query.getStarring())
                    .eq(query.getArea() != null, MovieDO::getArea, query.getArea())
                    .orderByDesc(MovieDO::getYear);
            //获取type_id 在 query.getType()(List类型)中的 movie_id
            List<Long> movieId = this.getMoviesByTypeId(query.getType()).getResult();
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
    public ResponseMessageDTO<List<Long>> getMoviesByTypeId(List<String> typeList) {
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
        log.info(movieDTO.getImg());
        LambdaUpdateWrapper<MovieDO> movieUpdateWrapper = new LambdaUpdateWrapper<MovieDO>()
                .eq(MovieDO::getId, movieDTO.getId())
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .set(MovieDO::getDirector, movieDTO.getDirector())
                .set(MovieDO::getName, movieDTO.getName())
                .set(MovieDO::getStarring, movieDTO.getStarring())
                .set(MovieDO::getSynopsis, movieDTO.getSynopsis())
                .set(MovieDO::getYear, movieDTO.getYear())
                .set(MovieDO::getDuration, movieDTO.getDuration())
                //海报的url不包括Constants.POSTER_PATH时，更新海报url，否则不更新
                .set(movieDTO.getImg().contains(Constants.POSTER_PATH)==false,MovieDO::getImg, Constants.POSTER_PATH + movieDTO.getImg())
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
        movieDO.setImg(Constants.POSTER_PATH+movieDTO.getImg());
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

    @Override
    public ResponseMessageDTO<List<Long>> getMovieIdByMovieName(String movieName) {
        LambdaQueryWrapper<MovieDO> wrapper = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .like(StringUtils.isNotBlank(movieName), MovieDO::getName, movieName);
        List<Long> idList = this.list(wrapper).stream().map(MovieDO::getId).collect(Collectors.toList());
        return ResponseMessageDTO.success(idList);
    }

    @Override
    public ResponseMessageDTO<List<MovieDTO>> getHighScoreMovies() {
        LambdaQueryWrapper<MovieDO> queryWrapper = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .orderByDesc(MovieDO::getScore)
                .last("limit 10");
        List<MovieDTO> movieDTOList = ConversionUtils.transformList(this.list(queryWrapper), MovieDTO.class);
        return ResponseMessageDTO.success(movieDTOList);
    }

    @Override
    public ResponseMessageDTO<MovieDTO> getMovieById(Long Id) {
        LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getId, Id)
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        MovieDO movieDO = this.getOne(movieDOLambdaQueryWrapper);
        MovieDTO movieDTO = new MovieDTO();
        BeanUtils.copyProperties(movieDO, movieDTO);
        //设置电影类型、当前用户是否收藏
        movieDTO.setType(typeService.getTypeById(movieDTO.getId()).getResult().stream().map(TypeDTO::getName).collect(Collectors.toList()));
        //获得当前登录用户的用户名根据用户名获取用户id
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username);
        UserDO userDO = userService.getOne(userQueryWrapper);
        movieDTO.setFlagCollect(userCollectService.getCollectByUserId(userDO.getId(), movieDTO.getId(), CollectKindEnum.MOVIE.getCode()));
        return ResponseMessageDTO.success(movieDTO);
    }

    @Override
    public ResponseMessageDTO<Boolean> updateMovieScore(MovieDTO movieDTO) {
        LambdaQueryWrapper<MovieDO> queryWrapper = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getId, movieDTO.getId())
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        MovieDO movieDO = this.getOne(queryWrapper);
        Double newScore = (movieDTO.getScore() + movieDO.getScore()) / 2;
        LambdaUpdateWrapper<MovieDO> updateWrapper = new LambdaUpdateWrapper<MovieDO>()
                .eq(MovieDO::getId, movieDTO.getId())
                .set(MovieDO::getScore, newScore);

        if (this.update(movieDO, updateWrapper)) {
            return ResponseMessageDTO.success(true);
        }
        return ResponseMessageDTO.success(false);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<MovieDTO>> getCollectMovies(UserCollectQuery query) {
        PageResultDTO<MovieDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<MovieDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<MovieDO> queryWrapper = new LambdaQueryWrapper<>();

            //根据用户id和kind查询收藏字符串
            LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                    .eq(UserCollectDO::getUserId, query.getUserId())
                    .eq(UserCollectDO::getKind, query.getKind());
            UserCollectDO userCollectDO=userCollectService.getOne(userCollectDOLambdaQueryWrapper);
            if(Objects.isNull(userCollectDO)){
                return ResponseMessageDTO.success(PageResultDTO
                        .<MovieDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            String collectIds = userCollectDO.getCollectIds();
            //将sting类型的字符串转化为List<Long>类型即3,6,8=》[3,6,8]
            List<Long> collectId = Arrays.asList(collectIds.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            //查询全部未被删除的评论记录
            queryWrapper.eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .in(MovieDO::getId, collectId)
                    .orderByDesc(MovieDO::getCreateTime);
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
            pageResultDTO.setRows(movieDTOList);
            pageResultDTO.setTotal(movieDOPage.getTotal());
            pageResultDTO.setPageSize(movieDOPage.getSize());
            pageResultDTO.setPage(movieDOPage.getCurrent());
            pageResultDTO.setTotalPage(movieDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<MovieDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<List<MovieDTO>> getMoviesByName(String movieName) {
        LambdaQueryWrapper<MovieDO> queryWrapper = new LambdaQueryWrapper<MovieDO>()
                .like(MovieDO::getName, movieName)
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        List<MovieDTO> movieDTOList = ConversionUtils.transformList(this.list(queryWrapper), MovieDTO.class);
        return ResponseMessageDTO.success(movieDTOList);
    }

    @Override
    public ResponseMessageDTO<List<String>> getAllMovieYear() {
        LambdaQueryWrapper<MovieDO> queryWrapper = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .orderByDesc(MovieDO::getYear);
        List<String> yearsList = this.list(queryWrapper).stream().map(MovieDO::getYear).distinct().collect(Collectors.toList());
        //使用distinct去掉重复的年份
        return ResponseMessageDTO.success(yearsList);
    }

    @Override
    public ResponseMessageDTO<List<String>> getAllMovieArea() {
        List<String> areaList = new ArrayList<>();
        for (AreaEnum item : AreaEnum.values()) {
            areaList.add(item.getArea());
        }
        return ResponseMessageDTO.success(areaList);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<MovieDTO>> getAllMovieByTypeAreaYear(SpecialMovieQuery query) {
        PageResultDTO<MovieDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<MovieDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            //处理前端传过来的字符串类型、地区
            Integer area = -1;
            for (AreaEnum item : AreaEnum.values()) {
                if (item.getArea().equals(query.getArea())) {
                    area = item.getCode();
                }
            }
            String type = query.getType();
            LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //type为全部，查询全部未被删除的电影记录，可以省略不写if条件
            movieDOLambdaQueryWrapper.eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(Constants.MOVIE_ALL.equals(query.getYear()) == false, MovieDO::getYear, query.getYear())
                    .eq(area != -1, MovieDO::getArea, area)
                    .orderByDesc(MovieDO::getScore);
            //type不为全部
            if(type.equals(Constants.MOVIE_ALL)==false){//根据type的name字段查询出type_id
                LambdaQueryWrapper<TypeDO> typeDOLambdaQueryWrapper = new LambdaQueryWrapper<TypeDO>()
                        .eq(TypeDO::getName, type);
                String typeId = typeService.getOne(typeDOLambdaQueryWrapper).getId().toString();
                //从movie_type表中找出type_id包含typeId的movieId
                LambdaQueryWrapper<MovieDO> movieQueryWrapper = new LambdaQueryWrapper<MovieDO>()
                        .inSql(MovieDO::getId, "SELECT movie_id FROM movie_type WHERE type_id REGEXP ('" + typeId + "')");
                List<Long> movieIds = this.list(movieQueryWrapper).stream().map(MovieDO::getId).collect(Collectors.toList());
                movieDOLambdaQueryWrapper.in(MovieDO::getId, movieIds);
            }
            IPage<MovieDO> movieDOPage = this.page(page, movieDOLambdaQueryWrapper);
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

//    @Override
//    public ResponseMessageDTO<List<MovieDTO>> getUserLikeMovies(Long userId) {
//        //根据user_id获取user_label
//        LambdaQueryWrapper<UserLabelDO> userLabelDOLambdaQueryWrapper=new LambdaQueryWrapper<UserLabelDO>()
//                .eq(UserLabelDO::getUserId,userId)
//                .eq(UserLabelDO::getStatus, UserLabelEnum.AFTER.getCode());
//        UserLabelDO userLabelDO=userLabelService.getOne(userLabelDOLambdaQueryWrapper);
//        String
//        //获取所有未被删除的电影
//        LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper=new LambdaQueryWrapper<MovieDO>()
//                .eq(MovieDO::getDeleted,DeleteEnum.NO_DELETE.getCode());
//        List<MovieDO> movieDOList=this.list(movieDOLambdaQueryWrapper);
//        //获取每个电影在user_label中出现的次数，采用hashMap存储,key为movie_id,value为所有的label出现次数
//        HashMap<String,Integer> hashMap=new HashMap<>();
//        movieDOList.forEach(
//                item->{
//                    //年份
//                    //将电影的year转化为label_id
//                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper=new LambdaQueryWrapper<LabelDO>()
//
//                }
//
//        );
//    }

    @Override
    public ResponseMessageDTO<String> getPosterUrl(MultipartFile file){
        log.info("是否为空:"+file.isEmpty());
        if (!file.isEmpty()) {
            String uploadImg = UploadUtil.uploadPoster(file);
            if (StrUtil.isEmpty(uploadImg)) {
                return ResponseMessageDTO.success("海报上传失败！");
            }
            return ResponseMessageDTO.success(uploadImg);
        }
        return ResponseMessageDTO.success("海报上传失败！");
    }

}
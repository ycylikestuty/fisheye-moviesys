package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.MovieTypeDO;
import com.gdesign.fisheyemoviesys.entity.TypeDO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.TypeDTO;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.mapper.TypeMapper;
import com.gdesign.fisheyemoviesys.service.MovieTypeService;
import com.gdesign.fisheyemoviesys.service.TypeService;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ycy
 */
@Service
@Slf4j
public class TypeServiceImpl extends ServiceImpl<TypeMapper, TypeDO> implements TypeService {
    @Resource
    private MovieTypeService movieTypeService;

    @Override
    public ResponseMessageDTO<List<TypeDTO>> getTypeById(Long movieId) {
        //在movie_type表中根据movie_id获取type_id字符串
        LambdaQueryWrapper<MovieTypeDO> movieTypeQueryWrapper = new LambdaQueryWrapper<MovieTypeDO>()
                .eq(MovieTypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .eq(MovieTypeDO::getMovieId, movieId);
        MovieTypeDO movieTypeDO = movieTypeService.getOne(movieTypeQueryWrapper);
        //1,3=>[1,3]=>["战争","青春"]
        //将type_id字符串转化为List 如1,3=>[1,3]
        List<Long> typeId = Arrays.asList(movieTypeDO.getTypeId().split(",")).stream().map(
                item -> Long.parseLong(item.trim())).collect(Collectors.toList());
        typeId.forEach(
                item -> log.info("item:" + item)
        );
        LambdaQueryWrapper<TypeDO> typeQueryWrapper = new LambdaQueryWrapper<TypeDO>()
                .in(TypeDO::getId, typeId);
        /**
         * 获取type的name字段集合
         * List<String> type=this.list(queryWrapper).stream().map(TypeDO::getName).collect(Collectors.toList());
         */
        List<TypeDO> typeDOList = this.list(typeQueryWrapper);
        /**
         * 获取type的name字段集合 并 将其组合为带、的一条字符串
         * String type=this.list(queryWrapper).stream().map(TypeDO::getName).collect(Collectors.joining("、"));
         */

        return ResponseMessageDTO.success(ConversionUtils.transformList(typeDOList, TypeDTO.class));
    }

    @Override
    public ResponseMessageDTO<List<TypeDTO>> getAllType() {
        LambdaQueryWrapper<TypeDO> queryWrapper = new LambdaQueryWrapper<TypeDO>()
                .eq(TypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        List<TypeDO> typeDOList = this.list(queryWrapper);
        List<TypeDTO> typeDTOList = ConversionUtils.transformList(typeDOList, TypeDTO.class);
        return ResponseMessageDTO.success(typeDTOList);
    }

}

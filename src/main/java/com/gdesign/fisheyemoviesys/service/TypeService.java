package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.TypeDO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.TypeDTO;

import java.util.List;

/**
 * @author ycy
 */
public interface TypeService extends IService<TypeDO> {
    /**
     * 根据电影id返回类型列表
     *
     * @param movieId 电影id
     * @return 类型字符串
     */
    ResponseMessageDTO<List<TypeDTO>> getTypeById(Long movieId);

    /**
     * 获取全部类型
     *
     * @return 类型列表
     */
    ResponseMessageDTO<List<TypeDTO>> getAllType();

}

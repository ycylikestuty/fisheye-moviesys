package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.LabelDO;
import com.gdesign.fisheyemoviesys.entity.dto.*;
import com.gdesign.fisheyemoviesys.entity.param.LabelQuery;

import java.util.List;

/**
 * @author ycy
 */
public interface LabelService extends IService<LabelDO> {
    /**
     * 分页条件查询标签
     *
     * @param query 查询条件
     * @return 标签集合
     */
    ResponseMessageDTO<PageResultDTO<LabelDTO>> pageQueryLabelByCondition(LabelQuery query);

    /**
     * 逻辑删除标签
     *
     * @param ids 标签id
     * @return删除成功与否
     */
    ResponseMessageDTO<String> deleteLabel(Long[] ids);

    /**
     * 统计标签的分布
     *
     * @param labelDTO 标签
     * @return
     */
    ResponseMessageDTO<List<LabelDTO>> countLabelByCondition(LabelDTO labelDTO);

    /**
     * 根据用户信息获取用户注册时的所有标签列表
     * @param userLabelDTO
     * @return
     */
    ResponseMessageDTO<List<LabelDTO>> getOldUserLabelList(UserLabelDTO userLabelDTO);

    /**
     * 根据用户信息获取用户的当前所有标签列表
     * @param userLabelDTO
     * @return
     */
    ResponseMessageDTO<List<LabelDTO>> getNewUserLabelList(UserLabelDTO userLabelDTO);

    /**
     * 注册用户
     * @param userDTO 用户信息
     * @return 注册成功与否
     */
    Result registerUser(UserDTO userDTO);

    /**
     * 根据用户id获取推荐用户的电影
     * @param userId 用户id
     * @return 推荐电影集合
     */
    ResponseMessageDTO<List<MovieDTO>> getUserLikeMovies(Long userId);
}

package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.CommentDO;
import com.gdesign.fisheyemoviesys.entity.dto.CommentDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.*;

/**
 * @author ycy
 */
public interface CommentService extends IService<CommentDO> {
    /**
     * 分页条件查询评论
     *
     * @param query 查询条件
     * @return 分页结果
     */
    ResponseMessageDTO<PageResultDTO<CommentDTO>> pageQueryCommentByCondition(CommentQuery query);

    /**
     * 更新评论状态
     *
     * @param commentDTO 评论详情
     * @return 更新是否成功
     */
    ResponseMessageDTO<String> updateCommentStatus(CommentDTO commentDTO);

    /**
     * 逻辑删除影评
     *
     * @param ids 影评id
     * @return 删除是否成功
     */
    ResponseMessageDTO<String> deleteCommentsByIds(Long[] ids);

    /**
     * 查询该电影的唯一加精影评
     * @param movieId 电影id
     * @return 影评
     */
    ResponseMessageDTO<CommentDTO> getRefinementCommentsByMovieId(Long movieId);

    /**
     * 分页获取最热影评
     * @param query
     * @return
     */
    ResponseMessageDTO<PageResultDTO<CommentDTO>> getHotComments(SpecialCommentQuery query);

    /**
     * 分页获取最新影评
     * @param query
     * @return
     */
    ResponseMessageDTO<PageResultDTO<CommentDTO>> getLastComments(SpecialCommentQuery query);

    /**
     * 根据用户id查询该评论是否已经点赞
     * @param userId 用户id
     * @param commentId 评论id
     * @return 是否已经点赞
     */
    Boolean getStarByUserId(Long userId,Long commentId);

    /**
     * 更改点赞状态
     * @param userStarParam
     * @return
     */
    ResponseMessageDTO<Boolean> updateStar(UserStarParam userStarParam);

    /**
     * 更改收藏状态
     * @param userCollectParam
     * @return
     */
    ResponseMessageDTO<Boolean> updateCollect(UserCollectParam userCollectParam);

    /**
     * 新增评论
     * @param commentDTO 评论
     * @return 新增是否成功
     */
    ResponseMessageDTO<String> addComment(CommentDTO commentDTO);

    /**
     * 修改评论状态
     * @param commentId 评论id
     * @return 修改是否成功
     */
    ResponseMessageDTO<Boolean> updateCommentStatusById(Long commentId);

    /**
     * 分页获取最新的收藏影评
     * @param query
     * @return
     */
    ResponseMessageDTO<PageResultDTO<CommentDTO>> getLastCollectComments(UserCollectQuery query);

    /**
     * 分页获取最多的收藏影评
     * @param query
     * @return
     */
    ResponseMessageDTO<PageResultDTO<CommentDTO>> getHotCollectComments(UserCollectQuery query);

    /**
     * 根据用户id、评论id删除用户收藏
     * @param param 用户id、评论id、收藏种类
     * @return 收藏是否成功
     */
    ResponseMessageDTO<Boolean> deleteCollect(UserCollectParam param);

    /**
     * 分页获取用户影评
     * @param query 查询条件
     * @return
     */
    ResponseMessageDTO<PageResultDTO<CommentDTO>> getCommentsByUserId(UserCommentQuery query);

    /**
     * 更新评论详情
     *
     * @param commentDTO 评论详情
     * @return 更新是否成功
     */
    ResponseMessageDTO<String> updateCommentDetail(CommentDTO commentDTO);
}

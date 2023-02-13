package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.CommentDO;
import com.gdesign.fisheyemoviesys.entity.dto.CommentDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.CommentQuery;

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
     * 跟新评论状态
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
}

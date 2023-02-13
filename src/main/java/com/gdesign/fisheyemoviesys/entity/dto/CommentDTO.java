package com.gdesign.fisheyemoviesys.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -8692802468987549224L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 电影名
     */
    private String movieName;

    /**
     * 电影id
     */
    private Long movieId;

    /**
     * 点赞数
     */
    private Long star;

    /**
     * 收藏数
     */
    private Long collect;

    /**
     * 评论详情
     */
    private String detail;

    /**
     * 状态 0-正常 1-加精 2-举报
     */
    private Integer status;
}

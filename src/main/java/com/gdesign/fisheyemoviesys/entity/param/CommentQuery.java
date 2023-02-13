package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = 6345403097107755864L;

    /**
     * 用户账号，注意不是昵称
     */
    private String userName;

    /**
     * 电影名
     */
    private String movieName;

    /**
     *评论详情
     */
    private String detail;

    /**
     *状态 0-正常 1-加精 2-举报
     */
    private Integer status;
}

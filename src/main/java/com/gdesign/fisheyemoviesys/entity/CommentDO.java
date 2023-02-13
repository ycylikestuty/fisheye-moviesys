package com.gdesign.fisheyemoviesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comment")
public class CommentDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = -2185486164816793348L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 电影id
     */
    private Long movieId;

    /**
     * 点赞数
     * 本来字段名为like，但是like为mysql中的关键字，执行查询语句时会出错，所以改名为star
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

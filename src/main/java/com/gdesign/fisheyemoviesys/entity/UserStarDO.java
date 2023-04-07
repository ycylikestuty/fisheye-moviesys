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
@TableName("user_star")
public class UserStarDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = 8179192039206591406L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 点赞的评论字符串
     */
    private String commentIds;
}

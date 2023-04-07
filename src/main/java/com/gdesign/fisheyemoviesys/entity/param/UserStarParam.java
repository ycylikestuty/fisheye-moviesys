package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
public class UserStarParam implements Serializable {
    /**
     * 当前登录用户id
     */
    private Long userId;

    /**
     * 具体的评论id
     */
    private Long commentId;
}

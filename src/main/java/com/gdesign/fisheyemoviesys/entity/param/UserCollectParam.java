package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
public class UserCollectParam implements Serializable {
    private static final long serialVersionUID = 7233137257716525771L;
    /**
     * 当前登录用户id
     */
    private Long userId;

    /**
     * 收藏id
     */
    private Long collectId;

    /**
     * 收藏类型
     */
    private Integer kind;
}

package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends CommonPageQuery implements Serializable {

    private static final long serialVersionUID = -3317955399519030174L;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String username;

    /**
     * 状态
     * 0-正常
     * 1-禁言
     */
    private Integer status;
}

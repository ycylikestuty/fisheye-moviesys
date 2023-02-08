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
@TableName("user")
public class UserDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = 4332838871745969291L;

    /**
     * 等级
     */
    private Long grade;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     * 0-正常
     * 1-禁言
     */
    private Integer status;

    /**
     * 头像链接
     */
    private String img;
}

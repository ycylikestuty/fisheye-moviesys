package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum ErrorCodeEnum {
    /**
     * 查询异常
     */
    QUERY_ERROR_CODE(10001, "查询异常"),

    /**
     * 登录异常
     */
    LOGIN_ERROR_CODE(10002, "登录异常"),

    /**
     * 更新异常
     */
    UPDATE_ERROR_CODE(10003, "修改异常"),

    /**
     * 注册异常
     */
    REGISTER_ERROR_CODE(10004,"注册异常");


    private final Integer code;
    private final String msg;

    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

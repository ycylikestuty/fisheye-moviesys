package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum CodeEnum {
    SUCCESS(200, "成功"),
    AUTH_ERROR_CODE(403, "权限校验失败"),
    UN_LOGIN(201, "未登录"),
    VALIDATION_ERROR(202, "参数校验异常"),
    QUERY_ERROR(203, "查询异常"),
    PARSE_ERROR(204, "解析异常"),
    USER_NOT_EXIST_ERROR(205, "用户不存在异常"),
    UN_KNOW_ERROR(500, "系统未知异常");

    private final Integer code;
    private final String desc;

    CodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

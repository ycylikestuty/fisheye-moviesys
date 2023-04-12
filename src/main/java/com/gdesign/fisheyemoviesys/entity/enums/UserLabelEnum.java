package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum UserLabelEnum {
    BEFORE(0, "注册时"),
    AFTER(1, "注册后");

    private final Integer code;
    private final String msg;

    UserLabelEnum(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }
}

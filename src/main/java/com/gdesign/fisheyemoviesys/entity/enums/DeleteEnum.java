package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum DeleteEnum {
    NO_DELETE(0, "未删除"),
    DELETE(1, "删除");

    private final Integer code;
    private final String desc;

    DeleteEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

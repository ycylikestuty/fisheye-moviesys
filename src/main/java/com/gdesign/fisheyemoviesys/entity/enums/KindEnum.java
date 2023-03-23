package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum KindEnum {
    YEAR(1, "year"),
    AREA(2, "area"),
    GENRE(3, "genre");

    private final Integer code;
    private final String msg;

    KindEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum CollectKindEnum {
    MOVIE(1, "movie"),
    COMMENT(2, "comment");

    private final Integer code;
    private final String msg;

    CollectKindEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

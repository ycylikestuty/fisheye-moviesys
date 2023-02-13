package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */
@Getter
public enum CommentStatusEnum {
    NORMAL(0, "正常"),
    REFINEMENT(1, "加精"),
    REPORT(2, "举报");

    private final Integer code;
    private final String desc;

    CommentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

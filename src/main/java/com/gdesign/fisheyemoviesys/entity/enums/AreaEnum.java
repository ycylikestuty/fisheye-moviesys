package com.gdesign.fisheyemoviesys.entity.enums;


import lombok.Getter;

/**
 * @author ycy
 * 好像没什么用，先放着
 */
@Getter
public enum AreaEnum {
    CHINA_LAND(1, "内地"),
    CHINA_HKANDT(2, "港台"),
    EUROPE(3, "欧美"),
    KOREA(4, "韩国"),
    JAPAN(5, "日本"),
    OTHER(6, "其他");
    private final Integer code;
    private final String area;

    AreaEnum(Integer code, String area) {
        this.code = code;
        this.area = area;
    }
}

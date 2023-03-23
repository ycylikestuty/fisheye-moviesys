package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LabelQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = -7173088148323013162L;

    /**
     * 标签名
     */
    private String name;

    /**
     * 类型 0-年份 1-地区 2-风格
     */
    private Integer kind;
}

package com.gdesign.fisheyemoviesys.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO extends BaseInfoDTO implements Serializable {
    /**
     * 标签名
     */
    private String name;

    /**
     * 类型 1-年份 2-地区 3-风格
     */
    private Integer kind;

    /**
     * 标签占比
     */
    private Double rate;
}

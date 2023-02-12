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
public class TypeDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = 792962363768644035L;

    /**
     * 类型名
     */
    private String name;
}

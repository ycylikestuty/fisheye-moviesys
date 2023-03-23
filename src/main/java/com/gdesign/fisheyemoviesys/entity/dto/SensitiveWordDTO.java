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
public class SensitiveWordDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -3339935494993877309L;

    /**
     * 违禁词
     */
    private String word;
}

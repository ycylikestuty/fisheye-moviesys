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
public class UserLabelDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = 298877352924860673L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户标签状态
     * 0-注册时，1-注册后
     */
    private Integer status;

    /**
     * 年份标签字符串
     */
    private String year;

    /**
     * 地区标签字符串
     */
    private String area;

    /**
     * 风格标签字符串
     */
    private String genre;
}

package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialMovieQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = 7688748672627232269L;

    /**
     * 电影年份
     */
    private String year;

    /**
     *
     */
    private String type;

    /**
     * 电影地区
     */
    private String area;
}

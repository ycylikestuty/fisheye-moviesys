package com.gdesign.fisheyemoviesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("movie")
public class MovieDO extends BaseInfoDO implements Serializable {

    private static final long serialVersionUID = 6993928244241844765L;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 评分
     */
    private Double score;

    /**
     * 电影名
     */
    private String name;

    /**
     * 导演
     */
    private String director;

    /**
     * 主演
     */
    private String starring;

    /**
     * 上映年份
     */
    private String year;

    /**
     * 地区
     * 1-内地 2-港台 3-欧美 4-韩国 5-日本 6-其他
     */
    private Integer area;

    /**
     * 剧情简介
     */
    private String synopsis;

    /**
     * 海报链接
     */
    private String img;
}

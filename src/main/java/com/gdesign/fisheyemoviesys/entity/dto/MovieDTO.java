package com.gdesign.fisheyemoviesys.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -2062710861353489938L;

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

    /**
     * 类型集合
     * 为了方便前端显示类型（string），所以将其设置为String类型，同时前端向后端发送的类型集合也为String类型
     * 如果要将其改为Long类型 即 类型的主键id，需要在前端的 el-option 控件中的 :value属性 进行修改，将其改为Long类型
     */
    private List<String> type;

    /**
     * 当前登录用户是否收藏
     */
    private Boolean flagCollect;
}

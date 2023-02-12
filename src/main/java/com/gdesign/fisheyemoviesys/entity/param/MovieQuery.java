package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MovieQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = -7909796307023417575L;

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
     * 地区
     * 1-内地 2-港台 3-欧美 4-韩国 5-日本 6-其他
     */
    private Integer area;

    /**
     * 类型
     */
    private List<String> type;
}

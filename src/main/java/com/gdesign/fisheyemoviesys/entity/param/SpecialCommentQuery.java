package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialCommentQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = 6925012713145410130L;

    /**
     * 电影名
     */
    private Long movieId;
}

package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCommentQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = 5421541975480054098L;

    /**
     * 用户id
     */
    private Long userId;
}

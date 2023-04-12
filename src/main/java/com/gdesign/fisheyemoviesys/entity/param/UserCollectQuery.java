package com.gdesign.fisheyemoviesys.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCollectQuery extends CommonPageQuery implements Serializable {
    private static final long serialVersionUID = -5668870614098958365L;

    /**
     * 当前登录用户id
     */
    private Long userId;

    /**
     * 收藏类型
     */
    private Integer kind;
}

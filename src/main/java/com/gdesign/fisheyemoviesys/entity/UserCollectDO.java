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
@TableName("user_collect")
public class UserCollectDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = 1607949385568920937L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收藏种类
     */
    private Integer kind;

    /**
     * 收藏字符串
     */
    private String collectIds;
}

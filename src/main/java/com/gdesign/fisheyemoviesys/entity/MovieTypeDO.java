package com.gdesign.fisheyemoviesys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("movie_type")
public class MovieTypeDO implements Serializable {
    private static final long serialVersionUID = -7864433706344020219L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 电影id
     */
    private Long movieId;

    /**
     * 类型id
     */
    private String typeId;

    /**
     * 是否删除
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;
}

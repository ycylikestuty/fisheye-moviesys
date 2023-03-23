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
@TableName("label")
public class LabelDO extends BaseInfoDO implements Serializable {

    private static final long serialVersionUID = 7509137036601884633L;

    /**
     * 标签名
     */
    private String name;

    /**
     * 类型 1-年份 2-地区 3-风格
     */
    private Integer kind;
}

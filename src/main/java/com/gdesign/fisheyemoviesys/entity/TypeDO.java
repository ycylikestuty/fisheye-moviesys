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
@TableName("type")
public class TypeDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = 9132465453595977913L;

    /**
     * 类型名
     */
    private String name;
}

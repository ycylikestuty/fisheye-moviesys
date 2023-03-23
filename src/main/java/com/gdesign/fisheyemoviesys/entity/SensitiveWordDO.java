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
@TableName("sensitive_word")
public class SensitiveWordDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = -6433346054769938109L;

    /**
     * 违禁词
     */
    private String word;
}

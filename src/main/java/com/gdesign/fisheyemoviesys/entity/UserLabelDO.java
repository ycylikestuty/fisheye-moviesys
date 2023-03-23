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
@TableName("user_label")
public class UserLabelDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = -3614114689289888125L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 年份标签字符串
     */
    private String year;

    /**
     * 地区标签字符串
     */
    private String area;

    /**
     * 风格标签字符串
     */
    private String genre;
}

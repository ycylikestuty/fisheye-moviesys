package com.gdesign.fisheyemoviesys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_role")
public class UserRoleDO implements Serializable {
    private static final long serialVersionUID = -3674170882528743281L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;
}

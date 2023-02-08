package com.gdesign.fisheyemoviesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@TableName("role")
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends BaseInfoDO implements Serializable {

    private static final long serialVersionUID = -7770233780088211911L;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色编码
     * visitor-访客
     * ordinary-普通用户
     * admin-管理员
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
}

package com.gdesign.fisheyemoviesys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("menu")
public class MenuDO extends BaseInfoDO implements Serializable {
    private static final long serialVersionUID = -4788045142720352633L;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单类型
     * 0-目录
     * 1-菜单
     * 2-按钮
     */
    private Integer type;

    /**
     * 权限标识
     */
    private String perms;

    private String component;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private List<MenuDO> children = new ArrayList<>();
}

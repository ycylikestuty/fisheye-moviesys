package com.gdesign.fisheyemoviesys.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -4116796315357688941L;
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
}

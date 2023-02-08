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
public class RoleDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -8485203788426843060L;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色编码
     * visitor-访客
     * ordinary-普通用户
     * manage-管理员
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
}

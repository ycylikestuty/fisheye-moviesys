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
public class UserDTO extends BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -6267837482920884822L;

    /**
     * 等级
     */
    private Long grade;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     * 0-正常
     * 1-禁言
     */
    private Integer status;

    /**
     * 头像链接
     */
    private String img;
}

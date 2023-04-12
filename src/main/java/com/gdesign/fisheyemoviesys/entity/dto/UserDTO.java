package com.gdesign.fisheyemoviesys.entity.dto;


import lombok.*;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 用户注册时的地区数组
     */
    private List<Long> area;

    /**
     * 用户注册时的电影类型数组
     */
    private List<String> type;

    /**
     * 用户注册时的电影年份数组
     */
    private List<String> year;
}

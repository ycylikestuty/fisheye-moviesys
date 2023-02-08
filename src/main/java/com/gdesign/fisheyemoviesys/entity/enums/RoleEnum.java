package com.gdesign.fisheyemoviesys.entity.enums;

import lombok.Getter;

/**
 * @author ycy
 */

@Getter
public enum RoleEnum {
    VISITOR("visitor","访客"),
    ORDINARY("ordinary","普通用户"),
    MANAGE("admin","管理员");

    private final String msg;
    private final String desc;
    RoleEnum(String msg,String desc){
        this.msg=msg;
        this.desc=desc;
    }
}

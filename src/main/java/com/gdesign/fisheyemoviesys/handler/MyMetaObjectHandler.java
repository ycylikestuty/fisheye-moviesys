package com.gdesign.fisheyemoviesys.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ycy
 * mybatis-Plus自动填充功能
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //自动填充的字段名，字段类型要和数据库中的一致，否则会失败
        this.strictInsertFill(metaObject, "version", () -> Long.valueOf(1), Long.class);
        this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "modifyTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "deleted", () -> Integer.valueOf(0), Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "modifyTime", () -> new Date(), Date.class);
    }
}

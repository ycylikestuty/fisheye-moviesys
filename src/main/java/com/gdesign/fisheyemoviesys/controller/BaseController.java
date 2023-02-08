package com.gdesign.fisheyemoviesys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdesign.fisheyemoviesys.utils.RedisUtil;
import org.springframework.web.bind.ServletRequestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ycy
 */
public class BaseController<T> {
    @Resource
    HttpServletRequest request;

    @Resource
    RedisUtil redisUtil;

    public Page<T> getPage() {
        int pageNum = ServletRequestUtils.getIntParameter(request, "pageNum", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
        return new Page<T>(pageNum, pageSize);
    }
}

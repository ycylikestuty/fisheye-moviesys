package com.gdesign.fisheyemoviesys.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.exception.CaptchaException;
import com.gdesign.fisheyemoviesys.handler.LoginFailureHandler;
import com.gdesign.fisheyemoviesys.utils.RedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ycy
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if ("/login".equals(url) && request.getMethod().equals("POST")) {
            try {
                validate(request);
            } catch (CaptchaException e) {
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 验证码校验
     */
    private void validate(HttpServletRequest request) {
        String key = request.getParameter("key");
        String code = request.getParameter("code");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码为空");
        }
        Object hget = redisUtil.hget(Constants.CAPTCHA_KEY, key);
        if (!code.equals(hget)) {
            throw new CaptchaException("验证码错误");
        }
        if (hget == null || hget.equals("")) {
            throw new CaptchaException("刷新验证码");
        }
        //验证码校验成功则将redis中的验证码删除
        redisUtil.hdel(Constants.CAPTCHA_KEY, key);
    }
}

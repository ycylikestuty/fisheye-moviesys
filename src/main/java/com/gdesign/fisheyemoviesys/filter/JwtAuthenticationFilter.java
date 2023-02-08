package com.gdesign.fisheyemoviesys.filter;

import cn.hutool.core.util.StrUtil;
import com.gdesign.fisheyemoviesys.service.impl.UserDetailServiceImpl;
import com.gdesign.fisheyemoviesys.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ycy
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserDetailServiceImpl userDetailService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtil.getHeader());

        //若请求不包含jwt，对该请求不进行拦截，而是继续往后走
        //不包含jwt相当于匿名访问，一些接口可以匿名访问
        //若是一些接口不能匿名访问，则进入AccessDeniedHandler（即JwtAccessDeniedHandler）进行处理
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        //请求包含jwt
        Claims claims = jwtUtil.getClaimsByToken(jwt);
        if (claims == null) {
            throw new JwtException("token异常");
        }
        if (jwtUtil.isTokenExpired(claims)) {
            throw new JwtException("token过期");
        }

        //jwt验证成功，获取jwt中的用户信息
        String userName = claims.getSubject();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, null,
                userDetailService.getUserAuthority(userName));
        log.info("该用户的权限为：" + userDetailService.getUserAuthority(userName));
        //将token交给SecurityContextHolder，set进它的context中，
        //后续就能通过调用SecurityContextHolder.getContext().getAuthentication().getPrincipal()等方法获取到当前登录的用户信息
        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }
}

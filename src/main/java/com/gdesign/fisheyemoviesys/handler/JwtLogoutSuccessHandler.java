package com.gdesign.fisheyemoviesys.handler;

import cn.hutool.json.JSONUtil;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import com.gdesign.fisheyemoviesys.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ycy
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //将之前置入SecurityContext中的用户信息进行清除
        //(通过创建SecurityContextLogoutHandler对象，调用它的logout方法完成
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        response.setContentType("application/json;charset=UTF-8");

        //采取 置空策略 来清除浏览器中保存的JWT
        response.setHeader(jwtUtil.getHeader(), "");
        ServletOutputStream outputStream = response.getOutputStream();

        Result success = Result.succ("退出成功");
//        ResponseMessageDTO result = ResponseMessageDTO.success("SuccessLogout");

        outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

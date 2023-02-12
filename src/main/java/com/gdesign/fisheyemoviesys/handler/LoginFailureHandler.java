package com.gdesign.fisheyemoviesys.handler;

import cn.hutool.json.JSONUtil;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        String message = exception.getMessage();
        if (!"验证码错误".equals(message) && !"请刷新验证码".equals(message)) {
            message = "用户名或密码错误1";
        }
        Result fail = Result.fail(message);
//        ResponseMessageDTO<String> fail= ResponseMessageDTO.<String>builder()
//                .code(ErrorCodeEnum.LOGIN_CODE.getCode())
//                .message(message)
//                .success(Boolean.FALSE)
//                .build();
        outputStream.write(JSONUtil.toJsonStr(fail).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        if (message.equals("请刷新验证码")) {
            return;
        }
    }
}

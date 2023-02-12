package com.gdesign.fisheyemoviesys.handler;

import cn.hutool.json.JSONUtil;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream outputStream = response.getOutputStream();

        //将错误信息返回给前端
//        ResponseMessageDTO<String> result= ResponseMessageDTO.<String>builder()
//                .code(ErrorCodeEnum.LOGIN_CODE.getCode())
//                .message(accessDeniedException.getMessage())
//                .success(Boolean.FALSE)
//                .build();
        Result fail = Result.fail(accessDeniedException.getMessage());
        outputStream.write(JSONUtil.toJsonStr(fail).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

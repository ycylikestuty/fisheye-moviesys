package com.gdesign.fisheyemoviesys.config;

import com.gdesign.fisheyemoviesys.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author ycy
 * 解决跨域请求
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //addAllowedOrigin(String origin)方法可以追加访问源地址
        //如果不使用"*"（允许全部访问源），则可以配置多条访问源来做控制
        //addAllowedOrigin是追加访问源地址，而setAllowedOrigins是可以直接设置多条访问源
        config.addAllowedOrigin(Constants.ADMIN_WEB);
        config.addAllowedOrigin(Constants.USER_WEB);
        config.setAllowCredentials(false);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        long maxAge = 24 * 60 * 60;
        config.setMaxAge(maxAge);
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(corsSource);
    }
}

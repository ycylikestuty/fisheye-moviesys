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
        config.addAllowedOrigin(Constants.ADMIN_WEB);
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

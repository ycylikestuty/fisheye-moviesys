package com.gdesign.fisheyemoviesys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ycy
 */
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置一个拦截器,如果访问路径是addResourceHandler中的路径
        //就将其映射到本地的addResourceLocations的这个路径上
        //即将图片的URL映射至服务器的本地文件夹
        registry.addResourceHandler("/img/**").addResourceLocations("file:E:/upload/");
        registry.addResourceHandler("/poster/**").addResourceLocations("file:E:/poster/");
    }
}

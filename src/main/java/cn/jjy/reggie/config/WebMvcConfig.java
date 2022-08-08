package cn.jjy.reggie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-07-19 05:43
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("calsspath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("calsspath:/front/");
    }
}

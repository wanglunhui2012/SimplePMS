package indi.simple.pms.config;

import indi.simple.pms.inteceptor.RequestInterceptor;
import indi.simple.pms.support.file.LocalFileTemplate;
import indi.simple.pms.support.jsonmultipart.JsonMultipartMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:31
 * @Description:
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LocalFileTemplate localFileTemplate;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600L); // 跨域请求中预检请求(Http Method为Option)的有效期，单位秒
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/"+localFileTemplate.getFileDownloadUrlPrefix()+"**").addResourceLocations("file:"+localFileTemplate.getFileUploadPath()).setCachePeriod(0);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").setCachePeriod(0);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JsonMultipartMethodArgumentResolver());
    }

    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor());
    }
}

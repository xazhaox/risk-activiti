package com.xazhao.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SpringMvcConfig.java
 * @Author AnZhaoxu
 * @Create 2024.04.11
 * @UpdateUser
 * @UpdateDate 2024.04.11
 * @Version 2024.0.1
 * @Description
 */

// @Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/",
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/templates/"
    };

    /**
     * 添加 ByteArrayHttpMessageConverter 转换器,解决(ResponseEntity<byte[]>)没有转换器报错问题
     *
     * @param converterList a list to add message converters to (initially an empty list) 转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converterList) {
        final ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        final List<MediaType> list = new ArrayList<>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        list.add(MediaType.ALL);
        arrayHttpMessageConverter.setSupportedMediaTypes(list);
        converterList.add(arrayHttpMessageConverter);
    }

    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
    }
}

package com.green.greengram.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

//@Component
@Configuration //빈등록(DI 주소값 받기 위해) >> WebMvcConfiguration 객체화
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final String uploadPath; //private 에 값을 넣어주기 위해서는 생성자랑 명시적 방법만 가능 (setter x)
    //setter은 객체화 이후에 사용할 수 있음 (final이 붙으면 setter 이용 x)

    public WebMvcConfiguration(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**").addResourceLocations("file:" + uploadPath + "/");

        //새로고침시 화면이 나타날 수 있도록 세팅
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/**") //classpath >> resource
                .resourceChain(true)
                .addResolver(new PathResourceResolver() { //익명 클래스 재정의
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);

                        if(resource.exists() && resource.isReadable()) { //존재하거나 읽을 수 있는 파일이면 return
                            return resource;
                        }
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // RestController의 모든 URL에 "/api" prefix를 설정
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
    }


}







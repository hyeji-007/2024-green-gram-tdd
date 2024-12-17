package com.green.greengram.config.security;
//Spring Security 세팅

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springdoc.webmvc.core.service.RequestService;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration // 메소드 빈등록이 있어야 의미가 있다. 메소드 빈등록이 싱글톤이 됨.
@RequiredArgsConstructor
public class WebSecurityConfig {
    // 스프링 시큐리티 기능 비활성화 (스프링 시큐리티가 관여하지 않았으면 하는 부분)
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                         .requestMatchers(new AntPathRequestMatcher("/static/**")); //static에서 security 기능 비활성화
//    }

    @Bean // 스프링이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다. (빈등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SqlSessionFactory sqlSessionFactory, OrderedFormContentFilter formContentFilter, RequestService requestBuilder) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 시큐리티가 세션을 사용하지 않는다.
                .httpBasic(h -> h.disable()) //SSR(Server Side Rendering)이 아니다. 화면을 만들지 않을것이기 때문에 비활성화 시킨다. 시큐리티 로그인창 나타나지 않을 것이다.
                .formLogin(form -> form.disable()) //폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) //보안관련 SSR이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/feed", "/api/feed/ver3", "/api/").authenticated() //로그인이 되어 있어야만 사용 가능
                .anyRequest().permitAll() //나머지 요청은 모두 허용
                )

                .build();
    }

}

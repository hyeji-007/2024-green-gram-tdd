package com.green.greengram.config.jwt;

import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 통합 테스트때 사용
class TokenProviderTest {
    //테스트는 생성자를 이용한 DI가 불가능
    //DI방법은 필드, setter메소드, 생성자
    //테스트 때는 필드 주입방식을 사용한다.

    @Autowired //리플렉션 API를 이용해서 setter가 없어도 주입 가능
    private TokenProvider tokenProvider;

    @Test
    public void generateToken() {
        //Given (준비단계)
        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        jwtUser.setRoles(roles);

        //When (실행단계)
        String token = tokenProvider.generateToken(jwtUser, Duration.ofHours(3));

        //Then (검증단계)
        assertNotNull(token);

        System.out.println("token: " + token);

    }

    @Test
    void validToken() {
        //1분 (이미 지남)
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMTY3MiwiZXhwIjoxNzM0NDAxNzMyLCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.KsewH2oby-NaGC1sgTGAffbMcp9uQpv5P5qIzZ0Hm4aisLN0V0avL3jEJv14k3J2";

        boolean result = tokenProvider.validToken(token);

        assertFalse(result);
    }

    @Test
    void getAuthentication() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMzc2NCwiZXhwIjoxNzM0NDE0NTY0LCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.GhJuDzgxTU9kyajUCghb-q7UVSvWXHdziUNKGeP40rP3OkKNV05tJS_X_UKDDR0u"; //3시간 짜리

        Authentication authentication = tokenProvider.getAuthentication(token);

        assertNotNull(authentication);

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        JwtUser jwtUser = myUserDetails.getJwtUser();

        JwtUser expectedJwtUser = new JwtUser();
        expectedJwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        expectedJwtUser.setRoles(roles);

        assertEquals(expectedJwtUser, jwtUser);
    }

}
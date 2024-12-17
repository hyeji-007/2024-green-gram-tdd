package com.green.greengram.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
public class TokenProvider {
    private final ObjectMapper objectMapper; //Jackson 라이브러리
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;


    public TokenProvider(ObjectMapper objectMapper, JwtProperties jwtProperties) {
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
    }

    // JWT 생성
    public String generateToken(JwtUser jwtUser, Duration expiredAt) {
        Date now = new Date();
        return makeToken(jwtUser, new Date(now.getTime() + expiredAt.toMillis()));
        // 현재 시점으로부터 일정 기간 이후의 Date 객체를 만든다.
        // jwtUser에는 payload에 담을 내용
    }

    private String makeToken(JwtUser jwtUser, Date expiry) {
        // JWT 암호화 >> 암호화된 문자열을 만드는 것
        return Jwts.builder()
                .header().type("JWT")
                .and()
                .issuer(jwtProperties.getIssuer()) // yaml에 있는 issuer에서 e-mail 확인, green@green.kr
                .issuedAt(new Date()) // token 생성 시간
                .expiration(expiry) // token 만료 시간
                .claim("signedUser", makeClaimByUserToString(jwtUser)) // 비공개 claim
                .signWith(secretKey) // 암호화
                .compact(); // private String makeToken >> return type이 String
    }

    private String makeClaimByUserToString(JwtUser jwtUser) {
        // 문자열이 아닌 객체를 담아야 한다.
        // String을 객체화 하는 과정을 직렬화라고 한다.
        // 객체 자체를 JWT에 담고 싶어서 객체를 직렬화
        // 직렬화: jwtUser에 담고있는 데이터를 JSON형태의 문자열로 변환
        try {
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validToken(String token) {

        try {
            // JWT 복호화
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public UserDetails getUserDetailsFromToken(String token) {
        Claims claims = getClaims(token);
        String json = (String)claims.get("signedUser");
        JwtUser jwtUser = objectMapper.convertValue(json, JwtUser.class);
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setJwtUser(jwtUser);
        return userDetails;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) //서명 검증
                .build()
                .parseSignedClaims(token) //토큰에 담겨있는 signedUserId를 이용해서
                .getPayload(); //payload를 가져온다.

    }


}

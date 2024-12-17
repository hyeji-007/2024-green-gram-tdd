package com.green.greengram.config.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode //Equals, HashCode 메소드 오버라이딩 >> 객체 비교와 해시 코드를 자동으로 생성
// 논리적 동등성: 메모리 주소값이 다르더라도 필드 값이 같다면 동등하다고 판단
public class JwtUser {
    private long signedUserId;
    private List<String> roles; //인가(권한)처리 때 사용, ROLE_이름, ROLE_USER, ROLE_ADMIN

}

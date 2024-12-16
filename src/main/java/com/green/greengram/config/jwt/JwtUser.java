package com.green.greengram.config.jwt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtUser {
    private long signedUserId;
    private List<String> roles; //인가(권한)처리 때 사용

}

package com.green.greengram.config.security;

import com.green.greengram.config.jwt.JwtUser;
import io.jsonwebtoken.Jwt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public JwtUser getSignedUser() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext()
                                                                         .getAuthentication()
                                                                         .getPrincipal(); //myUserDetails

        return myUserDetails.getJwtUser(); //from TokenProvider
    }

    public long getSignedUserId() {
        return getSignedUser().getSignedUserId();
    }
}

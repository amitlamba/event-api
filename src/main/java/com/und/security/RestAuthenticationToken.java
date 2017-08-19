package com.und.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RestAuthenticationToken  extends UsernamePasswordAuthenticationToken {

    private String key;

    public RestAuthenticationToken(String principal, String credentials, String key) {
        super(principal, credentials);
        this.key = key;
    }

    public RestAuthenticationToken(EventUser principal, String credentials,
                                     Collection<? extends GrantedAuthority> authorities, String key) {
        super(principal, credentials, authorities);
        this.key = key;
    }



    public String getKey() {
        return key;
    }

}

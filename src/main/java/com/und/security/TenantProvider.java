package com.und.security;

import org.springframework.stereotype.Component;

@Component
public class TenantProvider {

    public String getTenant(){
        if(AuthenticationUtils.isUserLoggedIn())
            return AuthenticationUtils.getId();
        return "";
    }
}

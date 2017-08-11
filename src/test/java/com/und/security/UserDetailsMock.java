package com.und.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * Created by shiv on 21/07/17.
 */
public class UserDetailsMock extends EventUser {


    public UserDetailsMock(String username){
        super(
              1L,
               username,
                "firstname",
                "lastname",
                "email",
                "password",
                null,
                true,
                new Date(),
                "secret",
                "key"
        );
    }
    public UserDetailsMock(Long id, String username,
                           String firstname,
                           String lastname,
                           String email, String password, Collection<? extends GrantedAuthority> authorities,
                           boolean enabled, Date lastPasswordResetDate, String secret, String key) {
        super(id, username, firstname, lastname, email, password, authorities, enabled, lastPasswordResetDate, secret, key);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

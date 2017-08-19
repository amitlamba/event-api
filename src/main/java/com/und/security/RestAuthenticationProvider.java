package com.und.security;

import com.und.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * Created by shiv on 14/08/17.
 */
@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTokenUtil restTokenUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestAuthenticationToken token = (RestAuthenticationToken) authentication;
        EventUser user = RestUserFactory.create(userRepository.findByUsername(token.getName()));

        if(user != null || token.getKey().equalsIgnoreCase(user.getKey())) {
            if (restTokenUtil.validateToken(user.getKey(), user)) {
                return new RestAuthenticationToken(user, user.getPassword(), user.getAuthorities(), token.getKey());
            }
        }


        throw new BadCredentialsException("The credentials are invalid");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RestAuthenticationToken.class.equals(authentication);
    }

}
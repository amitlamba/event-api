package com.und.security.controller;

import com.und.security.RestTokenUtil;
import com.und.security.EventUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRestController {

    @Value("${security.header.token}")
    private String tokenHeader;

    @Value("${security.header.username}")
    private String usernameHeader;

    @Autowired
    private RestTokenUtil restTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public EventUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = request.getHeader(usernameHeader);
        EventUser user = (EventUser) userDetailsService.loadUserByUsername(username);
        String usernameFromToken = restTokenUtil.getUsernameFromToken(token, user.getSecret());
        return user;
    }

}

package com.und.security.controller;

import com.und.security.RestAuthenticationRequest;
import com.und.security.RestTokenUtil;
import com.und.security.EventUser;
import com.und.security.service.SecurityAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationRestController {

    @Value("${security.header.token}")
    private String tokenHeader;

    @Value("${security.header.username}")
    private String usernameHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RestTokenUtil restTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "${security.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody RestAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        EventUser user = (EventUser) userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = restTokenUtil.generateToken(user, device);

        // Return the token
        return ResponseEntity.ok(new SecurityAuthenticationResponse(token));
    }

    @RequestMapping(value = "${security.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request
            //, @RequestHeader(tokenHeader)
                                                              ) {
        String token = request.getHeader(tokenHeader);
        String username = request.getHeader(usernameHeader);
        EventUser user = (EventUser) userDetailsService.loadUserByUsername(username);
        String usernameFromToken = restTokenUtil.getUsernameFromToken(token,user.getSecret());

        if (restTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate(), user.getSecret())) {
            String refreshedToken = restTokenUtil.refreshToken(token, user.getSecret());
            return ResponseEntity.ok(new SecurityAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}

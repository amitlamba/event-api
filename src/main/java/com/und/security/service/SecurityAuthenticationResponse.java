package com.und.security.service;

import java.io.Serializable;

/**
 * Created by shiv on 21/07/17.
 */
public class SecurityAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public SecurityAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}

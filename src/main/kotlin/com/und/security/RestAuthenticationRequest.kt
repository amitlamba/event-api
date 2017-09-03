package com.und.security

import java.io.Serializable

/**
 * Created by shiv on 21/07/17.
 */
class RestAuthenticationRequest : Serializable {
    var username: String? = null
    var password: String? = null

    constructor() : super() {}

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    companion object {

        private const val serialVersionUID = -8445943548965154778L
    }
}

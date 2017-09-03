package com.und.security

import com.und.common.utils.DateUtils
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mobile.device.Device
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class RestTokenUtil : Serializable {

    @Autowired
    lateinit private var dateUtils: DateUtils

    @Value("\${security.expiration}")
    private var expiration: Long? = 0

    fun getUsernameFromToken(token: String, secret: String): String? {
        val username: String?
        val claims = getClaimsFromToken(token, secret)
        username = claims!!.subject
        return username
    }

    fun getCreatedDateFromToken(token: String, secret: String): Date? {
        val created: Date?
        val claims = getClaimsFromToken(token, secret)
        created = Date(claims!![CLAIM_KEY_CREATED] as Long)

        return created
    }

    fun getExpirationDateFromToken(token: String, secret: String): Date? {
        var expiration: Date?
        try {
            val claims = getClaimsFromToken(token, secret)
            expiration = claims!!.expiration
        } catch (e: Exception) {
            expiration = null
        }

        return expiration
    }

    fun getAudienceFromToken(token: String, secret: String): String? {
        var audience: String?
        try {
            val claims = getClaimsFromToken(token, secret)
            audience = claims!![CLAIM_KEY_AUDIENCE] as String
        } catch (e: Exception) {
            audience = null
        }

        return audience
    }

    private fun getClaimsFromToken(token: String, secret: String): Claims? {
        val claims: Claims? =
                Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .body

        return claims
    }

    private fun isTokenExpired(token: String, secret: String): Boolean {
        val expiration = getExpirationDateFromToken(token, secret)
        return expiration!!.before(dateUtils.now())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date?, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created!!.before(lastPasswordReset)
    }

    private fun generateAudience(device: Device): String {
       val audience = when {
            device.isNormal -> AUDIENCE_WEB
            device.isMobile -> AUDIENCE_MOBILE
            device.isTablet -> AUDIENCE_TABLET
            else -> AUDIENCE_UNKNOWN
        }
        return audience
    }

    //9654339369 gas agency number
    private fun ignoreTokenExpiration(token: String, secret: String): Boolean {
        val audience = getAudienceFromToken(token, secret)
        return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
    }

    fun generateToken(userDetails: UndUserDetails, device: Device): String {
        val claims = HashMap<String, Any>()
            claims.put(CLAIM_KEY_USERNAME, userDetails.username)
            claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device))

            val createdDate = dateUtils.now()
            claims.put(CLAIM_KEY_CREATED, createdDate)
            return doGenerateToken(claims, userDetails.secret)
    }

    private fun doGenerateToken(claims: Map<String, Any>, secret: String): String {
        val createdDate = claims[CLAIM_KEY_CREATED] as Date
        val expirationDate = Date(createdDate.time + expiration!! * 1000)

        println("doGenerateToken " + createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date, secret: String): Boolean {
        val created = getCreatedDateFromToken(token, secret)
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token, secret) || ignoreTokenExpiration(token, secret))
    }

    fun refreshToken(token: String, secret: String): String {
        val refreshedToken: String
        val claims = getClaimsFromToken(token, secret)
        claims!!.put(CLAIM_KEY_CREATED, dateUtils.now())
        refreshedToken = doGenerateToken(claims, secret)

        return refreshedToken
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val user = userDetails as UndUserDetails
        val username = getUsernameFromToken(token, user.secret)
        val created = getCreatedDateFromToken(token, user.secret)
        //final Date expiration = getExpirationDateFromToken(token);
        return username == user.username
                && !isTokenExpired(token, user.secret)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate)
    }

    companion object {

        private const val serialVersionUID = -3301605591108950415L

        internal val CLAIM_KEY_USERNAME = "sub"
        internal val CLAIM_KEY_AUDIENCE = "audience"
        internal val CLAIM_KEY_CREATED = "created"
        internal val CLAIM_KEY_EXPIRED = "exp"

        internal val AUDIENCE_UNKNOWN = "unknown"
        internal val AUDIENCE_WEB = "web"
        internal val AUDIENCE_MOBILE = "mobile"
        internal val AUDIENCE_TABLET = "tablet"
    }
}
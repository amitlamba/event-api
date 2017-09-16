package com.und.security

import com.und.common.utils.DateUtils
import com.und.security.model.UndUserDetails
import com.und.security.utils.RestTokenUtil
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.assertj.core.util.DateUtil
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

/**
 * Created by shiv on 21/07/17.
 */
class RestTokenUtilTest {

    @Mock
    lateinit private var dateUtilsMock: DateUtils

    @InjectMocks
    lateinit private var restTokenUtil: RestTokenUtil

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        ReflectionTestUtils.setField(restTokenUtil, "expiration", 3600L) // one hour
        //ReflectionTestUtils.setField(restTokenUtil, "secret", "mySecret");
    }

    @Test
    @Throws(Exception::class)
    fun testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() {
        `when`(dateUtilsMock.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now())

        val token = createToken()
        val laterToken = createToken()

        assertThat(token).isNotEqualTo(laterToken)
    }

    @Test
    @Throws(Exception::class)
    fun getUsernameFromToken() {
        `when`(dateUtilsMock.now()).thenReturn(DateUtil.now())

        val token = createToken()

        assertThat(restTokenUtil.getUsernameFromToken(token!!, "secret")).isEqualTo(TEST_USER)
    }

    @Test
    @Throws(Exception::class)
    fun getCreatedDateFromToken() {
        val now = DateUtil.now()
        `when`(dateUtilsMock.now()).thenReturn(now)

        val token = createToken()

        assertThat(restTokenUtil.getCreatedDateFromToken(token!!, "secret")).hasSameTimeAs(now)
    }

    @Test
    @Throws(Exception::class)
    fun getExpirationDateFromToken() {
        val now = DateUtil.now()
        `when`(dateUtilsMock.now()).thenReturn(now)
        val token = createToken()

        val expirationDateFromToken = restTokenUtil.getExpirationDateFromToken(token!!, "secret")
        assertThat(DateUtil.timeDifference(expirationDateFromToken!!, now)).isCloseTo(3600000L, within(1000L))
    }

    @Test
    @Throws(Exception::class)
    fun getAudienceFromToken() {
        `when`(dateUtilsMock.now()).thenReturn(DateUtil.now())
        val token = createToken()

        assertThat(restTokenUtil.getAudienceFromToken(token!!, "secret")).isEqualTo(RestTokenUtil.AUDIENCE_WEB)
    }

    // TODO write tests
    //    @Test
    //    public void canTokenBeRefreshed() throws Exception {
    //    }
    //
    //    @Test
    //    public void refreshToken() throws Exception {
    //    }
    //
    //    @Test
    //    public void validateToken() throws Exception {
    //    }

    private fun createClaims(creationDate: String): Map<String, Any> {
        val claims = HashMap<String, Any>()
        claims.put(RestTokenUtil.CLAIM_KEY_USERNAME, TEST_USER)
        claims.put(RestTokenUtil.CLAIM_KEY_AUDIENCE, "testAudience")
        claims.put(RestTokenUtil.CLAIM_KEY_CREATED, DateUtil.parseDatetime(creationDate))
        return claims
    }

    private fun createToken(): String? {
        val device = DeviceMock()
        device.isNormal = true

        return restTokenUtil.generateToken(UndUserDetails(id=1L,username = TEST_USER,secret = "secret",key="key", password = ""), device)
    }

    companion object {

        private val TEST_USER = "testUser"
    }

}
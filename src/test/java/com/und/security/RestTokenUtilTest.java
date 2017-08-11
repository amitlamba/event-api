package com.und.security;

import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import com.und.common.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.*;

/**
 * Created by shiv on 21/07/17.
 */
public class RestTokenUtilTest {

    private static final String TEST_USER = "testUser";

    @Mock
    private DateUtils dateUtilsMock;

    @InjectMocks
    private RestTokenUtil restTokenUtil;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(restTokenUtil, "expiration", 3600L); // one hour
        //ReflectionTestUtils.setField(restTokenUtil, "secret", "mySecret");
    }

    @Test
    public void testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() throws Exception {
        when(dateUtilsMock.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now());

        final String token = createToken();
        final String laterToken = createToken();

        assertThat(token).isNotEqualTo(laterToken);
    }

    @Test
    public void getUsernameFromToken() throws Exception {
        when(dateUtilsMock.now()).thenReturn(DateUtil.now());

        final String token = createToken();

        assertThat(restTokenUtil.getUsernameFromToken(token,"secret")).isEqualTo(TEST_USER);
    }

    @Test
    public void getCreatedDateFromToken() throws Exception {
        final Date now = DateUtil.now();
        when(dateUtilsMock.now()).thenReturn(now);

        final String token = createToken();

        assertThat(restTokenUtil.getCreatedDateFromToken(token,"secret")).hasSameTimeAs(now);
    }

    @Test
    public void getExpirationDateFromToken() throws Exception {
        final Date now = DateUtil.now();
        when(dateUtilsMock.now()).thenReturn(now);
        final String token = createToken();

        final Date expirationDateFromToken = restTokenUtil.getExpirationDateFromToken(token,"secret");
        assertThat(DateUtil.timeDifference(expirationDateFromToken, now)).isCloseTo(3600000L, within(1000L));
    }

    @Test
    public void getAudienceFromToken() throws Exception {
        when(dateUtilsMock.now()).thenReturn(DateUtil.now());
        final String token = createToken();

        assertThat(restTokenUtil.getAudienceFromToken(token,"secret")).isEqualTo(RestTokenUtil.AUDIENCE_WEB);
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

    private Map<String, Object> createClaims(String creationDate) {
        Map<String, Object> claims = new HashMap();
        claims.put(RestTokenUtil.CLAIM_KEY_USERNAME, TEST_USER);
        claims.put(RestTokenUtil.CLAIM_KEY_AUDIENCE, "testAudience");
        claims.put(RestTokenUtil.CLAIM_KEY_CREATED, DateUtil.parseDatetime(creationDate));
        return claims;
    }

    private String createToken() {
        final DeviceMock device = new DeviceMock();
        device.setNormal(true);

        return restTokenUtil.generateToken(new UserDetailsMock(TEST_USER), device);
    }

}
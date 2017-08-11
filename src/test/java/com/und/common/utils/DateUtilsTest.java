package com.und.common.utils;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by shiv on 21/07/17.
 */
public class DateUtilsTest {
    @Test
    public void now() throws Exception {
        assertThat(new DateUtils().now()).isCloseTo(new Date(), 1000);
    }

}
package com.und.common.utils

import org.junit.Test
import java.util.*
import org.assertj.core.api.Assertions.*

/**
 * Created by shiv on 21/07/17.
 */
class DateUtilsTest {


    @Test
    fun now() {
        assertThat(DateUtils().now()).isCloseTo(Date(), 1000)
    }

}
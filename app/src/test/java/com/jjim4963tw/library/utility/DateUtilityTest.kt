package com.jjim4963tw.library.utility

import org.junit.*

import org.junit.Assert.*

class DateUtilityTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun isAFewSecondsAgo() {
        val result = DateUtility().isAFewSecondsAgo(99999)
        assertEquals(true, result)
    }


}
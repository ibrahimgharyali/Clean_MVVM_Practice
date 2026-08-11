package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.domain.AnalyticsLogger
import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsLoggerTest {


    @Test
    fun `register log event and verify`() {
        val logger = AnalyticsLogger()
        var eventName = ""
        logger.addListener { event ->
            eventName = event.eventName
        }
        logger.logEvent("ButtonCLiked", emptyMap())
        assertEquals("ButtonCLiked", eventName)

    }
}
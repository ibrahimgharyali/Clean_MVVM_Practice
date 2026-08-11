package com.ibrahimgharyali.mypracticeapplication

import org.junit.Assert.assertEquals
import org.junit.Test

class ComputeMatchResutTest {


    @Test
    fun `home team wins`() {
        val result = computeResultText(
            "homeTeam",
            "awayTeam",
            20,
            10,
            "ft"
        )
        val expected = "homeTeam won after full-time"
        assertEquals(expected, result)
    }

    @Test
    fun `away team wins`() {
        val result = computeResultText(
            "homeTeam",
            "awayTeam",
            20,
            30,
            "ft"
        )
        val expected = "awayTeam won after full-time"
        assertEquals(expected, result)
    }

    @Test
    fun `match draw`() {
        val result = computeResultText(
            "homeTeam",
            "awayTeam",
            10,
            10,
            "ft"
        )
        val expected = "Draw after full-time"
        assertEquals(expected, result)
    }
}
private fun computeResultText(
    homeTeam: String,
    awayTeam: String,
    homeScore: Int,
    awayScore: Int,
    status: String
): String {
    val statusText = when (status.lowercase()) {
        "ft", "full_time" -> "after full-time"
        "ht" -> "at half-time"
        else -> status
    }
    return when {
        homeScore > awayScore -> "$homeTeam won $statusText"
        awayScore > homeScore -> "$awayTeam won $statusText"
        else -> "Draw $statusText"
    }
}
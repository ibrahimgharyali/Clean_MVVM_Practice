package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.domain.PriceTracker
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceTrackerTest {

    @Test
    fun `updatePrice then getHighValueTickers returns the ticker if price greater than 10,000`() {
        val pricetracker = PriceTracker()
        pricetracker.updatePrice("ticket1", 20000.0)
        pricetracker.updatePrice("ticket2", 9000.0)
        pricetracker.updatePrice("ticket3", 2000.0)

        assertEquals(1, pricetracker.getHighValueTickers().size)
        assertEquals("ticket1", pricetracker.getHighValueTickers()[0])

    }

    @Test
    fun `A ticker at exactly $10,000 boundary caseexclusive`(){
        val pricetracker = PriceTracker()
        pricetracker.updatePrice("ticket1", 10000.0)
        assertEquals(0, pricetracker.getHighValueTickers().size)

    }

    @Test
    fun `Updating the same ticker twice does the second value overwrite the first`(){
        val pricetracker = PriceTracker()
        pricetracker.updatePrice("ticket1", 20000.0)
        pricetracker.updatePrice("ticket3", 30000.0)
        assertEquals("ticket3", pricetracker.getHighValueTickers()[0])

        pricetracker.updatePrice("ticket1", 90000.0)
        assertEquals("ticket1", pricetracker.getHighValueTickers()[0])
    }

    @Test
    fun `Multiple tickers returned in correct descending order`(){
        val pricetracker = PriceTracker()
        pricetracker.updatePrice("ticket1", 20000.0)
        pricetracker.updatePrice("ticket2", 90000.0)
        pricetracker.updatePrice("ticket3", 30000.0)

        assertEquals(3, pricetracker.getHighValueTickers().size)
        assertEquals("ticket2", pricetracker.getHighValueTickers()[0])
        assertEquals("ticket1", pricetracker.getHighValueTickers()[2])
    }
}
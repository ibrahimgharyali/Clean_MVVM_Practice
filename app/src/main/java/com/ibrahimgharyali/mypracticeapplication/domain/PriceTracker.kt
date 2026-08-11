package com.ibrahimgharyali.mypracticeapplication.domain

import java.util.concurrent.ConcurrentHashMap

class PriceTracker {
    val priceMap = ConcurrentHashMap<String, Double>()


    fun updatePrice(ticker: String, price: Double) {
        priceMap[ticker] = price
    }

    fun getHighValueTickers(): List<String> {
        return priceMap.entries
            .filter { it.value > 10000.0 }
            .sortedByDescending { it.value }
            .map { it.key }
    }
}
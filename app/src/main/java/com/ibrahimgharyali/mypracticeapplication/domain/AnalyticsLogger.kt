package com.ibrahimgharyali.mypracticeapplication.domain

class AnalyticsLogger {
    private val recentEvents = ArrayDeque<AnalyticsEvent>()
    private val maxSize = 10

    //    private var listener : ((AnalyticsEvent) -> Unit)? = null // single
    private val listeners = mutableListOf<(AnalyticsEvent) -> Unit>()
    fun logEvent(eventName: String, params: Map<String, Any>) {
        if(recentEvents.size >= maxSize) {
            recentEvents.removeFirst() // evict oldest
        }
        recentEvents.addLast(AnalyticsEvent(eventName, params)) // add newest
    }

    fun getRecentEvents(): List<AnalyticsEvent>  = recentEvents.toList()

    fun addListener(listener: (AnalyticsEvent) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (AnalyticsEvent) -> Unit) {
        listeners.remove(listener)
    }
}
data class AnalyticsEvent(
    val eventName: String,
    val params: Map<String, Any>
)
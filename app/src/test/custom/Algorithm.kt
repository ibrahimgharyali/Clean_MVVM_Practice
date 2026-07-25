package com.ibrahimgharyali.mypracticeapplication.custom

import java.util.Stack

class Demo {

    // Hotel Price Fluctuation (Max Profit)
    fun  netProfitOnFluctuation(prices : IntArray) : Int {
        if (prices.size <= 1) return 0
        var maxProfit = 0
        var minPrice = prices[0]
        for (i in 1..<prices.size) {
            val currentPrice = prices[i]
            if(currentPrice > minPrice){
                maxProfit = maxOf(maxProfit, currentPrice-minPrice)
            } else {
                minPrice = currentPrice
            }
        }
        return maxProfit
    }

    // Hotel booking Conflicts, interval Overlap
    // Input: bookings = [[7, 10], [2, 4], [15, 19]]
    // Output: true
    // Explanation: None of the bookings overlap.
    //
    // Input: bookings = [[0, 30], [5, 10], [15, 20]]
    // Output: false
    // Explanation: [0, 30] overlaps with [5, 10] and [15, 20].
    fun checkRoomOverlapping(bookings: Array<IntArray>): Boolean {
        if (bookings.size <= 1) return true
        // Step 1: Sort by check-in time
        bookings.sortBy { it[0] }

        // Step 2: Track the end time of the previous booking
        var lastCheckout = bookings[0][1]

        // Step 3: Iterate starting from the second booking
        for (i in 1 until bookings.size) {
            val currentCheckIn = bookings[i][0]
            val currentCheckOut = bookings[i][1]

            // Conflict: Next check-in happens BEFORE previous check-out
            if (currentCheckIn < lastCheckout) {
                return false
            }

            // Update the last checkout time
            lastCheckout = currentCheckOut
        }
        return true

        // Kotlin way Compares every adjacent pair: (prev, next)
        return bookings.asSequence()
            .zipWithNext()
            .all { (prev, next) -> prev[1] <= next[0] }
    }


    // 3Sum
    // return all the unique triplets [nums[i], nums[j], nums[k]] where
    // nums[i]+nums[j]+nums[k]=0
    fun threeSum(nums: IntArray) : List<List<Int>> {
        if(nums.size < 3) return emptyList()

        nums.sort()

        return buildList{
            // Loop up to the 3rd to last element
            for (i in nums.indices - 2) {
                // skip to avoid duplicate triplets
                if(i > 0 && nums[i] == nums[i-1]) continue

                // you can't add three positive numbers to get 0
                if(nums[i] > 0) break

                val x = nums[i]
                var left = i+1
                var right = nums.lastIndex
                while(left < right) {
                    val y = nums[left]
                    val z = nums[right]
                    val sum = x+y+z
                    when {
                        sum == 0 -> {
                            add(listOf(nums[i], y, z))
                            while (left < right && nums[left] == y) left++
                            while (left < right && nums[right] == z) right--
                        }
                        sum < 0 -> left++
                        else -> right--
                    }
                }
            }
        }
    }


    // Least Recently Used (LRU) Cache.
    class LRUCache(val capacity: Int) {
        private class Node(
            val key: Int = 0,
            var value: Int = 0,
            var prev: Node? = null,
            var next: Node? = null
        )

        private val cache = HashMap<Int, Node>()
        private var head = Node(0,0)
        private var tail = Node(0,0)


        init {
            head.next = tail // Most Recently Used
            tail.prev = head // Least Recently Used
        }

        fun get(key: Int): Int { // return value or -1
            val node = cache[key] ?: return -1
            moveToHead(node)
            return node.value
        }

        fun put(key: Int, value: Int) { //
            if(cache.containsKey(key)) { // update existing node
                val node = cache[key]!!
                node.value = value
                moveToHead(node)
                return
            }
            val node = Node(key, value) // add new node
            cache[key] = node
            addNodeToHead(node)
            if(cache.size > capacity) { // evict LRU node, tail.prev
                val lru = tail.prev!!
                removeNode(lru)
                cache.remove(lru.key)
            }
        }

        private fun addNodeToHead(node: Node) {
            node.next = head.next
            node.prev = head
            head.next?.prev = node
            head.next = node
        }
        private fun removeNode(node: Node) {
            node.prev?.next = node.next
            node.next?.prev = node.prev
        }
        private fun moveToHead(node: Node) {
            removeNode(node)
            addNodeToHead(node)
        }
    }

    // Valid Parentheses
    fun validParenthesis(s: String) : Boolean {
        if (s.length %2 != 0) return false
        val stack = ArrayDeque<Char>(s.length)

        for (c in s) {
            when(c) {
                '[', '{', '(' -> stack.addLast(c)
                ']' -> if(stack.isEmpty() || stack.removeLast() != '[') return false
                '}' -> if(stack.isEmpty() || stack.removeLast() != '{') return false
                ')' -> if(stack.isEmpty() || stack.removeLast() != '(') return false
                else -> continue
            }
        }
        return stack.isEmpty()
    }

    // Decode string
    // Given an encoded string s, return its decoded string.
    // Example 1:
    // Input: s = "3[a]2[bc]"
    // Output: "aaabcbc"
    //
    // Example 2:
    // Input: s = "3[a2[c]]"
    // Output: "accaccacc"
    fun decodeString(s: String) : String {
        if(s.isEmpty()) return s
        val stringStack = Stack<StringBuilder>()
        val countStack = Stack<Int>()
        var currentString = StringBuilder()
        var count = StringBuilder()

        for (c in s) {
            when {
                c.isDigit() -> count.append(c)
                c == '[' -> {
                    countStack.push(count.toString().toInt())
                    stringStack.push(currentString)
                    count = StringBuilder()
                    currentString = StringBuilder()
                }
                c == ']' -> {
                    val str = stringStack.pop()
                    var times = countStack.pop()
                    while (times > 0) {
                        str.append(currentString)
                        times--
                    }
                    currentString = str
                }
                else -> currentString.append(c)
            }
        }
        return currentString.toString()
    }


    // Meeting Rooms II
    // start and end times [[s1,e1],[s2,e2],...] (si < ei), find the minimum number of conference rooms required.

    //  Definition of Interval:

    class Interval (
        val start: Int = 0,
        val end: Int = 0
    )
    fun minMeetingRooms(intervals: List<Interval>): Int {
        if (intervals.size <= 1) return intervals.size
        val n = intervals.size
        // split start and end array and sort it ascending order
        val start = IntArray(n) {intervals[it].start}.apply { sort() }
        val end = IntArray(n) {intervals[it].end}.apply { sort() }


        var minRoom = 0
        var endPtr = 0
        for(startPtr in 0..start.size) {
            if (start[startPtr] < end[endPtr]) {
                minRoom++
            } else {
                endPtr++
            }
        }
        return minRoom
    }


    //Eliminate Maximum Number of Monsters / "Airplanes Shot Down"

    fun eliminateMonster(dist: IntArray, speed: IntArray): Int {
        // calculate arrivaltime and sort it ascending order.
        val arrivalTimes = DoubleArray(dist.size) {
            dist[it].toDouble() / speed[it]
        }.apply { sort() }
        for(i in arrivalTimes.indices) {
            // check if arrivalTimeis less than or equal to index(minutes)
            if(arrivalTimes[i] <= i){
                return i // total monster eliminated before they reached city
            }
        }
        return dist.size
    }

    //Capacity To Ship Packages Within D Days (LeetCode 1011)
    // Binary search to find mid weight and check using hi and lo
    fun shipWithinDays(weights: IntArray, days: Int): Int {
        var lo = weights.maxOrNull() ?: 0
        var hi = weights.sum()
        var ans = hi // initial capacity

        while (lo <= hi) {
            val mid = lo + (hi - lo)/2
            if(canShip(weights, days, mid)) {
                hi = mid -1
                ans = mid // look for lower capacity
            } else {
                lo = mid +1 // look for higher capacity
            }
        }
        return ans
    }

    fun canShip(weights: IntArray, days: Int, capacity: Int) : Boolean {
        var currLoad = 0
        var neededDays = 1
        weights.forEach { w ->
            if(currLoad + w > capacity) {
                neededDays++
                currLoad = w
            } else {
                currLoad += w
            }
        }
        return neededDays <= days
    }

// ArrayDeque<Int> for both Stack: LIFO, and Queue: FIFO
}


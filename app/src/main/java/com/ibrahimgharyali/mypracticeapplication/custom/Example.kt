// https://app.codility.com/public-link/Wipro-Limited-Android-Developer-Senior/

class Solve {
    // Find Longest Word

    fun findTheLongestWord(s: String) : String {

        // Using kotlin idiomatic
//        return Regex("[A-Za-z0-9]+")
//            .splitToSequence(s)
//            .maxByOrNull{it.length}
//            .orEmpty()

        // using algorithm
        var longest  = ""
        for(word in s.split("[A-Za-z0-9]+")) {
            if(word.length > longest.length) longest = word
        }
        return longest
    }


    //Problem 2: Reverse Array In-Place
    fun reverseArray(chars: CharArray) : CharArray {

//        chars.reverse()
//        return chars

        var start = 0
        var end = chars.size -1
        while(start < end) {
            val temp = chars[start]
            chars[start] = chars[end]
            chars[end] = temp
            start ++
            end --
        }
        return chars
    }


    //two sorted integer arrays nums1 and nums2, merge them into a single sorted array.
    fun mergeSortedArrays(num1 : IntArray, num2: IntArray) : IntArray {
        // Final array should have a size num1+num2

        var k = 0; var i = 0; var j = 0
        val merged = IntArray(num1.size + num2.size -1)
        while (i < num1.size && j < num2.size) {
            merged[k++] = if(num1[i] < num2[j])  num1[i++] else num2[j++]
        }
        while(i < num1.size) merged[k++] = num1[i++]
        while(j < num2.size) merged[k++] = num1[j++]

        return merged

//        return (num1 + num2).apply { sort() }
    }

    // Problem 4: Card Pairing (Codility Logic Puzzle)
    fun cardPairing(rows: IntArray): Int {
        // need to find the sum of total cards required to make complete pairs
        var sum = 0
        for(i in rows) {
            sum += i % 2
        }
        return sum

//        return rows.sumOf { it % 2 }
    }

    // Two Sum (Hash Map & Array Search)
    fun findTargetSum(input : IntArray, target: Int) : IntArray {
        val hashMap = HashMap<Int, Int>()
        for(i in input.indices) {
            val remainder = target - input[i]
            if(hashMap.containsKey(remainder)) {
                return intArrayOf(hashMap[i]!!, i)
            }
            hashMap[input[i]] = i
        }
        return intArrayOf()
    }

    //Longest Substring Without Repeating Characters (Sliding Window)
    // a a b c b b e f g h
    fun longestSubstring(s: String) : Int {
        if(s.length <=1) return s.length
        var left = 0
        var maxCount = 0
        val hashSet = HashMap<Char, Int>()
        for (right in s.indices) {
            val c  = s[right]
            if(hashSet.containsKey(c)) {
                left = maxOf(left, hashSet[c]!!+1)
            }
            hashSet[c] = right
            maxCount = maxOf(right-left+1, maxCount)
        }
        return maxCount
    }

    // Merge Overlapping Intervals (Sorting & Array Processing)
    fun mergeOverlappingSet(intervals: Array<IntArray>) : Array<IntArray> {
        intervals.sortBy { it[0] }

        val finalArray = mutableListOf<IntArray>()
        for( arr in intervals) {
            if(finalArray.isEmpty() || finalArray.last()[1] < arr[0]) {
                finalArray.add(arr)
            } else {
                finalArray.last()[1] = maxOf(finalArray.last()[1], arr[1])
            }
        }
        return finalArray.toTypedArray()
    }

    //Sort Characters By Frequency (HashMap & Heap/Sorting)
    fun sortByFrequency(s: String) : String {
        if(s.length <= 2) return  s
        val map = HashMap<Char, Int>()
        for(c in s) {
            map[c] = map.getOrDefault(c, 0) + 1
        }
        // Sort entries by value descending
//        val sortedEntries = map.entries.sortedByDescending { it.value }
        val sortedEntries = map.entries.sortedWith(compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key.toString() })
        val finalString = StringBuilder()
        sortedEntries.forEach { (ch, i) ->
            finalString.append(ch.toString().repeat(i))
        }
        return finalString.toString()



    }

    fun sortByFrequencyKotlinWay(s: String) : String =
        // Using groupingBy kotlin idiomatic
//        var kotlinString = ""
        s.groupingBy { it }.eachCount()
            .entries.sortedByDescending { it.value }
//            .fold(""){ sb, i -> sb + i.key.toString().repeat(i.value) }
            .joinToString("") { (ch, i) ->
                ch.toString().repeat(i)
            }
//            .forEach { (ch, i) -> repeat(i) {kotlinString += ch} }
//        return kotlinString
//    }


    data class Student(val name: String, val score: Int)

    val students = listOf(
        Student("A", 10),
        Student("B", 20),
        Student("C", 15),
        Student("D", 25)
    )

    val totalScoreByFirstLetter = students
        .groupingBy { it.name.first() }
        .fold(0) { acc, student -> acc + student.score }





 }


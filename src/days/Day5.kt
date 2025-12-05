package days

import utils.execFileByLine

class Day5 {
    var newLineFound = false
    val inputRanges = mutableListOf<LongRange>()
    val ingredients = mutableListOf<Long>()
    fun solve() {

        execFileByLine(5) {
            if (!newLineFound) {
                if (it.isEmpty()) {
                    newLineFound = true
                } else {
                    val split = it.split("-")
                    inputRanges += split.first().toLong()..split.last().toLong()
                }
            } else {
                ingredients += it.toLong()
            }
        }
        val nonOverlappingRanges = inputRanges.mergeRanges()

        println(ingredients.count { nonOverlappingRanges.any { range -> it in range } })
        println(nonOverlappingRanges.sumOf { (it.last - it.first) + 1 })
    }

    private fun List<LongRange>.mergeRanges(): List<LongRange> {
        if (isEmpty()) return emptyList()

        val sortedRanges = sortedBy { it.first }
        val merged = mutableListOf<LongRange>()

        for (currentRange in sortedRanges) {
            if (merged.isEmpty() || currentRange.first > merged.last().last) {
                merged.add(currentRange)
            } else {
                val lastRange = merged.removeLast()
                val newEnd = maxOf(lastRange.last, currentRange.last)
                merged.add(lastRange.first..newEnd)
            }
        }
        return merged
    }
}
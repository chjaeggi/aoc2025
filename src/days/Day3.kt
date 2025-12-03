package days

import utils.execFileByLine

class Day3 {

    fun solve() {
        var maxPart1 = 0L
        var maxPart2 = 0L
        execFileByLine(3) {
            val joltages = it.toCharArray().map { it.digitToInt() }
            maxPart1 += joltages.findLargestBank(2)
            maxPart2 += joltages.findLargestBank(12)
        }
        println(maxPart1)
        println(maxPart2)
    }

    private fun List<Int>.findLargestBank(len: Int): Long {
        val result = ArrayList<Int>(len)
        var currentIndex = 0
        var remainingLength = len

        while (remainingLength > 0 && currentIndex <= size - remainingLength) {
            val sublist = subList(currentIndex, size - remainingLength + 1)
            val maxElement = sublist.maxOrNull() ?: break
            result.add(maxElement)
            currentIndex += sublist.indexOf(maxElement) + 1
            remainingLength--
        }
        return result.joinToString("").toLong()
    }
}
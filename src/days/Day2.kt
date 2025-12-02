package days

import utils.execFileByLine
import kotlin.math.sqrt

class Day2 {

    var result1 = 0L
    var result2 = 0L

    fun solve() {
        execFileByLine(2) {
            it.split(",").forEach {
                val (f, t) = it.split("-")
                val from = f.toLong()
                val to = t.toLong()
                for (number in from..to) {
                    val digitsAsString = number.toString()
                    if (digitsAsString.isSplitRepeat()) {
                        result1 += number
                    }
                    if (digitsAsString.isChunkRepeat()) {
                        result2 += number
                    }
                }
            }
        }
        println(result1)
        println(result2)
    }

    private fun String.isSplitRepeat(): Boolean {
        if (length % 2 != 0) return false
        return take(length / 2) == substring(length / 2, length)
    }

    private fun String.isChunkRepeat(): Boolean {
        val windowSizes = length.findDivisors()

        for (windowSize in windowSizes) {
            if (windowSize > length / 2) break

            val firstChunk = substring(0, windowSize)
            var isRepeating = true

            for (idxToCheck in windowSize until length step windowSize) {
                if (substring(idxToCheck, idxToCheck + windowSize) != firstChunk) {
                    isRepeating = false
                    break
                }
            }

            if (isRepeating) return true
        }
        return false
    }

    private fun Int.findDivisors(): List<Int> {
        val divisors = mutableSetOf<Int>()
        for (i in 1..sqrt(this.toDouble()).toInt()) {
            if (this % i == 0) {
                divisors.add(i)
                divisors.add(this / i)
            }
        }
        return divisors.sorted()
    }

}
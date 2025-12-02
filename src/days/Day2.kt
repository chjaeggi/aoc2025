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
                f1@ for (number in from..to) {
                    val digitsAsString = number.toString()
                    val digitCount = digitsAsString.length

                    if (digitCount % 2 == 0) {
                        var left = 0
                        var right = digitCount / 2
                        val digitArray = digitsAsString.map { it.digitToInt() }.toIntArray()

                        while (left < digitCount / 2) {
                            if (digitArray[left] == digitArray[right]) {
                                left++
                                right++
                            } else {
                                continue@f1
                            }
                        }
                        result1 += number
                    }
                }

                for (number in from..to) {
                    val digitsAsString = number.toString()
                    val digitCount = digitsAsString.length
                    val windowSizes = digitCount.findDivisors()

                    for (windowSize in windowSizes) {
                        if (windowSize > digitCount / 2) {
                            break
                        }

                        val firstChunk = digitsAsString.take(windowSize)
                        var isRepeating = true

                        for (idxToCheck in windowSize until digitCount step windowSize) {
                            val currentChunk =
                                digitsAsString.substring(idxToCheck, idxToCheck + windowSize)
                            if (currentChunk != firstChunk) {
                                isRepeating = false
                                break
                            }
                        }
                        if (isRepeating) {
                            result2 += number
                        }
                    }
                }
            }
        }
        println(result1)
        println(result2)
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
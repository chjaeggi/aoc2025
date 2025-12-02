package days

import utils.execFileByLine

class Day2 {

    var result1 = 0L
    var result2 = 0L

    fun solve() {
        execFileByLine(2) {
            it.split(",").map {
                val (from, to) = it.split("-").map { it.toLong() }
                result1 += (from..to).filter { it.toString().isSplitRepeat() }.sum()
                result2 += (from..to).filter { it.toString().isChunkRepeat() }.sum()
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
        for (chunkLength in 1..length / 2) {
            if (length % chunkLength == 0) {
                val firstChunk = this.substring(0, chunkLength)
                if (this == firstChunk.repeat(length / chunkLength)) {
                    return true
                }
            }
        }
        return false
    }
}
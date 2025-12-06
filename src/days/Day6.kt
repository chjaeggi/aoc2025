package days

import utils.execFileByLineInGroups
import utils.numberOfLinesPerFile

class Day6 {

    fun solve() {
        val nOfLines = numberOfLinesPerFile(6)
        val numbers = mutableListOf<Int>()
        val colNumbers = mutableListOf<List<Int>>()
        val operators = mutableListOf<Char>()

        execFileByLineInGroups(6, nOfLines - 1) {
            if (it.size > 1) {
                numbers += Regex("""\d+""").findAll(it.joinToString())
                    .map { match -> match.value.toInt() }.toList()
                colNumbers += it.getColNumbers()
            } else {
                operators += Regex("""[*+]""").findAll(it.joinToString())
                    .map { match -> match.value.first() }.toList()
            }
        }

        val numbersPerLine = numbers.size / (nOfLines - 1)
        val chunked = numbers.chunkedByStride(numbersPerLine)
        println(chunked.applyOperations(operators))
        println(colNumbers.applyOperations(operators))
    }

    private fun List<String>.getColNumbers(
    ): List<List<Int>> {
        val numbers = mutableListOf<List<Int>>()
        val colNums = mutableListOf<Int>()
        for (i in this[0].indices) {
            val numberString = (0..<this.size).joinToString("") { j -> this[j][i].toString() }
            if (numberString.isNotBlank()) {
                colNums.add(numberString.trim().toInt())
            } else {
                numbers += colNums.toList()
                colNums.clear()
            }
        }
        if (colNums.isNotEmpty()) numbers += colNums.toList()
        return numbers
    }

    fun List<Int>.chunkedByStride(stride: Int): List<List<Int>> {
        val groupedByColumn = this.withIndex().groupBy { it.index % stride }
        return (0 until stride).map { columnIndex ->
            groupedByColumn[columnIndex]?.map { it.value } ?: emptyList()
        }
    }

    private fun List<List<Int>>.applyOperations(operators: List<Char>): Long {
        var result = 0L
        for ((idx, chunk) in this.withIndex()) {
            result += when (operators[idx]) {
                '+' -> {
                    chunk.sumOf { it.toLong() }
                }

                '*' -> {
                    chunk.fold(1L) { acc, i -> acc * i }
                }

                else -> {
                    0L
                }
            }
        }
        return result
    }
}
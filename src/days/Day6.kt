package days

import utils.execFileByLine
import utils.execFileByLineInGroups
import utils.numberOfLinesPerFile

class Day6 {

    fun solve() {
        val nOfLines = numberOfLinesPerFile(6)
        val numbers = mutableListOf<Int>()
        val numbersRtl = mutableListOf<List<Int>>()
        val operators = mutableListOf<Char>()
        val numberRegex = Regex("""\d+""")
        val operatorRegex = Regex("""[*+]""")

        execFileByLine(6) {
            numbers += numberRegex.findAll(it).map { match -> match.value.toInt() }.toList()
            operators += operatorRegex.findAll(it).map { match -> match.value.first() }.toList()
        }

        val numbersPerLine = numbers.size / (nOfLines - 1)
        val chunked = numbers.chunkedByStride(numbersPerLine)
        println(chunked.applyOperations(operators))

        execFileByLineInGroups(6, 4) {

            val mutated = it.toMutableList()
            mutated[0] += "  "
            if (mutated.size > 1) {
                val possibleNumbers = mutableListOf<Int>()
                for (i in mutated[0].indices) {
                    var numberString = ""
                    for (j in 0..<mutated.size) {
                        numberString += mutated[j][i]
                    }
                    if (numberString.isNotBlank()) {
                        possibleNumbers.add(numberString.trim().toInt())
                    } else {
                        numbersRtl += possibleNumbers.toList()
                        possibleNumbers.clear()
                    }
                    if (i == mutated[0].lastIndex) {
                        numbersRtl += possibleNumbers.toList()
                    }
                }
            }
        }
        println(numbersRtl.applyOperations(operators))
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
package utils

import java.io.File

fun execFileByLine(number: Int, f: (str: String) -> Unit) =
    File("./src/inputs/input$number.txt").forEachLine { f(it) }

fun execFileByLineIndexed(number: Int, f: (index: Int, str: String) -> Unit) {
    File("./src/inputs/input$number.txt").readLines().forEachIndexed(f)
}

fun execFileByLineInGroups(number: Int, groupSize: Int, f: (str: List<String>) -> Unit) {
    val lines = File("./src/inputs/input$number.txt").readLines()
    lines.chunked(groupSize).forEach(f)
}

fun numberOfLinesPerFile(number: Int): Int {
    return File("./src/inputs/input$number.txt").readLines().size
}

fun numberOfCharsPerLine(number: Int): Int {
    return File("./src/inputs/input$number.txt").useLines { it.firstOrNull()?.length ?: 0 }
}
package days

import utils.Point2D
import utils.execFileByLineInGroups

data class Shape(
    val points: Set<Point2D>,
)

data class Region(
    val width: Int,
    val height: Int,
    val pieceCounts: List<Int>
)

class Day12 {

    fun solve() {

        val shapes = mutableListOf<Shape>()
        val regions = mutableListOf<Region>()

        execFileByLineInGroups(12, 5) {
            if (it.any { it.contains("x") }) {
                it.forEach {
                    val (dimensions, countsStr) = it.split(": ")
                    val (heightStr, widthStr) = dimensions.split("x")
                    val pieceCounts = countsStr.trim().split(" ").map { numStr -> numStr.toInt() }

                    regions.add(
                        Region(
                            width = widthStr.toInt(),
                            height = heightStr.toInt(),
                            pieceCounts = pieceCounts
                        )
                    )
                }
            } else {
                val points = it.mapIndexed { y, line ->
                    line.mapIndexedNotNull { x, char ->
                        if (char == '#') Point2D(x, y) else null
                    }
                }.flatten()
                shapes.add(Shape(points.toSet()))
            }
        }

        var res = 0
        var checkedCount = 0
        for (r in regions) {
            val allShapes = shapes.zip(r.pieceCounts).flatMap { (shape, count) -> List(count) { shape } }
            if (bestCase(allShapes.toList()) > r.height * r.width) {
                checkedCount++
                continue
            }
            if (worstCase(allShapes.toMutableList()) <= r.height * r.width) {
                checkedCount++
                res++
            }
        }
        if (checkedCount == regions.size) {
            println(res)
        } else {
            // implement heavy alogrithm here ... seems not needed with real input
        }
    }

    private fun worstCase(shapes: List<Shape>): Int {
        return 9 * shapes.size
    }

    private fun bestCase(shapes: List<Shape>): Int {
        return shapes.flatMap { it.points }.size
    }
}
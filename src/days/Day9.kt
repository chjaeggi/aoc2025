package days

import utils.Point2D
import utils.Rectangle
import utils.execFileByLine
import utils.overlaps


// THANKS TODD
class Day9 {

    fun solve() {
        val redTiles = mutableSetOf<Point2D>()

        execFileByLine(9) {
            val (x, y) = it.split(",").map { it.toInt() }
            redTiles.add(Point2D(x, y))
        }

        val rects = createRectangles(redTiles).sortedByDescending { it.area }
        println(rects.first().area)

        println(
            rects.first { r ->
                redTiles
                    .zipWithNext()
                    .flatMap { (left, right) -> createRectangles(setOf(left, right)) }
                    .none { line -> line.overlaps(r.inner()) }
            }.area
        )
    }


    private fun createRectangles(tiles: Set<Point2D>): Set<Rectangle> {
        val rects = mutableSetOf<Rectangle>()
        for (redTile in tiles) {
            for (otherTile in tiles) {
                if (redTile != otherTile) {
                    rects += Rectangle(redTile.x, otherTile.x, redTile.y, otherTile.y)
                }
            }
        }
        return rects
    }
}
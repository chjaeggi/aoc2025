package days

import utils.Direction
import utils.Point2D
import utils.execFileByLine
import utils.plus
import kotlin.math.abs

class Day9 {

    fun solve() {
        val redTiles = mutableSetOf<Point2D>()

        execFileByLine(9) {
            val (x, y) = it.split(",").map { it.toInt() }
            redTiles.add(Point2D(x, y))
        }
        println(simpleAreaCalc(redTiles))
        val outline = generateOutline(redTiles)
        val flooded = floodFill(outline)
        println(expensiveAreaCalc(redTiles, flooded))
    }

    private fun createFloodedRect(p1: Point2D, p2: Point2D): Set<Point2D> {
        val rectPoints = mutableSetOf<Point2D>()
        rectPoints.add(p1)
        rectPoints.add(Point2D(p1.x, p2.y))
        rectPoints.add(p2)
        rectPoints.add(Point2D(p2.x, p1.y))
        rectPoints.add(p1)

        val outline = generateOutline(rectPoints)
        return floodFill(outline)
    }

    private fun floodFill(outline: Set<Point2D>): Set<Point2D> {
        val first = outline.take(1)
        if (outline.all { it.x == first.first().x || it.y == first.first().y }) {
            return outline
        }
        
        val minY = outline.minBy { it.y }
        var floodFillStart: Point2D?

        if (outline.contains(Point2D(minY.x + 1, minY.y))) {
            floodFillStart = Point2D(minY.x + 1, minY.y + 1)
        } else {
            floodFillStart = Point2D(minY.x - 1, minY.y + 1)
        }
        val tiles = ArrayDeque<Point2D>()
        val flooded = outline.toMutableSet()
        flooded.add(floodFillStart)
        tiles.add(floodFillStart)

        while (tiles.isNotEmpty()) {
            val tile = tiles.removeFirst()
            for (dir in Direction.entries) {
                val neighbor = tile + dir
                if (neighbor !in flooded) {
                    flooded += neighbor
                    tiles.add(neighbor)
                }
            }
        }
        return flooded
    }

    private fun expensiveAreaCalc(redTiles: Set<Point2D>, flooded: Set<Point2D>): Long {
        var maxArea = 0L
        for (redTile in redTiles) {
            for (otherTile in redTiles) {
                if (redTile != otherTile) {
                    val rect = createFloodedRect(redTile, otherTile)
                    if (flooded.containsAll(rect)) {
                        if (rect.size.toLong() > maxArea) {
                            maxArea = rect.size.toLong()
                        }
                    }
                }
            }
        }
        return maxArea
    }

    private fun simpleAreaCalc(redTiles: Set<Point2D>): Long {
        var maxArea = 0L
        for (redTile in redTiles) {
            for (otherTile in redTiles) {
                if (redTile != otherTile) {
                    val dx = abs(redTile.x - otherTile.x).toLong()
                    val dy = abs(redTile.y - otherTile.y).toLong()
                    val area = ((dx + 1) * (dy + 1))
                    if (area > maxArea) {
                        maxArea = area
                    }
                }
            }
        }
        return maxArea
    }

    private fun generateOutline(
        redTiles: Set<Point2D>,
    ) : Set<Point2D> {
        val outline = mutableSetOf<Point2D>()

        redTiles.zipWithNext { first, second ->
            if (first.x == second.x) {
                val d = abs(first.y - second.y)
                if (first.y < second.y) {
                    // south
                    for (i in 0..d) {
                        outline.add(Point2D(first.x, first.y + i))
                    }
                } else {
                    // north
                    for (i in 0..d) {
                        outline.add(Point2D(first.x, first.y - i))
                    }
                }
            } else {
                val d = abs(first.x - second.x)
                if (first.x > second.x) {
                    // west
                    for (i in 0..d) {
                        outline.add(Point2D(first.x - i, first.y))
                    }
                } else {
                    // east
                    for (i in 0..d) {
                        outline.add(Point2D(first.x + i, first.y))
                    }
                }
            }
        }

        // wrap-around
        listOf(redTiles.last(), redTiles.first()).zipWithNext { first, second ->
            if (first.x == second.x) {
                val d = abs(first.y - second.y)
                if (first.y < second.y) {
                    // south
                    for (i in 0..d) {
                        outline.add(Point2D(first.x, first.y + i))
                    }
                } else {
                    // north
                    for (i in 0..d) {
                        outline.add(Point2D(first.x, first.y - i))
                    }
                }
            } else {
                val d = abs(first.x - second.x)
                if (first.x > second.x) {
                    // west
                    for (i in 0..d) {
                        outline.add(Point2D(first.x - i, first.y))
                    }
                } else {
                    // east
                    for (i in 0..d) {
                        outline.add(Point2D(first.x + i, first.y))
                    }
                }
            }
        }
        return outline
    }
}
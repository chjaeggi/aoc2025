package days

import utils.*

class Day7 {

    val manifold = Array(numberOfLinesPerFile(7)) { CharArray(numberOfCharsPerLine(7)) }
    var start: Point2D? = null

    fun solve() {
        execFileByLineIndexed(7) { y, line ->
            line.forEachIndexed { x, v ->
                manifold[y][x] = v
                if (v == 'S') {
                    start = Point2D(x, y)
                }
            }
        }
        println(runBeam(start!!))
        println(nOfPaths(start!!))
    }
    
    
    private fun runBeam(pos: Point2D, visited: MutableSet<Point2D> = mutableSetOf()): Int {
        if (!manifold.inBounds(pos.s) || !visited.add(pos.s)) {
            return 0
        }

        return when (manifold.at(pos.s)) {
            '.' -> runBeam(pos.s, visited)
            '^' -> 1 + runBeam(pos.sw, visited) + runBeam(pos.se, visited)
            else -> 0
        }
    }

    private fun nOfPaths(pos: Point2D, cache: MutableMap<Point2D, Long> = mutableMapOf()): Long {
        return cache.getOrPut(pos.s) {
            when (manifold.atOrNull(pos.s)) {
                null -> 1L
                '.' -> nOfPaths(pos.s, cache)
                '^' -> nOfPaths(pos.sw, cache) + nOfPaths(pos.se, cache)
                else -> throw IllegalStateException("Should never reach this")
            }
        }
    }
}
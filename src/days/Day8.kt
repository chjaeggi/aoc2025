package days

import utils.DisjointSet
import utils.Point3D
import utils.distanceTo
import utils.execFileByLine

private data class Edge(val from: Point3D, val to: Point3D, val distance: Double)

class Day8 {

    fun solve() {
        val boxes = mutableListOf<Point3D>()
        execFileByLine(8) {
            val (x, y, z) = it.split(",").map { it.toInt() }
            boxes.add(Point3D(x, y, z))
        }

        val edges = mutableListOf<Edge>()
        for (i in boxes.indices) {
            for (j in (i + 1) until boxes.size) {
                val from = boxes[i]
                val to = boxes[j]
                edges.add(Edge(from, to, from.distanceTo(to)))
            }
        }

        edges.sortBy { it.distance }

        val dsu = DisjointSet<Point3D>()
        boxes.forEach { dsu.makeSet(it) }

        edges.take(1000).forEach { edge ->
            dsu.union(edge.from, edge.to)
        }

        val rootCounts = mutableMapOf<Point3D, Int>()
        for (element in dsu.elements) {
            val root = dsu.find(element)
            rootCounts[root] = (rootCounts[root] ?: 0) + 1
        }
        println(
            rootCounts.values.toList()
                .sortedDescending()
                .take(3)
                .fold(1L) { acc, size -> acc * size }
        )

        val dsu2 = DisjointSet<Point3D>()
        boxes.forEach { dsu2.makeSet(it) }

        for (edge in edges) {
            if (dsu2.union(edge.from, edge.to)) {
                if (dsu2.setCount == 1) {
                    println("${edge.from.x.toLong() * edge.to.x.toLong()}")
                    break
                }
            }
        }
    }
}
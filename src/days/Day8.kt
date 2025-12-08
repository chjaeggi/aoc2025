package days

import utils.*

class Day8 {

    fun solve() {
        val boxes = mutableSetOf<Point3D>()

        execFileByLine(8) {
            val (x, y, z) = it.split(",").map { it.toInt() }
            boxes.add(Point3D(x, y, z))
        }
        
    }
    
}
package days

import utils.execFileByLine

class Day1 {

    var currentPosition = 50
    val positions = mutableListOf(currentPosition)
    var zeroPass = 0
    fun solve() {
        execFileByLine(1) {
            val isLeft = it.contains("L")
            var rotateBy = it.removePrefix(if (isLeft) "L" else "R").toInt()

            zeroPass += rotateBy / 100
            rotateBy %= 100

            val rotateTo = if (isLeft) currentPosition - rotateBy else currentPosition + rotateBy

            if (rotateTo == 0 || rotateTo == 100) {
                zeroPass++
            } else if (rotateTo !in 0..100 && positions.last() != 0) {
                zeroPass++
            }

            currentPosition = rotateTo
            if (currentPosition < 0) {
                currentPosition += 100
            }
            currentPosition %= 100

            positions.add(currentPosition)
        }
        println(positions.count { it == 0 })
        println(zeroPass)
    }
}
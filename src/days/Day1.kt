package days

import utils.execFileByLine

class Day1 {

    var currentPosition = 50
    val positions = mutableListOf(currentPosition)
    var hitZero = 0
    fun solve() {
        execFileByLine(1) {
            val isLeft = it.contains("L")
            var rotateBy = it.removePrefix(if (isLeft) "L" else "R").toInt()

            hitZero += rotateBy / 100
            rotateBy %= 100

            val newPositionBeforeModulo = if (isLeft) currentPosition - rotateBy else currentPosition + rotateBy

            if (newPositionBeforeModulo == 0 || newPositionBeforeModulo == 100) {
                hitZero++
            } else if (newPositionBeforeModulo < 0 && positions.last() != 0) {
                hitZero++
            } else if (newPositionBeforeModulo > 100 && positions.last() != 0) {
                hitZero++
            }

            currentPosition = newPositionBeforeModulo
            if (currentPosition < 0) {
                currentPosition += 100
            }
            currentPosition %= 100

            positions.add(currentPosition)
        }
        println(positions.count { it == 0 })
        println(hitZero)
    }
}
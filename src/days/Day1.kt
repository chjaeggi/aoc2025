package days

import utils.execFileByLine

class Day1 {

    var currentPosition = 50
    var lastPosition = 50
    var zeroHit = 0
    var zeroCrossing = 0
    fun solve() {
        execFileByLine(1) {
            val isLeft = it.contains("L")
            var rotateBy = it.removePrefix(if (isLeft) "L" else "R").toInt()

            zeroCrossing += rotateBy / 100
            rotateBy %= 100

            val rotateTo = if (isLeft) currentPosition - rotateBy else currentPosition + rotateBy

            if (rotateTo == 0 || rotateTo == 100) {
                zeroCrossing++
            } else if (rotateTo !in 0..100 && lastPosition != 0) {
                zeroCrossing++
            }

            currentPosition = rotateTo
            if (currentPosition < 0) {
                currentPosition += 100
            }
            currentPosition %= 100

            lastPosition = currentPosition
            if (lastPosition == 0) zeroHit++
        }

        println(zeroHit)
        println(zeroCrossing)
    }
}
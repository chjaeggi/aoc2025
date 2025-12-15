package utils

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.text.compareTo

data class Point2D(
    val x: Int,
    val y: Int,
) {
    val n get() = this + Direction.N
    val e get() = this + Direction.E
    val s get() = this + Direction.S
    val w get() = this + Direction.W
    val ne get() = this + Direction.NE
    val se get() = this + Direction.SE
    val sw get() = this + Direction.SW
    val nw get() = this + Direction.NW
}

class Rectangle(
    point1: Point2D,
    point2: Point2D
) {
    val xStart: Int = min(point1.x, point2.x)
    val xEnd: Int = max(point1.x, point2.x)
    val yStart: Int = min(point1.y, point2.y)
    val yEnd: Int = max(point1.y, point2.y)

    val area: Long
        get() {
            return (xEnd - xStart + 1L) * (yEnd - yStart + 1L)
        }

    fun inner(): Rectangle =
        Rectangle(
            Point2D(xStart + 1, yStart + 1),
            Point2D(xEnd - 1, yEnd - 1),
        )

    override fun equals(other: Any?): Boolean {
        val other = other as? Rectangle ?: return false
        return this.xStart == other.xStart && this.xEnd == other.xEnd && this.yStart == other.yStart && this.yEnd == other.yEnd
    }

    override fun hashCode(): Int {
        var result = xStart
        result = 31 * result + xEnd
        result = 31 * result + yStart
        result = 31 * result + yEnd
        result = 31 * result + area.hashCode()
        return result
    }
}

fun Rectangle.overlaps(other: Rectangle): Boolean {
    return this.xStart <= other.xEnd && this.xEnd >= other.xStart &&
            this.yStart <= other.yEnd && this.yEnd >= other.yStart
}

data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int,
)

fun Point3D.distanceTo(to: Point3D): Double {
    val xd = (to.x.toDouble() - x.toDouble()).pow(2)
    val yd = (to.y.toDouble() - y.toDouble()).pow(2)
    val zd = (to.z.toDouble() - z.toDouble()).pow(2)
    return sqrt(xd + yd + zd)
}

fun Point2D.distanceTo(to: Point2D): Double {
    val xd = (to.x.toDouble() - x.toDouble()).pow(2)
    val yd = (to.y.toDouble() - y.toDouble()).pow(2)
    return sqrt(xd + yd)
}

data class Point2DWithDirection(
    val p: Point2D,
    val d: Direction, // signifies the direction in which the point was entered e.g. Direction.N == from Direction.S below the coordinates
)

enum class Direction {
    N, E, S, W, NE, SE, SW, NW
}

operator fun Point2D.plus(direction: Direction): Point2D {
    return when (direction) {
        Direction.N -> Point2D(x, y - 1)
        Direction.E -> Point2D(x + 1, y)
        Direction.S -> Point2D(x, y + 1)
        Direction.W -> Point2D(x - 1, y)
        Direction.NE -> Point2D(x + 1, y - 1)
        Direction.SE -> Point2D(x + 1, y + 1)
        Direction.SW -> Point2D(x - 1, y + 1)
        Direction.NW -> Point2D(x - 1, y - 1)
    }
}

operator fun Point2D.minus(direction: Direction): Point2D {
    return when (direction) {
        Direction.N -> this + Direction.S
        Direction.E -> this + Direction.W
        Direction.S -> this + Direction.N
        Direction.W -> this + Direction.E
        Direction.NE -> this + Direction.SW
        Direction.SE -> this + Direction.NW
        Direction.SW -> this + Direction.NE
        Direction.NW -> this + Direction.SE
    }
}

fun Direction.turnCW90(): Direction {
    return when (this) {
        Direction.N -> Direction.E
        Direction.E -> Direction.S
        Direction.S -> Direction.W
        Direction.W -> Direction.N
        Direction.NE -> Direction.SE
        Direction.SE -> Direction.SW
        Direction.SW -> Direction.NW
        Direction.NW -> Direction.NE
    }
}

fun Direction.turnCCW90(): Direction {
    return this.turnCW90().turnCW90().turnCW90()
}

operator fun Point2D.plus(other: Point2D): Point2D = Point2D(other.x + x, other.y + y)
operator fun Point2D.minus(other: Point2D): Point2D = Point2D(x - other.x, y - other.y)

inline fun <reified T> Array<T>.inBounds(p: Point2D): Boolean {
    return when (T::class) {
        CharArray::class -> p.y in indices && p.x in (this[0] as CharArray).indices
        IntArray::class -> p.y in indices && p.x in (this[0] as IntArray).indices
        else -> throw IllegalArgumentException("Unsupported type: ${T::class}")
    }
}

fun Array<CharArray>.at(p: Point2D) = this[p.y][p.x]
fun Array<CharArray>.atOrNull(p: Point2D) = if (inBounds(p)) this[p.y][p.x] else null
fun Array<IntArray>.at(p: Point2D) = this[p.y][p.x]
fun Array<IntArray>.atOrNull(p: Point2D) = if (inBounds(p)) this[p.y][p.x] else null
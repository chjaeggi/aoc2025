import days.*
import kotlin.time.measureTime

fun main() {
    val overallTime = measureTime {
        Day1().solve()
        Day2().solve()
        Day3().solve()
        Day4().solve()
        Day5().solve()
        Day6().solve()
        Day7().solve()
        Day8().solve()
        Day9().solve()
        Day10().solve()
        Day11().solve()
        Day12().solve()
    }
    
    println("----")
    println("Executed in: $overallTime")

}
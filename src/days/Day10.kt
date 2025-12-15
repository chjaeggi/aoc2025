package days

import utils.execFileByLine
import utils.powerSet
import java.util.BitSet
import kotlin.math.pow

private const val IMPOSSIBLE = 100_000_000

private data class MachinePuzzle(
    val startupTargetMask: Int,
    val buttons: List<List<Int>>,
    val joltages: List<Int>
)

// THANKS CLOUDDJR :D 
class Day10 {

    fun solve() {
        var res1 = 0
        var res2 = 0
        execFileByLine(10) {
            val puzzle = parseLine(it)
            val startupButtonMasks = puzzle.buttons.map { button ->
                button.sumOf { bitIndex -> 2.0.pow(bitIndex).toInt() }
            }
            res1 += solveStartup(puzzle.startupTargetMask, startupButtonMasks)
            val solutionsByParity = precomputeParitySolutions(puzzle.buttons)
            res2 += solveJoltages(
                puzzle.joltages,
                puzzle.buttons,
                solutionsByParity
            )
        }
        println("Res: $res1")
        println("Res: $res2")
    }

    private fun parseLine(line: String): MachinePuzzle {
        val patternString = line.substringAfter("[").substringBefore("]")
        val buttonsString = line.substringAfter("]").substringBefore("{")
        val joltageString = line.substringAfter("{").substringBefore("}")

        val startupTargetMask = patternString.replace(".", "0").replace("#", "1").reversed().toInt(2)
        val buttons = buttonsString.trim().split(" ").map { buttonStr ->
            buttonStr.trim('(', ')').split(",").map { it.toInt() }
        }
        val joltages = joltageString.split(",").map { it.toInt() }

        return MachinePuzzle(startupTargetMask, buttons, joltages)
    }

    private fun solveStartup(target: Int, buttonMasks: List<Int>): Int {
        if (target == 0) return 0
        val queue = ArrayDeque<Pair<Int, Int>>()
        val visited = mutableSetOf<Int>()
        queue.add(0 to 0)
        visited.add(0)

        while (queue.isNotEmpty()) {
            val (currentPattern, currentPushes) = queue.removeFirst()
            for (b in buttonMasks) {
                val nextPattern = currentPattern xor b
                if (nextPattern == target) {
                    return currentPushes + 1
                }
                if (nextPattern !in visited) {
                    visited.add(nextPattern)
                    queue.add(nextPattern to currentPushes + 1)
                }
            }
        }
        return -1 // Should not happen
    }

    private fun precomputeParitySolutions(
        buttons: List<List<Int>>
    ): Map<BitSet, List<List<List<Int>>>> {
        val solutionsByParity = mutableMapOf<BitSet, MutableList<List<List<Int>>>>()
        val buttonCombinations = buttons.powerSet()

        for (combination in buttonCombinations) {
            val resultingParityMask = BitSet()
            for (button in combination) {
                for (bitIndex in button) {
                    resultingParityMask.flip(bitIndex)
                }
            }
            solutionsByParity.getOrPut(resultingParityMask) { mutableListOf() }.add(combination)
        }
        return solutionsByParity
    }

    /**
     * Recursively calculates the minimum button presses to achieve target joltages, using memoization.
     *
     * This function solves the problem by breaking it down based on parity (even/odd presses).
     * For a given `joltages` target, it finds all button combinations (`candidates`) that satisfy
     * the target's parity. For each candidate, it calculates the total cost via a recursive call:
     *
     * `cost = (candidate_size) + 2 * (cost_for_remaining_joltages / 2)`
     *
     * The cost of the subproblem is doubled because each press in the subproblem corresponds to a
     * pair of presses in the current problem, which is necessary to maintain the correct parity.
     *
     * @param target The list of target joltage counts for the current state.
     * @param buttons The list of all available buttons for the machine.
     * @param solutionsByParity A pre-computed map from a parity `BitSet` to all button combinations that produce it.
     * @param cache A map for memoization to store the results of previously computed subproblems.
     * @return The minimum number of button presses required to reach the target joltages.
     */
    private fun solveJoltages(
        target: List<Int>,
        buttons: List<List<Int>>,
        solutionsByParity: Map<BitSet, List<List<List<Int>>>>,
        cache: MutableMap<List<Int>, Int> = mutableMapOf()
    ): Int {
        return when {
            target.all { it == 0 } -> 0
            target.any { it < 0 } -> IMPOSSIBLE
            else -> cache.getOrPut(target) {
                val bitmask = BitSet().apply {
                    target.forEachIndexed { i, j -> set(i, j % 2 == 1) }
                }

                val candidates = solutionsByParity[bitmask] ?: return@getOrPut IMPOSSIBLE

                candidates.minOf { buttonCombination ->
                    val newJoltages = IntArray(target.size) { target[it] }
                    buttonCombination.flatten().forEach { newJoltages[it]-- }
                    val subproblemCost =
                        solveJoltages(
                            newJoltages.map { it / 2 },
                            buttons,
                            solutionsByParity,
                            cache
                        )
                    if (subproblemCost >= IMPOSSIBLE) IMPOSSIBLE else buttonCombination.size + (2 * subproblemCost)
                }
            }
        }
    }
}

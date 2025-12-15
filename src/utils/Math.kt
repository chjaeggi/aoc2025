package utils

import kotlin.math.pow

fun <T> permutationsForElements(operators: List<T>, length: Int): List<List<T>> {
    if (length == 1) return operators.map { listOf(it) }
    return permutationsForElements(operators, length - 1).flatMap { prefix ->
        operators.map { operator -> prefix + operator }
    }
}

fun <T> List<T>.combinations(size: Int): List<List<T>> {
    if (size == 0) return listOf(emptyList())
    if (size > this.size) return emptyList()
    if (size == this.size) return listOf(this)

    val result = mutableListOf<List<T>>()
    val remaining = this.drop(1)

    result.addAll(remaining.combinations(size - 1).map { listOf(this.first()) + it })
    result.addAll(remaining.combinations(size))

    return result
}

fun <T> List<T>.permutations(): List<List<T>> {
    if (this.size <= 1) return listOf(this)

    val result = mutableListOf<List<T>>()
    for (i in this.indices) {
        val remaining = this.toMutableList().apply { removeAt(i) }
        for (perm in remaining.permutations()) {
            result.add(listOf(this[i]) + perm)
        }
    }
    return result
}

/**
 * Calculates the power set of a list. The power set is the set of all possible subsets.
 * For example, the power set of [A, B] is [[], [A], [B], [A, B]].
 *
 * This is achieved by iterating from 0 to 2^n - 1, where n is the size of the list.
 * Each number in this range (the 'state') represents a unique subset. The binary
 * representation of the number is used as a mask to determine which elements to include.
 *
 * @return A list containing all possible sub-lists (subsets) of the original list.
 */
fun <T> List<T>.powerSet(): List<List<T>> {
    val numSubsets = 2.0.pow(size).toInt()
    return (0 until numSubsets).map { state ->
        this.filterIndexed { index, _ ->
            (state / 2.0.pow(index).toInt()) % 2 == 1
        }
    }
}
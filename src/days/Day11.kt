package days

import utils.*

/**
 * Application-specific aliases using the generic Graph utility.
 */
typealias Network = Graph<Device>
typealias Connection = Edge<Device>
data class Device(val name: String)

fun Network.countPathsWithVias(
    current: Device,
    end: Device,
    vias: Set<Device>,
    cache: MutableMap<Pair<Device, Set<Device>>, Long>
): Long {
    val cacheKey = Pair(current, vias)
    cache[cacheKey]?.let { return it }

    val remainingVias = vias - current

    if (current == end) {
        return if (remainingVias.isEmpty()) 1L else 0L
    }

    val connections = this[current]
    var totalPaths = 0L

    if (connections != null) {
        for (connection in connections) {
            totalPaths += countPathsWithVias(connection.to, end, remainingVias, cache)
        }
    }

    cache[cacheKey] = totalPaths
    return totalPaths
}

class Day11 {

    fun solve() {
        val network: Network = mutableMapOf()

        execFileByLine(11) {
            val from = it.split(":").first()
            val to = it.split(": ").last().split(" ")
            to.forEach {
                network.add(EdgeType.Directed, Connection(Device(from), Device(it)))
            }
        }

        val allFromYouToOut = network.countPathsWithVias(
            current = Device("you"),
            end = Device("out"),
            vias = emptySet(),
            cache = mutableMapOf()
        )

        val countFromSvrToOut = network.countPathsWithVias(
            current = Device("svr"),
            end = Device("out"),
            vias = setOf(Device("dac"), Device("fft")),
            cache = mutableMapOf()
        )

        println(allFromYouToOut)
        println(countFromSvrToOut)
    }

}
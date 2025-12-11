package days

import utils.execFileByLine

typealias Network = MutableMap<Device, List<Connection>>

data class Device(val name: String)
data class Connection(val from: Device, val to: Device)

enum class EdgeType {
    Directed, Undirected
}

fun Connection.invert() = copy(from = to, to = from)

fun MutableMap<Device, List<Connection>>.add(type: EdgeType, connection: Connection) {
    val fromRoutes = this[connection.from] ?: emptyList()
    this[connection.from] = fromRoutes + connection

    if (type == EdgeType.Undirected) {
        val toRoutes = this[connection.to] ?: emptyList()
        this[connection.to] = toRoutes + connection.invert()
    }
}

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
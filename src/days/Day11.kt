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

fun findPathsRecursive(
    network: Network,
    currentNode: Device,
    endNode: Device,
    vias: List<Device>,
    currentPath: ArrayDeque<Device>,
    allPathsFound: MutableList<List<Device>>
) {
    currentPath.addLast(currentNode)

    if (currentNode == endNode && vias.all { it in currentPath }) {
        allPathsFound.add(currentPath.toList())
    } else {
        network[currentNode]?.forEach { connection ->
            val neighbor = connection.to
            if (neighbor !in currentPath) {
                findPathsRecursive(network, neighbor, endNode, vias, currentPath, allPathsFound)
            }
        }
    }
    currentPath.removeLast()
}

fun Network.findAllPaths(start: Device, end: Device, vias: List<Device>): List<List<Device>> {
    val allPathsFound = mutableListOf<List<Device>>()
    val currentPath = ArrayDeque<Device>()
    findPathsRecursive(this, start, end, vias, currentPath, allPathsFound)
    return allPathsFound
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

        val allFromYouToOut = network.findAllPaths(
            Device("you"), Device("out"), emptyList()
        )
//        val allFromSvrViaDacViaFftToOut = network.findAllPaths(
//            Device("svr"), Device("out"), listOf(
//                Device("dac"), Device("fft")
//            )
//        )
        println(allFromYouToOut.size)
    }

}
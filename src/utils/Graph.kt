package utils


typealias Graph<T> = MutableMap<T, List<Edge<T>>>

data class Edge<T>(val from: T, val to: T)

enum class EdgeType {
    Directed, Undirected
}

fun <T> Edge<T>.invert(): Edge<T> = copy(from = to, to = from)

fun <T> Graph<T>.add(type: EdgeType, edge: Edge<T>) {
    this[edge.from] = (this[edge.from] ?: emptyList()) + edge

    if (type == EdgeType.Undirected) {
        this[edge.to] = (this[edge.to] ?: emptyList()) + edge.invert()
    }
}

// Basic Disjoint Set (Union Find) implementation
// With the help of Gemini
class DisjointSet<T> {
    private val parent = mutableMapOf<T, T>()
    private val size = mutableMapOf<T, Int>()
    val elements: Set<T> get() = parent.keys

    var setCount = 0
        private set

    fun makeSet(element: T) {
        parent[element] = element
        size[element] = 1
        setCount++
    }

    fun find(element: T): T {
        if (parent[element] == element) return element
        parent[element] = find(parent[element]!!)
        return parent[element]!!
    }

    fun union(a: T, b: T): Boolean {
        val rootA = find(a)
        val rootB = find(b)
        if (rootA != rootB) {
            if (size[rootA]!! < size[rootB]!!) {
                parent[rootA] = rootB
                size[rootB] = size[rootB]!! + size[rootA]!!
            } else {
                parent[rootB] = rootA
                size[rootA] = size[rootA]!! + size[rootB]!!
            }
            setCount--
            return true
        }
        return false
    }
}
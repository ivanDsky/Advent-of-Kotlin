import kotlin.math.max

fun main() {
    val digitRegex = Regex("""\d+""")
    fun String.mapToLongList(): List<Long> = digitRegex.findAll(this).map { it.value.toLong() }.toList()
    fun String.mapToIntList(): List<Int> = digitRegex.findAll(this).map { it.value.toInt() }.toList()
    fun mapToLongList(input: List<String>): List<List<Long>> = input.map { line -> line.mapToLongList() }
    fun mapToIntList(input: List<String>): List<List<Int>> = input.map { line -> line.mapToIntList() }
    fun mapToCharList(input: List<String>): List<List<Char>> = input.map { it.toList() }

    val MOD = 16_777_216;

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )


    fun String.toNode(): Int = (this[0].code - 'a'.code) * 26 + (this[1].code - 'a'.code)
    fun Int.toStringNode(): String = "${(this / 26 + 'a'.code).toChar()}${(this % 26 + 'a'.code).toChar()}"
    data class Connection(val from: String, val to: String) {
        override fun toString(): String {
            return "($from-$to)"
        }
    }

    val graph: List<MutableList<Boolean>> = List(30 * 30) { MutableList(30 * 30) { false } }

    fun part1(rawInput: List<String>): Long {

        val connections = rawInput.map { line ->
            val (from, to) = line.split("-")
            Connection(from, to)
        }

        for (connection in connections) {
            graph[connection.from.toNode()][connection.to.toNode()] = true
            graph[connection.to.toNode()][connection.from.toNode()] = true
        }

        val ans = mutableSetOf<Triple<Int, Int, Int>>()
        var cnt = 0L
        for (i in graph.indices) {
            for (j in graph[i].indices) {
                if (!graph[i][j]) continue
                for (k in graph[j].indices) {
                    if (!graph[j][k]) continue
                    if (!graph[i][k]) continue
                    ans.add(Triple(i, j, k))
                    if (i.toStringNode()[0] == 't' || j.toStringNode()[0] == 't' || k.toStringNode()[0] == 't') {
                        cnt++
                    }
                }
            }
        }
//            for (j in graph[i]!!) {
//                for (k in graph[j]!!) {
//                    if (k !in graph[i]!!) continue
//                    ans.add(Triple(i, j, k))
//                }
//            }
//        }
//        for (i in 0 until 30 * 30) {
//            for (j in i until 30 * 30) {
//                if (!graph[i][j]) continue
//                for (k in j until 30 * 30) {
//                    if (!graph[j][k]) continue
//                    if (!graph[i][k]) continue
//                    ans.add(Triple(i, j, k))
//                    if (i.toStringNode()[0] == 't' || j.toStringNode()[0] == 't' || k.toStringNode()[0] == 't') {
//                        cnt++
////                        println("${i.toStringNode()} ${j.toStringNode()} ${k.toStringNode()}")
//                    }
//                }
//            }
//        }
//        cnt = ans
//            .map { Triple(it.first.toStringNode(), it.second.toStringNode(), it.third.toStringNode()) }
//            .count { it.first[0] == 't' || it.second[0] == 't' || it.third[0] == 't' }.toLong()

        return cnt / 6
    }

    fun part1Long(rawInput: List<String>): Long {

        val connections = rawInput.map { line ->
            val (from, to) = line.split("-")
            Connection(from, to)
        }

        val graph = mutableMapOf<String, Set<String>>()
        for (connection in connections) {
            graph[connection.from] = (graph[connection.from] ?: emptySet()) + connection.to
            graph[connection.to] = (graph[connection.to] ?: emptySet()) + connection.from
        }

        val ans = mutableSetOf<Triple<String, String, String>>()
        for (i in graph.keys) {
            for (j in graph[i]!!) {
                for (k in graph[j]!!) {
                    if (k !in graph[i]!!) continue
                    ans.add(Triple(i, j, k))
                }
            }
        }
        return ans
            .count { it.first[0] == 't' || it.second[0] == 't' || it.third[0] == 't' }
            .toLong() / 6
    }


    val graphS : MutableMap<String, Set<String>> = mutableMapOf()

    val path: MutableSet<String> = mutableSetOf()
    var bestAns: List<String> = emptyList()
    fun dfs(node: String, available: Set<String>) {
        if (path.size > bestAns.size) {
            bestAns = path.toList()
        }
        val newAvailable = available intersect graphS[node]!!
        for (next in newAvailable) {
            if (next in path) continue
            path.add(next)
            dfs(next, newAvailable)
            path.remove(next)
        }
    }

    val path2: MutableSet<Int> = mutableSetOf()
    var bestAns2: List<Int> = emptyList()
    fun dfs2(node: Int, available: Set<Int>) {
        if (path.size > bestAns.size) {
            bestAns2 = path2.toList()
        }
        val newAvailable = available.filter { graph[node][it] }.toSet()
        for (next in newAvailable) {
            if (next in path2) continue
            path2.add(next)
            dfs2(next, newAvailable)
            path2.remove(next)
        }
    }

    fun part2(rawInput: List<String>): String {
        graphS.clear()
        val connections = rawInput.map { line ->
            val (from, to) = line.split("-")
            Connection(from, to)
        }

        for (connection in connections) {
            graph[connection.from.toNode()][connection.to.toNode()] = true
            graph[connection.to.toNode()][connection.from.toNode()] = true

            graphS[connection.from] = (graphS[connection.from] ?: emptySet()) + connection.to
            graphS[connection.to] = (graphS[connection.to] ?: emptySet()) + connection.from
        }

        val order = graphS.keys.sortedByDescending { graphS[it]!!.size }
        for (node in order) {
            path.add(node)
            dfs(node, graphS[node]!!)
            path.remove(node)
            bestAns.println()
        }
        return bestAns.sorted().joinToString(",")
    }

    // Or read a large test input from the `src/Day23_test.txt` file:
    val testInput = readInput("Day23_test")

    // Read the input from the `src/Day23.txt` file.
    val input = readInput("Day23")
    part1Long(testInput).println()
    part2(testInput).println()
    part1Long(input).println()
    part2(input).println()
}
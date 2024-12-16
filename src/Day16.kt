import jdk.internal.org.jline.utils.Colors.s
import java.util.PriorityQueue

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> = input.map { it.split(" ").map { it.toInt()} }

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    data class State(val pos: Vec2, val cost: Int, val dir: Direction): Comparable<State> {
        override fun compareTo(other: State): Int = cost.compareTo(other.cost)
    }

    fun part1(input: List<List<Char>>): Long {
        val sX = input.indexOfFirst { it.contains('S') }
        val sY = input[sX].indexOf('S')
        val s = Vec2(sX, sY)
        val queue = PriorityQueue<State>()
        val cache = mutableMapOf<Pair<Vec2, Direction>, Int>()
        for(d in Direction.entries) {
            queue.add(State(s, if (d == Direction.RIGHT) 0 else 1000, d))
        }
        var ans = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val (pos, cost, dir) = queue.poll()
            if (input[pos] == 'E') ans = cost
            if (cost > ans) break
            if (cost >= (cache[pos to dir] ?: Int.MAX_VALUE)) continue
            cache[pos to dir] = cost
            for (nextDir in Direction.entries) {
                val nextPos = pos + deltas[nextDir]!!
                if (nextPos notInBounds input) continue
                if (input[nextPos] == '#') continue
                queue += State(nextPos, cost + 1 + if (nextDir != dir) 1000 else 0, nextDir)
            }
        }
        return 0L
    }

    val positions = mutableSetOf<Vec2>()
    lateinit var inp: List<List<Char>>
    lateinit var cache: MutableMap<Pair<Vec2, Direction>, Int>

    fun dfs(state: State) {
        positions += state.pos
        for (d in Direction.entries) {
            val nextPos = state.pos + deltas[state.dir.opposite]!!
            if (nextPos notInBounds(inp)) continue
            if (inp[nextPos] == '#') continue
            val prevCost = state.cost - 1 - if (d != state.dir) 1000 else 0
            if (cache[nextPos to d] != prevCost) continue
            dfs(State(nextPos, prevCost, d))
        }
    }

    fun part2(input: List<List<Char>>): Long {
        positions.clear()
        val sX = input.indexOfFirst { it.contains('S') }
        val sY = input[sX].indexOf('S')
        val s = Vec2(sX, sY)
        val queue = PriorityQueue<State>()
        inp = input
        cache = mutableMapOf()
        for(d in Direction.entries) {
            queue.add(State(s, if (d == Direction.RIGHT) 0 else 1000, d))
        }
        var ans = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val (pos, cost, dir) = queue.poll()
            if (input[pos] == 'E') ans = cost
            if (cost > ans) break
            if (cost >= (cache[pos to dir] ?: Int.MAX_VALUE)) continue
            cache[pos to dir] = cost
            for (nextDir in Direction.entries) {
                val nextPos = pos + deltas[nextDir]!!
                if (nextPos notInBounds input) continue
                if (input[nextPos] == '#') continue
                queue += State(nextPos, cost + 1 + if (nextDir != dir) 1000 else 0, nextDir)
            }
        }

        val eX = input.indexOfFirst { it.contains('E') }
        val eY = input[eX].indexOf('E')
        val e = Vec2(eX, eY)
        for(d in Direction.entries) {
            if ((cache[e to d] ?: Int.MAX_VALUE) == ans) {
                dfs(State(e, ans, d))
            }
        }

        return positions.size.toLong()
    }

    // Or read a large test input from the `src/Day16_test.txt` file:
    val testInput = readInput("Day16_test")

    // Read the input from the `src/Day16.txt` file.
    val input = readInput("Day16")
    part1(testInput.map { it.toList() }).println()
    part2(testInput.map { it.toList() }).println()
    part1(input.map { it.toList() }).println()
    part2(input.map { it.toList() }).println()
}

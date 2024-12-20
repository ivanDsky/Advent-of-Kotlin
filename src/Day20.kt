import kotlin.math.absoluteValue

fun main() {
    val digitRegex = Regex("""\d+""")
    fun String.mapToLongList(): List<Long> = digitRegex.findAll(this).map { it.value.toLong() }.toList()
    fun String.mapToIntList(): List<Int> = digitRegex.findAll(this).map { it.value.toInt() }.toList()
    fun mapToLongList(input: List<String>): List<List<Long>> = input.map { line -> line.mapToLongList() }
    fun mapToIntList(input: List<String>): List<List<Int>> = input.map { line -> line.mapToIntList() }
    fun mapToCharList(input: List<String>): List<List<Char>> = input.map { it.toList() }

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    fun dist(input: List<List<Char>>, pos: Vec2): List<List<Int>> {
        val n = input.size
        val m = input[0].size
        val dist = List(n) { MutableList(m) { Int.MAX_VALUE / 4 } }
        val q = ArrayDeque<Vec2>()
        q.addLast(pos)
        dist[pos.i][pos.j] = 0
        while (q.isNotEmpty()) {
            val cur = q.removeFirst()
            for (d in Direction.entries) {
                val next = cur + deltas[d]!!
                if (next inBounds input && input[next] != '#' && dist[next] > dist[cur] + 1) {
                    dist[next.i][next.j] = dist[cur.i][cur.j] + 1
                    q.addLast(next)
                }
            }
        }
        return dist
    }

    fun part1(rawInput: List<String>): Long {
        val input = mapToCharList(rawInput)
        val si = rawInput.indexOfFirst { it.contains('S') }
        val sj = rawInput[si].indexOf('S')
        val start = Vec2(si, sj)
        val ei = rawInput.indexOfFirst { it.contains('E') }
        val ej = rawInput[ei].indexOf('E')
        val end = Vec2(ei, ej)

        val distStart = dist(input, start)
        val distEnd = dist(input, end)

        val time = distStart[end]

        val ans = mutableSetOf<Pair<Vec2, Vec2>>()
        val timeNeeded = 100
        for (i in input.indices) {
            for (j in input[i].indices) {
                for (d1 in Direction.entries) {
                    for (d2 in Direction.entries) {
                        val next1 = Vec2(i, j) + deltas[d1]!!
                        val next2 = next1 + deltas[d2]!!
                        if (next1 notInBounds input || next2 notInBounds input) continue
                        if (distStart[i][j] + distEnd[next2] + 2 + timeNeeded <= time) ans += (next1 to next2)
                    }
                }
            }
        }

        return ans.size.toLong()
    }

    fun part2(rawInput: List<String>): Long {
        val input = mapToCharList(rawInput)
        val si = rawInput.indexOfFirst { it.contains('S') }
        val sj = rawInput[si].indexOf('S')
        val start = Vec2(si, sj)
        val ei = rawInput.indexOfFirst { it.contains('E') }
        val ej = rawInput[ei].indexOf('E')
        val end = Vec2(ei, ej)

        val distStart = dist(input, start)
        val distEnd = dist(input, end)

        val time = distStart[end]

        val ans = mutableSetOf<Pair<Vec2, Vec2>>()
        val timeNeeded = 100
        for (i1 in input.indices) {
            for (j1 in input[i1].indices) {
                for (i2 in input.indices) {
                    for (j2 in input[i2].indices) {
                        val del = (i1 - i2).absoluteValue + (j1 - j2).absoluteValue
                        if (del <= 20) {
                            if (distStart[i1][j1] + distEnd[i2][j2] + del + timeNeeded <= time) ans += (Vec2(i1,j1) to Vec2(i2,j2))
                        }
                    }
                }
            }
        }

        return ans.size.toLong()
    }

    // Or read a large test input from the `src/Day20_test.txt` file:
    val testInput = readInput("Day20_test")

    // Read the input from the `src/Day20.txt` file.
    val input = readInput("Day20")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

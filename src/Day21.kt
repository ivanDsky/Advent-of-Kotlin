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

    val grid: List<List<Char>> = listOf(
        listOf('7', '8', '9'),
        listOf('4', '5', '6'),
        listOf('1', '2', '3'),
        listOf('#', '0', 'A')
    )

    val grid2: List<List<Char>> = listOf(
        listOf('#', '^', 'A'),
        listOf('<', 'v', '>'),
    )

    fun getPos(x: Char, grid: List<List<Char>>): Vec2 {
        val i = grid.indexOfFirst { x in it }
        val j = grid[i].indexOf(x)
        return Vec2(i, j)
    }
    /*
        var start = getPos('A', grid)
        val res = StringBuilder()
        for (x in input) {
            val next = getPos(x, grid)

            while (start != next) {
                val delta = next - start

                when {
                    delta.x > 0 && grid[start.i + delta.i][start.j] != '#' -> {
                        res.append('v'); start += Vec2(1, 0)
                    }
                    delta.y < 0 && grid[start.i][start.j + delta.j] != '#' -> {
                        res.append('<'); start += Vec2(0, -1)
                    }
                    delta.y > 0 && grid[start.i][start.j + delta.j] != '#' -> {
                        res.append('>'); start += Vec2(0, 1)
                    }
                    delta.x < 0 && grid[start.i + delta.i][start.j] != '#' -> {
                        res.append('^'); start += Vec2(-1, 0)
                    }
                }
            }

            res.append('A')
        }
        return res.toString()
     */

    fun solveOrigin(input: String, grid: List<List<Char>>): String {
        var start = getPos('A', grid)
        val res = StringBuilder()
        for (x in input) {
            val next = getPos(x, grid)

            while (start != next) {
                val delta = next - start

                when {
                    grid[start.i + delta.i][start.j] != '#' && delta.x > 0 -> {
                        res.append('v'); start += Vec2(1, 0)
                    }
                    grid[start.i][start.j + delta.j] != '#' && delta.y < 0 -> {
                        repeat(-delta.j) { res.append('<') }; start += Vec2(0, delta.j)
                    }
                    grid[start.i][start.j + delta.j] != '#' && delta.y > 0 -> {
                        res.append('>'); start += Vec2(0, 1)
                    }
                    grid[start.i + delta.i][start.j] != '#' && delta.x < 0 -> {
                        repeat(-delta.i) { res.append('^') }; start += Vec2(delta.i, 0)
                    }
                }
            }

            res.append('A')
        }
        return res.toString()
    }

    fun solve(input: String): String {
        val res = solveOrigin(input, grid)

        val res2 = solveOrigin(res, grid2)

        val res3 = solveOrigin(res2, grid2)
        return res3
    }

    data class CacheKey(val from: Vec2, val to: Vec2, val depth: Int)
    val cache: MutableMap<CacheKey, Long> = mutableMapOf()

    fun Direction.toChar(): Char = when (this) {
        Direction.UP -> '^'
        Direction.DOWN -> 'v'
        Direction.LEFT -> '<'
        Direction.RIGHT -> '>'
    }

    fun Char.toDirection(): Direction = when (this) {
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        else -> error("Invalid direction")
    }

    val paths: MutableMap<Pair<Vec2, Vec2>, List<String>> = mutableMapOf()

    fun allPaths(from: Vec2, to: Vec2, grid: List<List<Char>>, isVisited: Set<Vec2>): List<String> {
        if (from == to) return listOf("A")
        val res = mutableListOf<String>()
        for (dir in Direction.entries) {
            val next = from + deltas[dir]!!
            if (next notInBounds grid || grid[next] == '#' || next in isVisited) continue
            val allNextPaths = allPaths(next, to, grid, isVisited + from).map { dir.toChar() + it }
            res += allNextPaths
        }
        return res
    }

    fun initPaths(grid: List<List<Char>>) {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                for (i2 in grid.indices) {
                    for (j2 in grid[i2].indices) {
                        if (grid[i][j] == '#' || grid[i2][j2] == '#') continue
                        paths[Vec2(i, j) to Vec2(i2, j2)] = allPaths(Vec2(i, j), Vec2(i2, j2), grid, emptySet())
                    }
                }
            }
        }
    }

    fun findBestPath(from: Vec2, to: Vec2, depth: Int): Long {
        if (from == to) return 1
        if (depth == 0) return 1
        val key = CacheKey(from, to, depth)
        if (key in cache) return cache[key]!!
        val allPaths = paths[from to to]!!
        var best = Long.MAX_VALUE
        for (path in allPaths) {
            var cost = 0L
            var start = getPos('A', grid2)
            for (dir in path) {
                val next = getPos(dir, grid2)
                cost += findBestPath(start, next, depth - 1)
                start = next
            }
            best = minOf(best, cost)
        }
        cache[key] = best
        return best
    }

    fun findBestAnswer(input: String, depth: Int): Long {
        initPaths(grid2)
        cache.clear()
        var res = 0L
        var current = getPos('A', grid)
        for (x in input) {
            val next = getPos(x, grid)
            val allPaths = allPaths(current, next, grid, emptySet())
            var best = Long.MAX_VALUE
            for (path in allPaths) {
                var cost = 0L
                var start = getPos('A', grid2)
                for (dir in path) {
                    val to = getPos(dir, grid2)
                    cost += findBestPath(start, to, depth)
                    start = to
                }
                if (cost < best) {
                    best = cost
                }
            }
            res += best
            current = next
        }
        return res
    }

    fun part1(rawInput: List<String>): Long {
        val res = rawInput.map { it to solve(it) }
        var ans = 0L;
        for ((input, output) in res) {
            val inp = input.take(3).toLong()
            ans += inp * output.length
            (inp to output.length).println()
        }
        return ans
    }

    fun part2(rawInput: List<String>): Long {
        val depth = 25
        val res = rawInput.map { it to findBestAnswer(it, depth) }
        var ans = 0L
        for ((input, output) in res) {
            val inp = input.take(3).toLong()
            ans += inp * output
            (inp to output).println()
        }
        return ans
    }

    /*
    204109833660
    (82050061710, 029A)
    (72242026390, 980A)
    (81251039228, 179A)
    (80786362258, 456A)
    (77985628636, 379A)
     */

    // Or read a large test input from the `src/Day21_test.txt` file:
    val testInput = readInput("Day21_test")

    // Read the input from the `src/Day21.txt` file.
    val input = readInput("Day21")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

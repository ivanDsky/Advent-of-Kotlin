import year2023.Direction

fun main() {
    val deltas = listOf(
       Pair(-1, 0),
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1)
    )

    lateinit var grid: List<MutableList<Boolean>>

    fun parseInput(input: List<String>): List<MutableList<Boolean>> {
        return input.map { line ->
            line.map { it == '#' }.toMutableList()
        }
    }

    val cnt = mutableSetOf<Pair<Int, Int>>()

    fun dfs(posI: Int, posJ: Int, direction: Direction) {
        var (curI, curJ) = posI to posJ
        while (true) {
            cnt += curI to curJ
            curI += deltas[direction.ordinal].first
            curJ += deltas[direction.ordinal].second
            if (curI !in grid.indices || curJ !in grid[0].indices) return
            if (grid[curI][curJ]) break
        }
        dfs(curI - deltas[direction.ordinal].first, curJ - deltas[direction.ordinal].second, Direction.entries[(direction.ordinal + 1) % Direction.entries.size])
    }


    val visited = mutableSetOf<Triple<Int, Int, Direction>>()
    fun dfs2(posI: Int, posJ: Int, direction: Direction): Boolean {
        var (curI, curJ) = posI to posJ
        while (true) {
            if (Triple(curI, curJ, direction) in visited) return true
            visited += Triple(curI, curJ, direction)
            curI += deltas[direction.ordinal].first
            curJ += deltas[direction.ordinal].second
            if (curI !in grid.indices || curJ !in grid[0].indices) return false
            if (grid[curI][curJ]) break
        }
        return dfs2(curI - deltas[direction.ordinal].first, curJ - deltas[direction.ordinal].second, Direction.entries[(direction.ordinal + 1) % Direction.entries.size])
    }

    fun part1(input: List<String>): Int {
        cnt.clear()
        grid = parseInput(input)
        val startI = input.indexOfFirst { it.contains('^') }
        val startJ = input[startI].indexOf('^')
        dfs(startI, startJ, Direction.UP)
        return cnt.size
    }

    fun part2(input: List<String>): Int {
        grid = parseInput(input)
        val startI = input.indexOfFirst { it.contains('^') }
        val startJ = input[startI].indexOf('^')

        var cnt = 0

        for (i in grid.indices) {
            for (j in 0 until grid[0].size) {
                visited.clear()
                if (!grid[i][j]) {
                    grid[i][j] = true
                    if (dfs2(startI, startJ, Direction.UP)) cnt++
                    grid[i][j] = false
                }
            }
        }
        return cnt
    }

    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day06_test")

//    check(part1(testInput) == 18)
//    check(part2(testInput) == 9)

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

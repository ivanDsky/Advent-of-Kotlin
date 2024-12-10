fun main() {
    fun parseInput(input: List<String>): List<List<Int>> = input.map { it.map { it - '0' } }

    val dist: MutableMap<Vec2, Int> = mutableMapOf()

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    var grid: List<List<Int>> = listOf()

    fun dfs(pos: Vec2) {
        for ((_, delta) in deltas) {
            val newPos = pos + delta
            if (newPos notInBounds grid) continue
            if (grid[newPos] == grid[pos] + 1) {
                dist[newPos] = (dist[newPos] ?: 0) + 1
                dfs(newPos)
            }
        }
    }

    fun part1(input: List<String>): Long {
        grid = parseInput(input)
        var res = 0
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                if (grid[i][j] == 0) {
                    dist.clear()
                    dist[Vec2(i, j)] = 1

                    dfs(Vec2(i, j))

                    for (i in grid.indices) {
                        for (j in grid[0].indices) {
                            if (grid[i][j] == 9) {
                                res += if ((dist[Vec2(i, j)] ?: 0) > 0) 1 else 0
                            }
                        }
                    }
                }
            }
        }


        return res.toLong()
    }


    fun part2(input: List<String>): Long {
        grid = parseInput(input)
        var res = 0
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                if (grid[i][j] == 0) {
                    dist.clear()
                    dist[Vec2(i, j)] = 1

                    dfs(Vec2(i, j))

                    for (i in grid.indices) {
                        for (j in grid[0].indices) {
                            if (grid[i][j] == 9) {
                                res += dist[Vec2(i, j)] ?: 0
                            }
                        }
                    }
                }
            }
        }


        return res.toLong()
    }

    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test")

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> = input.map { it.split(" ").map { it.toInt()} }

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.UP to Vec2(-1, 0),
        Direction.LEFT to Vec2(0, -1),
        Direction.DOWN to Vec2(1, 0),
    )
    val deltaPair = listOf(
        Pair(Vec2(1, 0), Vec2(0, 1)),
        Pair(Vec2(0, 1), Vec2(-1, 0)),
        Pair(Vec2(-1, 0), Vec2(0, -1)),
        Pair(Vec2(0, -1), Vec2(1, 0)),
    )
    lateinit var gardens: List<List<Char>>
    lateinit var garden: List<MutableList<Int>>

    var cnt = 0
    fun dfs(oldPos: Vec2): Long {
        garden[oldPos] = cnt
        var area = 1L
        for ((_, delta) in deltas) {
            val pos = oldPos + delta
            if (pos notInBounds gardens) continue
            if (garden[pos] == -1 && gardens[pos] == gardens[oldPos]) {
                area += dfs(pos)
            }
        }
        return area
    }

    fun part1(input: List<List<Char>>): Long {
        var ans = 0L
        gardens = input
        garden = MutableList(input.size) { MutableList(input[0].size) { -1 } }
        val area: MutableMap<Int, Long> = mutableMapOf()

        for (i in input.indices) {
            for (j in input[0].indices) {
                if (garden[i][j] == -1) {
                    cnt += 1
                    area[cnt] = dfs(Vec2(i, j))
                }
            }
        }

        for (i in input.indices) {
            for (j in input[0].indices) {
                for ((_, delta) in deltas) {
                    val pos = Vec2(i, j) + delta
                    if (pos notInBounds input){
                        ans += area[garden[Vec2(i, j)]]!!
                        continue
                    }
                    if (input[pos] != input[i][j]) ans += area[garden[pos]]!!
                }
            }
        }
        return ans
    }

    fun part2(input: List<List<Char>>): Long {
        var ans = 0L
        gardens = input
        garden = MutableList(input.size) { MutableList(input[0].size) { -1 } }
        val area: MutableMap<Int, Long> = mutableMapOf()

        for (i in input.indices) {
            for (j in input[0].indices) {
                if (garden[i][j] == -1) {
                    cnt += 1
                    area[cnt] = dfs(Vec2(i, j))
                }
            }
        }

        for (i in input.indices) {
            for (j in input[0].indices) {
                for ((diffD, sameD) in deltaPair) {
                    val diff = Vec2(i, j) + diffD
                    val same = Vec2(i, j) + sameD
                    val prevDiff = Vec2(i, j) + sameD + diffD

                    val isDiff = diff notInBounds input || input[diff] != input[i][j]
                    val isSame = same inBounds input && input[same] == input[i][j]
                    val isPrevDiff = prevDiff notInBounds input || input[prevDiff] != input[i][j]
                    if (isDiff && (!isSame || !isPrevDiff)) ans += area[garden[Vec2(i, j)]]!!
                }
            }
        }
        return ans
    }

    // Or read a large test input from the `src/Day12_test.txt` file:
    val testInput = readInput("Day12_test")

    // Read the input from the `src/Day12.txt` file.
    val input = readInput("Day12")
    part1(testInput.map { it.toList() }).println()
    part2(testInput.map { it.toList() }).println()
    part1(input.map { it.toList() }).println()
    part2(input.map { it.toList() }).println()
}

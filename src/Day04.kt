fun main() {

    val directionsXMAS = List(9) { packedMul ->
        val mulI = packedMul / 3 - 1
        val mulJ = packedMul % 3 - 1
        (0..3).map { mulI * it to mulJ * it }
    }



    val diagonals = listOf(-1 to -1, -1 to 1, 1 to 1, 1 to -1)

    val directionsX_MAS = List(diagonals.size){ shift ->
        diagonals.drop(shift) + diagonals.take(shift) + listOf(0 to 0)
    }

    fun List<String>.isXMAS(i: Int, j: Int, direction: List<Pair<Int, Int>>, pattern: String): Boolean {
        for (index in direction.indices) {
            val ni = i + direction[index].first
            val nj = j + direction[index].second
            if (ni !in indices || nj !in this[ni].indices) return false
            if (pattern[index] != this[ni][nj]) return false
        }
        return true
    }

    fun solve(input: List<String>, directions: List<List<Pair<Int, Int>>>, pattern: String): Int {
        var result = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                for (direction in directions) {
                    if (input.isXMAS(i, j, direction, pattern)) result++
                }
            }
        }
        return result
    }

    fun part1(input: List<String>): Int = solve(input, directionsXMAS, "XMAS")

    fun part2(input: List<String>): Int = solve(input, directionsX_MAS, "MMSSA")

    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day04_test")

    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

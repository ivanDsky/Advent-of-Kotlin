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

    lateinit var patterns: List<String>

    fun check(design: String): Long {
        val dp = MutableList(design.length + 1) { 0L }
        dp[0] = 1
        for (i in design.indices) {
            for (pattern in patterns) {
                if (i - pattern.length + 1 >= 0 && pattern == design.substring(i - pattern.length + 1, i + 1)) {
                    dp[i + 1] += dp[i - pattern.length + 1]
                }
            }
        }
        return dp[design.length]
    }

    fun part1(input: List<String>): Long {
        patterns = input[0].split(", ")
        val designs = input.drop(2)
        return designs.count { check(it) > 0 }.toLong()
    }

    fun part2(input: List<String>): Long {
        patterns = input[0].split(", ")
        val designs = input.drop(2)
        return designs.sumOf { check(it) }
    }

    // Or read a large test input from the `src/Day19_test.txt` file:
    val testInput = readInput("Day19_test")

    // Read the input from the `src/Day19.txt` file.
    val input = readInput("Day19")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

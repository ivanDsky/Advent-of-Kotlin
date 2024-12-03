fun main() {
    fun parseInput(input: String): List<List<Long>> {
        val regex = Regex("""mul\(\d{1,3},\d{1,3}\)""")
        val digit = Regex("""\d{1,3}""")
        return regex.findAll(input).map { digit.findAll(it.value).map { it.value.toLong() }.toList() }.toList()
    }

    fun parseInput2(input: String): List<List<Long>> {
        val doDont = Regex("""do\(\).*?don't\(\)""")

        return doDont.findAll(input).map { parseInput(it.value) }.flatten().toList()
    }

    fun part1(input: String): Long {
        val res = parseInput(input)
        return res.sumOf { (a, b) -> a * b }
    }

    fun part2(input: String): Long {
        val res = parseInput2(input)
        return res.sumOf { (a, b) -> a * b }
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInput("Day03_test").first()

    check(part1(testInput) == 161L)
    check(part2(testInput) == 48L)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03").joinToString("")
    part1(input).println()
    part2(input).println()
}

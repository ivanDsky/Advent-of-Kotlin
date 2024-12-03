fun main() {
    fun String.readDigits(): List<Pair<Long, Long>> {
        val regex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        return regex.findAll(this).map { result ->
            val (firstNumber, secondNumber) = result.groupValues.drop(1).map { it.toLong() }
            firstNumber to secondNumber
        }.toList()
    }

    fun part1(input: String): Long {
        val digits = input.readDigits()
        return digits.sumOf { (a, b) -> a * b }
    }


    fun String.readAllowedDigits(): List<Pair<Long, Long>> {
        val modifiedInput = "do()${this}don't()"
        val doDont = Regex("""do\(\).*?don't\(\)""")

        return doDont
            .findAll(modifiedInput)
            .map { it.value.readDigits() }
            .flatten()
            .toList()
    }

    fun part2(input: String): Long {
        val digits = input.readAllowedDigits()
        return digits.sumOf { (a, b) -> a * b }
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readRawInput("Day03_test")

    check(part1(testInput) == 161L)
    check(part2(testInput) == 48L)

    // Read the input from the `src/Day03.txt` file.
    val input = readRawInput("Day03")
    part1(input).println()
    part2(input).println()
}

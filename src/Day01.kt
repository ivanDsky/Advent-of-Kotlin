import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
        return input.map {
            val (firstNumber, secondNumber) = it
                .split(' ')
                .filter { number -> number.isNotBlank() }
            firstNumber.toInt() to secondNumber.toInt()
        }.unzip()
    }

    fun part1(input: List<String>): Int {
        val (firstList, secondList) = parseInput(input)

        val sortedFirstList = firstList.sorted()
        val sortedSecondList = secondList.sorted()

        val answer = sortedFirstList.indices.sumOf { index ->
            abs(sortedFirstList[index] - sortedSecondList[index])
        }

        return answer
    }

    fun part2(input: List<String>): Int {
        val (firstList, secondList) = parseInput(input)

        val secondListCounter = secondList.groupingBy { it }.eachCount()

        val answer = firstList.sumOf { it * (secondListCounter[it] ?: 0) }

        return answer
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

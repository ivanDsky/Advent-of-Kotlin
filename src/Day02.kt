import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.split(' ').map { it.toInt() }
        }
    }

    fun List<Int>.isLevelSafe(): Boolean {
        if (size <= 1) return true
        var prev = this[0]
        val sign = (this[1] - this[0]).sign

        if (sign == 0) return false

        for (level in drop(1)) {
            if (abs(level - prev) !in 1..3) return false
            if ((level - prev).sign != sign) return false
            prev = level
        }

        return true
    }

    fun part1(input: List<String>): Int {
        val levels = parseInput(input)

        val answer = levels.count { it.isLevelSafe() }

        return answer
    }

    fun List<Int>.isDumpedLevelSafe(): Boolean {
        return indices.any { i ->
            val copy = this.toMutableList()
            copy.removeAt(i)
            copy.isLevelSafe()
        }
    }

    fun part2(input: List<String>): Int {
        val levels = parseInput(input)

        val answer = levels.count { it.isDumpedLevelSafe() }

        return answer
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

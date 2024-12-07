
fun main() {

    fun parseInput(input: List<String>): List<Pair<Long, List<Long>>> {
        return input.map { line ->
            val (res, rawValues) = line.split(": ")
            val values = rawValues.split(" ").map { it.toLong() }
            res.toLong() to values
        }
    }

    fun solve1(values: List<Long>): Set<Long> {
        if (values.size == 1) return setOf(values.first())
        val current = values.last()
        val previous = solve1(values.dropLast(1))

        return previous.map { it + current }.toSet() + previous.map { it * current }.toSet()
    }

    fun solve2(values: List<Long>): Set<Long> {
        if (values.size == 1) return setOf(values.first())
        val current = values.last()
        val previous = solve2(values.dropLast(1))

        return previous.map { it + current }.toSet() +
                previous.map { it * current }.toSet() +
                previous.map { "$it$current".toLong() }.toSet()
    }

    fun part1(input: List<String>): Long {
        val samples = parseInput(input)
        return samples.sumOf { (res, values) ->
            if (res in solve1(values)) res else 0
        }
    }

    fun part2(input: List<String>): Long {
        val samples = parseInput(input)
        return samples.sumOf { (res, values) ->
            if (res in solve2(values)) res else 0
        }
    }

    // Or read a large test input from the `src/Day07_test.txt` file:
    val testInput = readInput("Day07_test")

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

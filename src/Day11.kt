fun main() {
    fun parseInput(input: String): List<Long> = input.split(" ").map { it.toLong()}

    val ans: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()
    fun process(number: Long, steps: Int): Long {
        if (steps == 0) {
            ans[(number to 0)] = 1
            return 1
        }
        if (ans.containsKey(number to steps)) {
            return ans[(number to steps)]!!
        }
        if (number == 0L) {
            return process(1, steps - 1).also { ans[(number to steps)] = it }
        }
        val str = number.toString()
        if (str.length % 2 == 0) {
            val a = str.substring(0, str.length / 2)
            val b = str.substring(str.length / 2, str.length)
            val aAns = process(a.toLong(), steps - 1).also { ans[(a.toLong() to steps - 1)] = it }
            val bAns = process(b.toLong(), steps - 1).also { ans[(b.toLong() to steps - 1)] = it }
            return aAns + bAns
        }
        return process(number * 2024, steps - 1).also { ans[(number * 2024 to steps - 1)] = it }
    }

    fun part1(input: String): Long {
        ans.clear()
        return parseInput(input).sumOf { process(it, 25) }
    }


    fun part2(input: String): Long {
        ans.clear()
        return parseInput(input).sumOf { process(it, 75) }
    }

    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readRawInput("Day11_test")

    // Read the input from the `src/Day11.txt` file.
    val input = readRawInput("Day11")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

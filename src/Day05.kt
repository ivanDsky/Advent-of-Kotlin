import java.util.random.RandomGeneratorFactory.all

fun main() {

    fun readRules(input: List<String>): Map<Int, List<Int>> {
        val result = mutableMapOf<Int, List<Int>>()
        input.forEach {
            val (x,y) = it.split("|").map { it.toInt() }
            result[x] = (result[x] ?: emptyList()) + y
        }
        return result
    }

    fun solve2(input: String, rules: Map<Int, List<Int>>): Int {
        val pages = input.split(",").map { it.toInt() }
        val sortedPages = pages.sortedBy {
            pages.size - (rules[it]?.count { pages.contains(it) } ?: 0)
        }

        return sortedPages[sortedPages.size / 2]
    }

    fun solve(input: String, rules: Map<Int, List<Int>>): Int {
        val pages = input.split(",").map { it.toInt() }
        val positions = pages.withIndex().associate { it.value to it.index }

        val isValid = pages.all { page ->
            val rulesForPage = rules[page] ?: emptyList()
            rulesForPage.filter { positions.containsKey(it) }.all { positions[page]!! < positions[it]!! }
        }

        return if (isValid) 0 else solve2(input, rules)
    }




    fun part1(input: List<String>): Int {
        val rules = input.takeWhile { it != "" }.run { readRules(this) }
        return input.takeLastWhile { it != "" }.sumOf { solve(it, rules) }

    }

    fun part2(input: List<String>): Int = TODO()

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInput("Day05_test")

//    check(part1(testInput) == 18)
//    check(part2(testInput) == 9)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(testInput).println()
//    part2(testInput).println()
    part1(input).println()
//    part2(input).println()
}

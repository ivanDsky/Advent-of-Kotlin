
fun main() {

    fun parseInput(input: List<String>): MutableMap<Char, MutableList<Pair<Int, Int>>> {
        val locations: MutableMap<Char, MutableList<Pair<Int, Int>>> = mutableMapOf()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] != '.') {
                    locations[input[i][j]] = ((locations[input[i][j]] ?: mutableListOf()) + (i to j)).toMutableList()
                    continue
                }
            }
        }
        return locations
    }

    fun part1(input: List<String>): Long {
        val locations = parseInput(input)
        val result = List(input.size) { MutableList(input[0].length) { '.' } }
        for((_, location) in locations) {
            for (i in location.indices) {
                for (j in location.indices) {
                    if (i == j) continue
                    val delI = location[i].first - location[j].first
                    val delJ = location[i].second - location[j].second
                    val nI = location[i].first + delI
                    val nJ = location[i].second + delJ
                    if (nI in result.indices && nJ in result[0].indices) {
                        result[nI][nJ] = '#'
                    }

                }
            }
        }

        return result.flatten().count { it == '#' }.toLong()
    }

    fun part2(input: List<String>): Long {
        val locations = parseInput(input)
        val result = List(input.size) { MutableList(input[0].length) { '.' } }
        for((_, location) in locations) {
            for (i in location.indices) {
                for (j in location.indices) {
                    if (i == j) continue
                    val delI = location[i].first - location[j].first
                    val delJ = location[i].second - location[j].second
                    var cnt = 0
                    while (true) {
                        val nI = location[i].first + delI * cnt
                        val nJ = location[i].second + delJ * cnt
                        if (nI in result.indices && nJ in result[0].indices) {
                            result[nI][nJ] = '#'
                        } else {
                            break
                        }
                        cnt++
                    }

                }
            }
        }
        return result.flatten().count { it == '#' }.toLong()
    }

    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

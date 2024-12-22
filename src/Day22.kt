import kotlin.math.max

fun main() {
    val digitRegex = Regex("""\d+""")
    fun String.mapToLongList(): List<Long> = digitRegex.findAll(this).map { it.value.toLong() }.toList()
    fun String.mapToIntList(): List<Int> = digitRegex.findAll(this).map { it.value.toInt() }.toList()
    fun mapToLongList(input: List<String>): List<List<Long>> = input.map { line -> line.mapToLongList() }
    fun mapToIntList(input: List<String>): List<List<Int>> = input.map { line -> line.mapToIntList() }
    fun mapToCharList(input: List<String>): List<List<Char>> = input.map { it.toList() }

    val MOD = 16_777_216;

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    fun Long.nextSecret(): Long {
        var step1 = (this * 64L)
        step1 = step1 xor this.toLong()
        step1 %= MOD
        var step2 = step1 / 32
        step2 = step2 xor step1
        step2 %= MOD
        var step3 = step2 * 2048L
        step3 = step3 xor step2
        step3 %= MOD
        return step3
    }

    fun part1(rawInput: List<String>): Long {
        var input = mapToLongList(rawInput).map { it.first() }

        repeat(2000) {
            input = input.map { it.nextSecret() }
        }
        input.println()

        return input.sum()
    }

    fun List<Pair<Long, Long>>.hash(): Pair<Long, Long> {
        var hash = 0L
        for (change in this) {
            hash = change.first + hash * 97
        }
        return hash to this.last().second
    }

    fun part2(rawInput: List<String>): Long {
        val input = mapToLongList(rawInput).map { it.first() }.toMutableList()
        val changes = List(input.size) { mutableListOf<Pair<Long, Long>>() }
        val cnt = List(input.size) { mutableMapOf<Long, Long>() }

        repeat(2000) {
            for (i in input.indices) {
                val curr = input[i]
                val next = curr.nextSecret()
                changes[i].add((next % 10) - (curr % 10) to (next % 10))
                input[i] = next
            }
        }

        var ans = 0L

        for (i in changes.indices) {
            for (l in changes[i].indices) {
                val r = l + 4 - 1
                if (r >= changes[i].size) continue
                val hash = changes[i].subList(l, r + 1).hash()
                if (cnt[i].containsKey(hash.first)) continue
                cnt[i][hash.first] = hash.second
            }
        }

        for (i in changes.indices) {
            for (l in changes[i].indices) {
                val r = l + 4 - 1
                if (r >= changes[i].size) continue
                val hash = changes[i].subList(l, r + 1).hash()
                var sum = 0L
                for (j in cnt.indices) {
                    if (cnt[j].containsKey(hash.first)) {
                        sum += cnt[j][hash.first]!!
                    }
                }
                if (sum > ans) {
                    ans = max(ans, sum)
                    print("$sum ")
                    changes[i].subList(l, r + 1).println()
                }
            }
        }

        return ans
    }

    // Or read a large test input from the `src/Day22_test.txt` file:
    val testInput = readInput("Day22_test")

    // Read the input from the `src/Day22.txt` file.
    val input = readInput("Day22")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}
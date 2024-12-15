fun main() {
    data class Input(val a: Vec2, val b: Vec2, val prize: Vec2)
    val regex = Regex("""\d+""")
    fun parseLine(input: List<String>): Input {
        val (ax, ay) = regex.findAll(input[0]).toList().map { it.value.toInt() }
        val (bx, by) = regex.findAll(input[1]).toList().map { it.value.toInt() }
        val (px, py) = regex.findAll(input[2]).toList().map { it.value.toInt() }
        return Input(Vec2(ax, ay), Vec2(bx, by), Vec2(px, py))
    }
    fun parseInput(input: List<String>): List<Input> = input.chunked(4).map { parseLine(it) }

    fun solve(inputs: List<Input>): Long {
        var ans = 0L
        for ((a, b, prize) in inputs) {
            // a.x * x + b.x * y = prize.x
            // a.y * x + b.y * y = prize.y
            // (prize.x - b.x * y) * a.y = (prize.y - b.y * y) * a.x
            // prize.x * a.y - b.x * y * a.y = prize.y * a.x - b.y * y * a.x
            // prize.x * a.y - prize.y * a.x = b.x * y * a.y - b.y * y * a.x
            // (prize.x * a.y - prize.y * a.x) / (b.x * a.y - b.y * a.x) = y

            val y = (prize.x * a.y - prize.y * a.x) / (b.x * a.y - b.y * a.x)
            val x = (prize.x - b.x * y) / a.x
            if (a.x * x + b.x * y != prize.x || a.y * x + b.y * y != prize.y) continue
            ans += x * 3 + y
        }
        return ans
    }

    fun part1(input: List<String>): Long {
        val inputs = parseInput(input)
        return solve(inputs)
    }

    fun part2(input: List<String>): Long {
        val inputs = parseInput(input).map { Input(it.a, it.b, it.prize + Vec2(10000000000000, 10000000000000)) }
        return solve(inputs)
    }

    // Or read a large test input from the `src/Day13_test.txt` file:
    val testInput = readInput("Day13_test")

    // Read the input from the `src/Day13.txt` file.
    val input = readInput("Day13")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

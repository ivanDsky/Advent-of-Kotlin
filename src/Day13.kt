fun main() {
    data class Input(val a: Vec2, val b: Vec2, val prize: Vec2)
    val regex = Regex("""X.(\d+).*?Y.(\d+)""")
    fun parseLine(input: List<String>): Input {
        val (ax, ay) = regex.find(input[0])!!.destructured
        val (bx, by) = regex.find(input[1])!!.destructured
        val (px, py) = regex.find(input[2])!!.destructured
        return Input(Vec2(ax.toInt(), ay.toInt()), Vec2(bx.toInt(), by.toInt()), Vec2(px.toInt(), py.toInt()))
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

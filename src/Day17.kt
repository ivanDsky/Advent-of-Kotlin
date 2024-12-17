fun main() {
    val digitRegex = Regex("""\d+""")
    fun String.mapToIntList(): List<Long> = digitRegex.findAll(this).map { it.value.toLong() }.toList()
    fun mapToIntList(input: List<String>): List<List<Long>> = input.map { line -> line.mapToIntList() }
    fun mapToCharList(input: List<String>): List<List<Char>> = input.map { it.toList() }

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    var a: Long = 0
    var b: Long = 0
    var c: Long = 0
    var i = 0
    val result: MutableList<Long> = mutableListOf()

    fun program(code: Long, operand: Long = 0) {
        val value = when(operand) {
            4L -> a
            5L -> b
            6L -> c
            else -> operand
        }
        when(code) {
            0L -> a = (a shr value.toInt())
            1L -> b = (b xor operand) // literal
            2L -> b = (value % 8)
            3L -> if (a != 0L) i = operand.toInt()
            4L -> b = (b xor c)
            5L -> result += (value % 8)
            6L -> b = (a shr value.toInt())
            7L -> c = (a shr value.toInt())
        }
    }

    fun part1(input: List<String>): Long {
        result.clear()
        val ints = mapToIntList(input)
        a = ints[0][0]
        b = ints[1][0]
        c = ints[2][0]
        val program = ints[4]

        i = 0
        while (i < program.size) {
            val opcode = program[i]
            val operand = program[i + 1]
            program(opcode, operand)
            if (a != 0L && opcode == 3L) continue
            i += 2
        }

        result.joinToString(",").println()
        return 0L
    }

    fun part2(input: List<String>): Long {
        val ints = mapToIntList(input)
        val program = ints[4]
        var programIndex = 0
        var test = 0L

        // Precalculated suffixes that were repeating
        var suffix = listOf<Long>(
            0b0000010101000101010,
            0b0000010101000101101,
            0b0000010101000101111,
        )

        val shift = 64 - suffix.first().countLeadingZeroBits() + 5

        while (true) {
            for (s in suffix) {
                programIndex = 0
                result.clear()

                a = (test shl shift) + s
                b = ints[1][0]
                c = ints[2][0]

                i = 0
                while (i < program.size) {
                    val opcode = program[i]
                    val operand = program[i + 1]
                    program(opcode, operand)
                    if (a != 0L && opcode == 3L) continue
                    if (opcode == 5L && (programIndex >= program.size || result.last() != program[programIndex++])) break
//                    if (result.size > 8) break
                    i += 2
                }

//                if (result.size > 12) {
//                    println(((test shl shift) + s).toString(2))
//                }

                if (result == program) {
                    ((test shl shift) + s).println()
                    return 0L
                } else {
                    test++
                }
            }
        }
        return 0L
    }

    // Or read a large test input from the `src/Day17_test.txt` file:
    val testInput = readInput("Day17_test")

    // Read the input from the `src/Day17.txt` file.
    val input = readInput("Day17")
    part1(testInput)
    part2(testInput)
    part1(input)
    part2(input)
}

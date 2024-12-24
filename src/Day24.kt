import java.util.LinkedList
import kotlin.math.max

enum class InstructionType { AND, OR, XOR }
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
    data class Instruction(val lGate: String, val rGate: String, val output: String, val type: InstructionType)
    val graph: MutableMap<String, Instruction> = mutableMapOf()
    val values: MutableMap<String, Boolean> = mutableMapOf()



    fun String.toGate() {
        val (name, canCarry) = split(": ")
        values[name] = canCarry == "1"
    }
    val rename: MutableMap<String, String> = mutableMapOf()
    fun String.toInstruction(): Instruction {
        val (input, output) = split(" -> ")
        val (lGate, operation, rGate) = input.split(" ")
        val type = when (operation) {
            "AND" -> InstructionType.AND
            "OR" -> InstructionType.OR
            "XOR" -> InstructionType.XOR
            else -> error("Unknown instruction type")
        }


        val l = if (lGate < rGate) lGate else rGate
        val r = if (lGate < rGate) rGate else lGate

        if (l[0] == 'x' && r[0] == 'y') {
            val t = if (type == InstructionType.AND) 'a' else 'r'
            rename[output] = "$t${lGate.drop(1)}"
        }

        return Instruction(l, r, output, type)
    }

    fun dfs(gate: String): Boolean {
        if (gate in values) return values[gate]!!
        val instruction = graph[gate] ?: return false
        val left = dfs(instruction.lGate)
        val right = dfs(instruction.rGate)
        values[gate] = when (instruction.type) {
            InstructionType.AND -> left and right
            InstructionType.OR -> left or right
            InstructionType.XOR -> left xor right
        }
        return values[gate]!!
    }

    fun part1(rawInput: List<String>): Long {
        values.clear()
        graph.clear()
        val inputGates = rawInput.takeWhile { it.isNotEmpty() }.map { it.toGate() }
        val instructions = rawInput.takeLastWhile { it.isNotEmpty() }.map { it.toInstruction() }
        instructions.forEach { instruction ->
            graph[instruction.output] = instruction
        }

        for (instruction in instructions) {
            dfs(instruction.output)
        }

//        for (gate in graph.keys.sorted()) {
//            println("${gate}: ${if(values[gate] == true) 1 else 0}")
//        }

        var ans = 0L
        for (gate in graph.keys) {
            if (values[gate] == true && gate.startsWith("z")) {
                val bit = gate.drop(1).toInt()
                ans = ans or (1L shl bit)
            }
        }
        return ans
    }


    fun part2(rawInput: List<String>): String {
        rename.clear()
        values.clear()
        graph.clear()
        val inputGates = rawInput.takeWhile { it.isNotEmpty() }.map { it.toGate() }
        val instructions = rawInput.takeLastWhile { it.isNotEmpty() }.map { it.toInstruction() }
        instructions.forEach { instruction ->
            graph[instruction.output] = instruction
        }

        instructions
            .sortedByDescending { it.output }
//            .sortedBy { it.lGate }
            .map {
                val l = rename[it.lGate] ?: it.lGate
                val r = rename[it.rGate] ?: it.rGate
                val o = rename[it.output] ?: it.output
                "$l ${it.type} $r -> $o"
            }
//            .sorted()
            .forEach { println(it) }


        fun Long.toInstructions(name: Char) {
            for (i in 0..44) {
                val isOne = this shr i and 1L == 1L
                values["$name$i"] = isOne
            }
        }

//        (15L shl 38).toInstructions('x')
//        (0L shl 32).toInstructions('y')
        (15L).toInstructions('x')
        (171L).toInstructions('y')
        for (instruction in instructions) {
            dfs(instruction.output)
        }
//z16 mrb
//dhm qjd
//z32 gfm
//z08 cdj


//        for (gate in graph.keys.sorted()) {
//            println("${gate}: ${if(values[gate] == true) 1 else 0}")
//        }

        var ans = 0L
        for (gate in graph.keys) {
            if (values[gate] == true && gate.startsWith("z")) {
                val bit = gate.drop(1).toInt()
                ans = ans or (1L shl bit)
            }
        }
        return ans.toString(10)
    }

    fun solve2(rawInput: List<String>, x: Long, y: Long): String {
        values.clear()
        graph.clear()
        val inputGates = rawInput.takeWhile { it.isNotEmpty() }.map { it.toGate() }
        val instructions = rawInput.takeLastWhile { it.isNotEmpty() }.map { it.toInstruction() }
        instructions.forEach { instruction ->
            graph[instruction.output] = instruction
        }


        fun Long.toInstructions(name: Char) {
            for (i in 0..44) {
                val isOne = this shr i and 1L == 1L
                values["$name$i"] = isOne
            }
        }

        x.toInstructions('x')
        y.toInstructions('y')
        for (instruction in instructions) {
            dfs(instruction.output)
        }

        var ans = 0L
        for (gate in graph.keys) {
            if (values[gate] == true && gate.startsWith("z")) {
                val bit = gate.drop(1).toInt()
                ans = ans or (1L shl bit)
            }
        }
        return ans.toString(2)
    }



    // Or read a large test input from the `src/Day24_test.txt` file:
    val testInput = readInput("Day24_test")

    // Read the input from the `src/Day24.txt` file.
    val input = readInput("Day24")
//    part1(testInput).println()
//    part2(testInput).println()
//    part1(input).println()
    part2(input).println()

    for (i in 0..44) {
        val x = (1L shl i)
        val y = (1L shl i)
        if (solve2(input, x, 0) != x.toString(2)) {
            println(i)
        }
        if (solve2(input, 0, y) != y.toString(2)) {
            println(i)
        }
    }
}
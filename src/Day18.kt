import java.util.LinkedList
import javax.swing.text.html.HTML.Attribute.N
import kotlin.math.min

fun main() {
    val digitRegex = Regex("""\d+""")
    fun String.mapToLongList(): List<Long> = digitRegex.findAll(this).map { it.value.toLong() }.toList()
    fun String.mapToIntList(): List<Int> = digitRegex.findAll(this).map { it.value.toInt() }.toList()
    fun mapToLongList(input: List<String>): List<List<Long>> = input.map { line -> line.mapToLongList() }
    fun mapToIntList(input: List<String>): List<List<Int>> = input.map { line -> line.mapToIntList() }
    fun mapToCharList(input: List<String>): List<List<Char>> = input.map { it.toList() }

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    val N = 71

    fun part1(input: List<String>): Long {
        val pos = input.map{
            val (x,y) = it.toIntList()
            Vec2(y,x)
        }.take(1024)

        val blocks = List(N) { MutableList(N) { '.' } }
        for (p in pos) { blocks[p] = '#' }

        val queue = LinkedList<Vec2>()
        val dist = List(N) { MutableList(N) { Int.MAX_VALUE } }
        queue += Vec2(0, 0)
        dist[0][0] = 0
        while (queue.isNotEmpty()) {
            val p = queue.poll()
            if (p == Vec2(N - 1, N - 1)) break
            for (d in deltas.values) {
                val np = p + d
                if (np inBounds blocks && blocks[np] == '.') {
                    blocks[np] = '#'
                    dist[np] = dist[p] + 1
                    queue += np
                }
            }
        }



        return dist[N - 1][N - 1].toLong()
    }

    fun part2(input: List<String>): Long {
        val posD = input.map{
            val (x,y) = it.toIntList()
            Vec2(y,x)
        }

        for (i in 1..posD.size) {
            val pos = posD.take(i)
            val blocks = List(N) { MutableList(N) { '.' } }
            for (p in pos) { blocks[p] = '#' }

            val queue = LinkedList<Vec2>()
            val dist = List(N) { MutableList(N) { Int.MAX_VALUE } }
            queue += Vec2(0, 0)
            dist[0][0] = 0
            while (queue.isNotEmpty()) {
                val p = queue.poll()
                if (p == Vec2(N - 1, N - 1)) break
                for (d in deltas.values) {
                    val np = p + d
                    if (np inBounds blocks && blocks[np] == '.') {
                        blocks[np] = '#'
                        dist[np] = dist[p] + 1
                        queue += np
                    }
                }
            }
            if (dist[N - 1][N - 1] == Int.MAX_VALUE) {
                println("${posD[i - 1].y},${posD[i - 1].x}")
                break
            }
        }

        return 0L
    }

    // Or read a large test input from the `src/Day18_test.txt` file:
    val testInput = readInput("Day18_test")

    // Read the input from the `src/Day18.txt` file.
    val input = readInput("Day18")
    part1(testInput).println()
    part2(testInput)
    part1(input).println()
    part2(input)
}

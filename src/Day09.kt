import java.util.*
import kotlin.math.min

fun main() {

    fun part1(input: String): Long {
        val numbers = input.withIndex().filter { it.index % 2 == 0 }.map { it.value.code - '0'.code }
        val spaces = input.withIndex().filter { it.index % 2 == 1 }.map { it.value.code - '0'.code } + Int.MAX_VALUE

        val result = mutableListOf<Pair<Int, Int>>()
        var numForward = 0
        var numBackward = numbers.size
        var numLeft = 0
        var spacesForward = -1
        var spacesLeft = 0
        while (numBackward > numForward) {
            if (spacesLeft == 0) {
                result += (numForward to numbers[numForward])
                numForward++
                spacesForward++
                spacesLeft = spaces[spacesForward]
            }
            if (numLeft == 0) {
                numBackward--
                numLeft = numbers[numBackward]
            }

            if (spacesLeft >= numLeft) {
                spacesLeft -= numLeft
                result += (numBackward to numLeft)
                numLeft = 0
            } else {
                numLeft -= spacesLeft
                result += (numBackward to spacesLeft)
                spacesLeft = 0
            }
        }
        result += (numBackward to numLeft)
        var ans = 0L
        var index = 0
        for ((number, size) in result)  {
            val startIndex = index.toLong()
            val endIndex = (index + size - 1).toLong()
            val sumIndex = (endIndex * (endIndex + 1) - startIndex * (startIndex - 1)) / 2
            ans += number * sumIndex
            index += size
        }
        return ans
    }


    fun part2(input: String): Long {
        val numbers = input.withIndex().filter { it.index % 2 == 0 }.map { it.value.code - '0'.code }
        val spaces = input.withIndex().filter { it.index % 2 == 1 }.map { it.value.code - '0'.code }
        val freeSpaces: MutableMap<Int, MutableSet<Int>> = TreeMap()
        for (i in 0..9) freeSpaces[i] = TreeSet()

        var idx = 0
        for (spaceIdx in spaces.indices) {
            idx += numbers[spaceIdx]
            freeSpaces.getOrPut(spaces[spaceIdx]) { TreeSet() }.add(idx)
            idx += spaces[spaceIdx]
        }

        val result: MutableList<Pair<Int, Long>> = mutableListOf()
        for (numIndex in numbers.lastIndex downTo 1) {
            var minSpace = Int.MAX_VALUE
            var minIndex = Int.MAX_VALUE
            for ((space, indices) in freeSpaces) {
                if (indices.isEmpty() || space < numbers[numIndex]) continue
                if (indices.first() < minIndex) {
                    minSpace = space
                    minIndex = indices.first()
                }
            }
            if (minIndex == Int.MAX_VALUE) {
                result += (numIndex to idx.toLong())
            } else {
                minIndex = min(minIndex, idx)
                freeSpaces[minSpace]!!.remove(minIndex)
                result += (numIndex to minIndex.toLong())
                val remainingSpace = minSpace - numbers[numIndex]
                val newIndex = minIndex + numbers[numIndex]
                freeSpaces[remainingSpace]!!.add(newIndex)
            }

            idx -= numbers[numIndex - 1]
            idx -= spaces[numIndex - 1]

        }
        result += (0 to idx.toLong())

        var ans = 0L
        for((number, startIndex) in result) {
            val endIndex = startIndex + numbers[number] - 1
            val fullEnd = (endIndex * (endIndex + 1)) / 2L
            val fullStart = ((startIndex - 1) * startIndex) / 2L
            ans += (fullEnd - fullStart) * number
        }
        return ans
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readRawInput("Day09_test")

    // Read the input from the `src/Day09.txt` file.
    val input = readRawInput("Day09")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

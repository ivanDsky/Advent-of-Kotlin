fun main() {
    data class Robot(val pos: Vec2, val vel: Vec2)
    val width = 101L//wide
    val height = 103L//tall

    fun sim(robot: Robot, time: Long): Vec2 {
        return Vec2(((robot.pos.x + robot.vel.x * time) % width + width) % width, ((robot.pos.y + robot.vel.y * time) % height + height) % height)
    }

    fun part1(input: List<String>): Long {
        val positions = input.map { line ->
            val (rowPos, rowVel) = line.split(" ")
            val pos = Vec2(rowPos.split("=")[1].split(",")[0].toInt(), rowPos.split("=")[1].split(",")[1].toInt())
            val vel = Vec2(rowVel.split("=")[1].split(",")[0].toInt(), rowVel.split("=")[1].split(",")[1].toInt())
            Robot(pos, vel)
        }.map { sim(it, 100) }
        val quad: List<MutableList<Int>> = List(2) { MutableList(2) { 0 } }

        for (pos in positions) {
            if (pos.x == width / 2L || pos.y == height / 2L) continue
            val f = if (pos.x > width / 2) 1 else 0
            val s = if (pos.y > height / 2) 1 else 0
            quad[f][s]++
        }
        return quad[0][0] * quad[0][1] * quad[1][0] * quad[1][1].toLong()
    }

    fun part2(input: List<String>): Long {
        // Solved by printing simulations from 0 to 100000 to file and find out that XMAS tree will have many '#' in a row
        // So CTRL-F by file of finding "#######" gave me the correct answer
        return 0L
    }

    // Or read a large test input from the `src/Day14_test.txt` file:
    val testInput = readInput("Day14_test")

    // Read the input from the `src/Day14.txt` file.
    val input = readInput("Day14")
//    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> = input.map { it.split(" ").map { it.toInt()} }

    lateinit var grid: List<MutableList<Char>>

    val deltas = mapOf(
        Direction.DOWN to Vec2(1, 0),
        Direction.UP to Vec2(-1, 0),
        Direction.RIGHT to Vec2(0, 1),
        Direction.LEFT to Vec2(0, -1)
    )

    fun move(pos: Vec2, dir: Direction): Boolean {
        val newPos = pos + deltas[dir]!!
        if (grid[newPos] == '#') return false
        if (grid[newPos] == 'O') {
            val ans = move(newPos, dir)
            if (ans) {
                grid[newPos] = grid[pos]
                grid[pos] = '.'
            }
            return ans
        }
        if (grid[newPos] == '.') {
            grid[newPos] = grid[pos]
            grid[pos] = '.'
            return true
        }
        return false
    }

    fun solve(moves: List<Direction>) {
        val x = grid.indexOfFirst { it.contains('@') }
        val y = grid[x].indexOf('@')
        var pos = Vec2(x, y)
        for (dir in moves) {
            if(move(pos, dir)) pos += deltas[dir]!!
            if (grid[pos] != '@') {
                error("Invalid position")
            }
        }

        var ans = 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == 'O') ans += (100 * i + j)
            }
        }
        println(ans)
    }

    fun part1(input: List<String>): Long {
        grid = input.takeWhile { it.isNotBlank() }.map { it.toMutableList() }
        val moves = input.takeLastWhile { it.isNotBlank() }.joinToString("").map {
            when(it) {
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                else -> error("Invalid direction")
            }
        }
        solve(moves)
        return 0L
    }

    fun canMove(pos: Vec2, dir: Direction): Boolean {
        val newPos = pos + deltas[dir]!!
        if (grid[newPos] == '#') return false
        if (grid[newPos] == '[' || grid[newPos] == ']') {
            val newPos2 = newPos + if (grid[newPos] == '[') Vec2(0, 1) else Vec2(0, -1)
            return when(dir) {
                Direction.UP, Direction.DOWN -> canMove(newPos, dir) && canMove(newPos2, dir)
                Direction.RIGHT, Direction.LEFT -> canMove(newPos2, dir)
            }
        }
        if (grid[newPos] == '.') return true
        return false
    }

    fun move2(pos: Vec2, dir: Direction) {
        val newPos = pos + deltas[dir]!!
        if (grid[newPos] == '#') return
        if (grid[newPos] == '[' || grid[newPos] == ']') {
            val newPos2 = newPos + if (grid[newPos] == '[') Vec2(0, 1) else Vec2(0, -1)
            when(dir) {
                Direction.UP, Direction.DOWN -> {
                    move2(newPos, dir)
                    move2(newPos2, dir)
                    grid[newPos2] = '.'
                }
                Direction.RIGHT, Direction.LEFT -> {
                    move2(newPos2, dir)
                    grid[newPos2] = grid[newPos]
                }
            }
            grid[newPos] = grid[pos]
            grid[pos] = '.'
        }
        if (grid[newPos] == '.') {
            grid[newPos] = grid[pos]
            grid[pos] = '.'
        }
    }

    fun solve2(moves: List<Direction>) {
        val x = grid.indexOfFirst { it.contains('@') }
        val y = grid[x].indexOf('@')
        var pos = Vec2(x, y)
        for (dir in moves) {
            if(canMove(pos, dir)) {
                move2(pos, dir)
                pos += deltas[dir]!!
            }
            if (grid[pos] != '@') {
                error("Invalid position")
            }
        }

        var ans = 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == '[') ans += (100 * i + j)
            }
        }
        println(ans)
    }

    fun part2(input: List<String>): Long {
        grid = input.takeWhile { it.isNotBlank() }.map { line ->
            line.flatMap {
                when(it) {
                    '#' -> "##".toList()
                    'O' -> "[]".toList()
                    '.' -> "..".toList()
                    '@' -> "@.".toList()
                    else -> error("Invalid character")
                }
            }.toMutableList()
        }
        val moves = input.takeLastWhile { it.isNotBlank() }.joinToString("").map {
            when(it) {
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                else -> error("Invalid direction")
            }
        }
        solve2(moves)
        return 0L
    }

    // Or read a large test input from the `src/Day15_test.txt` file:
    val testInput = readInput("Day15_test")

    // Read the input from the `src/Day15.txt` file.
    val input = readInput("Day15")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads the given input txt file as string.
 */
fun readRawInput(name: String) = Path("src/$name.txt").readText().trim()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

private val numberRegex = Regex("-?\\d+")
fun String.toIntList(): List<Int> = numberRegex.findAll(this).map { it.value.toInt() }.toList()
fun String.toLongList(): List<Long> = numberRegex.findAll(this).map { it.value.toLong() }.toList()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


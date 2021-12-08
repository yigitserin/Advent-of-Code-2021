import kotlin.collections.HashMap

fun main() {
    fun part1(input: List<String>): Int {
        val outputLines = input.map { it.split(" | ") }.map { it.map { it.split(" ") } }.map { it[1] }
        return outputLines.flatten().count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { it.split(" | ") }.map { it.map { it.split(" ") } }
        return lines.sumOf { calculateLineOutput(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun calculateLineOutput(line: List<List<String>>): Int{
    val allNumbers = line.flatten().map { it.toList().sorted().map { it.toString() } }
    val digits = calculateDigits(allNumbers)

    return line[1].joinToString("") { output ->
        val outputDigits = output.toList().map { it.toString() }.sorted()
        digits.filter { it.value == outputDigits }.keys.first().toString()
    }.toInt()
}

private fun calculateDigits(allNumbers: List<List<String>>): HashMap<Int, List<String>>{
    val digits = HashMap<Int, List<String>>()
    digits[1] = allNumbers.first { it.size == 2 }
    digits[7] = allNumbers.first { it.size == 3 }
    digits[4] = allNumbers.first { it.size == 4 }
    digits[8] = allNumbers.first { it.size == 7 }
    digits[2] = allNumbers.first { it.size == 5 && it.intersect(digits[4]!!.toSet()).size == 2 }
    digits[3] = allNumbers.first { it.size == 5 && it.intersect(digits[1]!!.toSet()).size == 2 }
    digits[5] = allNumbers.first { it.size == 5 && !it.containsAll(digits[2]!!) && !it.containsAll(digits[3]!!) }
    digits[6] = allNumbers.first { it.size == 6 && it.intersect(digits[1]!!.toSet()).size == 1 }
    digits[9] = allNumbers.first { it.size == 6 && it.intersect(digits[4]!!.toSet()).size == 4 }
    digits[0] = allNumbers.first { it.size == 6 && !it.containsAll(digits[6]!!) && !it.containsAll(digits[9]!!) }
    return digits
}
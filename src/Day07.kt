import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        return calculateMinFuel(input, true)
    }

    fun part2(input: List<String>): Int {
        return calculateMinFuel(input, false)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun calculateMinFuel(input: List<String>, isConstant: Boolean): Int{
    val positions = input[0].split(",").map { it.toInt() }
    val map = HashMap(positions.associateWith { positions.count { intToCount -> intToCount == it } })

    var minFuel = Int.MAX_VALUE

    for (i in 0..positions.maxOrNull()!!){
        minFuel = min(minFuel, calculateFuelForPosition(isConstant, i, map))
    }

    return minFuel
}

private fun calculateFuelForPosition(isConstant: Boolean, position: Int, positions: Map<Int, Int>) :Int {
    var fuel = 0
    positions.forEach { (currentPosition, count) ->
        var diff = abs(currentPosition - position)
        if (!isConstant){
            diff = (diff * (diff + 1)) / 2
        }
        fuel += diff * count
    }
    return fuel
}
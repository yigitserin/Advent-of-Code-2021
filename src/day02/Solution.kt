package day02

import readInput

private fun calculatePosition(movement: String): Pair<Int, Int>{
    val parts = movement.split(" ")
    val direction = parts[0]
    val distance = parts[1].toInt()

    return when (direction) {
        "forward" -> Pair(distance, 0)
        "up" -> Pair(0, distance * -1)
        "down" -> Pair(0, distance)
        else -> Pair(0,0)
    }
}

fun main() {
    val day = "02"

    fun part1(input: List<String>): Int {

        var x = 0
        var y = 0

        input.forEach {
            val changedPos = calculatePosition(it)
            x += changedPos.first
            y += changedPos.second
        }

        return x * y
    }

    fun part2(input: List<String>): Int {

        var x = 0
        var y = 0
        var aim = 0

        input.forEach {
            val changedPos = calculatePosition(it)
            if (changedPos.first != 0){
                //Only movement
                x += changedPos.first
                y += changedPos.first * aim
            }else{
                //Only change aim
                aim += changedPos.second
            }
        }

        return x * y
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input_test", "day$day")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Input", "day$day")
    println(part1(input))
    println(part2(input))
 }
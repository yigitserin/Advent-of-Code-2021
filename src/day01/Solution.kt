package day01

import readInputInt

fun main() {
    val day = "01"
    fun part1(input: List<Int>): Int {
        var result = -1
        var lastTotal = 0

        input.forEach { currentValue ->
            if (currentValue > lastTotal){
                result += 1
            }
            lastTotal = currentValue
        }

        return result
    }

    fun part2(input: List<Int>): Int {
        var result = -1
        var lastTotal = 0

        input.forEachIndexed { index, currentValue ->
            if(input.size > index + 2){
                val currentTotal = currentValue + input[index+1] + input[index+2]
                if (currentTotal > lastTotal){
                    result += 1
                }
                lastTotal = currentTotal
            }
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputInt("Input_test", "day$day")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInputInt("Input", "day$day")
    println(part1(input))
    println(part2(input))
 }
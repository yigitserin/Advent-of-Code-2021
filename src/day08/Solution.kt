package day08

import readInput

fun main() {
    val day = "08"

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input_test", "day$day")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("Input", "day$day")
    println(part1(input))
    println(part2(input))
 }
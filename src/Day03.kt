fun main() {
    fun part1(input: List<String>): Int {
        var gamma = ""
        var epsilon = ""

        for (i in 0 until input[0].length){
            gamma += getDigitAtIndex(true, i, input)
            epsilon += getDigitAtIndex(false, i, input)
        }

        return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
    }

    fun part2(input: List<String>): Int {
        var oxy = ""
        var co = ""

        for (i in 0 until input[0].length){
            oxy += getDigitAtIndex(true, i, input.filter { it.startsWith(oxy) })
            co += getDigitAtIndex(false, i, input.filter { it.startsWith(co) })
        }

        return Integer.parseInt(oxy, 2) * Integer.parseInt(co, 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun getDigitAtIndex(mostCommon: Boolean, index: Int, input: List<String>): Int {

    val chars = arrayListOf<Int>()

    input.forEach {
        chars.add(it[index].toString().toInt())
    }

    val eachCount = chars.groupingBy { it }.eachCount()

    if (eachCount.values.count() == 2 && eachCount.values.distinct().count() == 1){
        return if (mostCommon){
            1
        }else{
            0
        }
    }

    return if (mostCommon){
        eachCount.maxByOrNull { it.value }?.key ?: 0
    }else{
        eachCount.minByOrNull { it.value }?.key ?: 0
    }
}
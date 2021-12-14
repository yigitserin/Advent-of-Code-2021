import kotlin.collections.HashMap

fun main() {
    fun part1(input: List<String>): Long {
        return calculateSteps(input, 10)
    }

    fun part2(input: List<String>): Long {
        return calculateSteps(input, 40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

private fun calculateSteps(input: List<String>, count: Int): Long{
    val template = input[0]
    val rules = input.subList(2, input.size).map { it.split(" -> ") }.associate { it.first() to it.last() }

    val templatePaired = template.toList().map { it.toString() }.windowed(2).map { it.joinToString("") }
    var templatedPairedMap = templatePaired.associateWith { templatePaired.count { c -> c == it }.toLong() }

    repeat(count){
        templatedPairedMap = calculateStep(templatedPairedMap,rules)
    }

    val maxMin = calculateMaxAndMinPolymerCounts(templatedPairedMap)
    return maxMin.first - maxMin.second
}

private fun calculateStep(template: Map<String, Long>, rules: Map<String, String>):  Map<String, Long>{
    val newTemplate = HashMap<String, Long>()
    template.forEach { (key, value) ->
        val newKey1 = key.first() + rules[key]!!
        val newKey2 = rules[key]!! + key.last()
        newTemplate[newKey1] = newTemplate.getOrDefault(newKey1,0L) + value
        newTemplate[newKey2] = newTemplate.getOrDefault(newKey2,0L) + value
    }
    return newTemplate
}

private fun calculateMaxAndMinPolymerCounts(template: Map<String, Long>): Pair<Long, Long>{
    val finalDict = HashMap<String, Long>()

    //Add first element of each pair.
    template.forEach {
        val ch1 = it.key.first().toString()
        finalDict[ch1] = finalDict.getOrDefault(ch1, 0L) + it.value
    }

    //Add the last letter.
    val lastKey = template.keys.last().last().toString()
    finalDict[lastKey] = finalDict[lastKey]!! + 1

    return Pair(finalDict.maxOf { it.value }, finalDict.minOf { it.value })
}
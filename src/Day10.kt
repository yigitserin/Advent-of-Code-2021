fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { processLine(it).first }
    }

    fun part2(input: List<String>): Long {
        val incompleteLines = input.filter { processLine(it).first == 0 }
        val scores = incompleteLines.map { calculatePointsForAutoComplete(processLine(it).second) }.sorted()
        return scores[scores.size/2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun processLine(line: String): Pair<Int, String>{
    var newLine = line
    val pairs = listOf("()", "[]", "{}", "<>")
    while (newLine.contains(pairs[0]) || newLine.contains(pairs[1]) || newLine.contains(pairs[2]) || newLine.contains(pairs[3])){
        pairs.forEach { pair ->
            newLine = newLine.replace(pair, "")
        }
    }
    return Pair(calculatePointsForSyntax(newLine), newLine)
}

private fun calculatePointsForSyntax(line: String): Int{
    line.toList().map { it.toString() }.forEach {
        when(it){
            ")" -> return 3
            "]" -> return 57
            "}" -> return 1197
            ">" -> return 25137
        }
    }
    return 0
}

private fun calculatePointsForAutoComplete(line: String): Long{
    var score = 0L
    line.toList().map { it.toString() }.reversed().forEach {
        score *= 5
        score += when(it){
            "(" -> 1
            "[" -> 2
            "{" -> 3
            "<" -> 4
            else -> 0
        }
    }
    return score
}
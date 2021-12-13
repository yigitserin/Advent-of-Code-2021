fun main() {
    fun part1(input: List<String>): Int {
        val coordinates = input.filter { !it.startsWith("fold") && it.isNotEmpty() }.map { it.split(",") }.map { Pair(it.first().toInt(), it.last().toInt()) }
        val foldInstructions = input.filter { it.startsWith("fold") }
        return doFold(foldInstructions.first(), coordinates).size
    }

    fun part2(input: List<String>) {
        var coordinates = input.filter { !it.startsWith("fold") && it.isNotEmpty() }.map { it.split(",") }.map { Pair(it.first().toInt(), it.last().toInt()) }
        val foldInstructions = input.filter { it.startsWith("fold") }
        foldInstructions.forEach {
            coordinates = doFold(it, coordinates)
        }
        visualizeOutput(coordinates)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}

private fun doFold(foldInstruction: String, coordinates: List<Pair<Int, Int>>): List<Pair<Int, Int>>{
    val isFoldHorizontal = foldInstruction.startsWith("fold along y=")
    val foldCoordinate = foldInstruction.split("=").last().toInt()

    return if (isFoldHorizontal){
        val coordinatesToFold = coordinates.filter { it.second > foldCoordinate }
        val coordinatesToFoldAfterFold = coordinatesToFold.map { Pair(it.first, ( (2 * foldCoordinate) - it.second)) }
        (coordinates + coordinatesToFoldAfterFold).distinct().filter { it.second < foldCoordinate }
    }else{
        val coordinatesToFold = coordinates.filter { it.first > foldCoordinate }
        val coordinatesToFoldAfterFold = coordinatesToFold.map { Pair(( (2 * foldCoordinate) - it.first), it.second) }
        (coordinates + coordinatesToFoldAfterFold).distinct().filter { it.first < foldCoordinate }
    }
}

private fun visualizeOutput(coordinates: List<Pair<Int, Int>>){
    for (y in 0..coordinates.maxOf { it.second }){
        for (x in 0..coordinates.maxOf { it.first }){
            if (coordinates.contains(Pair(x, y))){
                print("#")
            }else{
                print(".")
            }
        }
        print("\n")
    }
}
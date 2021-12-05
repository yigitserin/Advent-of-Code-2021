fun main() {
    fun part1(input: List<String>): Int {
        val coordinatePairs = input.map { getCoordinatePair(it) }
        val lines = coordinatePairs.map { getCoordinatesOfLine(false, it) }.flatten()
        return makeMapAndResult(lines)
    }

    fun part2(input: List<String>): Int {
        val coordinatePairs = input.map { getCoordinatePair(it) }
        val lines = coordinatePairs.map { getCoordinatesOfLine(true, it) }.flatten()
        return makeMapAndResult(lines)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
 }

private fun getCoordinatePair(inputLine: String): CoordinatePair {
    val parts = inputLine.split(" -> ")
    val part1 = parts[0].split(",").map { it.toInt() }
    val part2 = parts[1].split(",").map { it.toInt() }

    val coordinateStart = Pair(part1[0], part1[1])
    val coordinateEnd = Pair(part2[0], part2[1])
    return Pair(coordinateStart, coordinateEnd)
}

private fun getCoordinatesOfLine(allowDiagonals: Boolean, pair: CoordinatePair): List<Coordinate>{
    val list = arrayListOf<Coordinate>()

    //Only  considering horizontal or vertical lines for now.
    if (pair.first.first == pair.second.first || pair.first.second == pair.second.second){
        //Determine horizontal or vertical
        if (pair.first.first == pair.second.first){
            //Horizontal
            val x = pair.first.first

            val yPositions = when(pair.first.second <= pair.second.second){
                true -> pair.first.second..pair.second.second
                false -> pair.second.second..pair.first.second
            }

            yPositions.forEach { y ->
                list.add(Pair(x, y))
            }

        }else{
            //Vertical
            val y = pair.first.second

            val xPositions = when(pair.first.first <= pair.second.first){
                true -> pair.first.first..pair.second.first
                false -> pair.second.first..pair.first.first
            }

            xPositions.forEach { x ->
                list.add(Pair(x, y))
            }
        }
    }else{
        if (allowDiagonals){
            //Diagonal
            val startX = pair.first.first
            val endX = pair.second.first
            val startY = pair.first.second
            val endY = pair.second.second

            val xDiff = when(startX < endX){
                true -> 1
                false -> -1
            }

            val yDiff = when(startY < endY){
                true -> 1
                false -> -1
            }

            var currentX = startX
            var currentY = startY

            val range = when(startX < endX){
                true -> startX..endX
                false -> endX..startX
            }

            for (i in range){
                list.add(Pair(currentX, currentY))
                currentX += xDiff
                currentY += yDiff
            }
        }
    }

    return list
}

private fun makeMapAndResult(lines: List<Coordinate>): Int{
    val map = HashMap<Coordinate, Int>()
    lines.forEach { line ->
        if (map[line] == null){
            map[line] = 1
        }else{
            map[line] = map[line]!! + 1
        }
    }
    return map.count { it.value >= 2 }
}

typealias Coordinate = Pair<Int, Int>
typealias CoordinatePair = Pair<Coordinate, Coordinate>


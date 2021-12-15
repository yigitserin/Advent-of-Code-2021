import java.util.*

fun main() {
    fun part1(input: List<String>): Int{
        val map = input.map { it.toList().map { it.toString().toInt() } }
        return lowestRiskPath(map)
    }

    fun part2(input: List<String>): Int{
        val map = input.map { it.toList().map { it.toString().toInt() } }
        val expandedMap = expandMap(map)
        return lowestRiskPath(expandedMap)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

private fun lowestRiskPath(map: List<List<Int>>): Int {
    val dist = Array(map.size) { Array(map.first().size) { Int.MAX_VALUE } }.apply { get(0)[0] = 0 }

    val toVisit = PriorityQueue<Pair<Int, Int>> { (pY, pX), (rY, rX) -> dist[pY][pX].compareTo(dist[rY][rX]) }
    val visited = mutableSetOf(0 to 0)

    toVisit.add(0 to 0)

    while (toVisit.isNotEmpty()) {
        val nextCoordinate = toVisit.poll()
        visited.add(nextCoordinate)

        generateDirections(nextCoordinate.first, nextCoordinate.second, map).forEach { (nextRow, nextCol) ->
            if (!visited.contains(nextRow to nextCol)) {
                (dist[nextCoordinate.first][nextCoordinate.second] + map[nextRow][nextCol]).let { newDistance ->
                    if (newDistance < dist[nextRow][nextCol]) {
                        dist[nextRow][nextCol] = newDistance
                        toVisit.add(nextRow to nextCol)
                    }
                }
            }
        }
    }

    return dist[dist.lastIndex][dist.last().lastIndex]
}

private fun generateDirections(x: Int, y: Int, levels: List<List<Int>>): List<Pair<Int, Int>>{
    val directions = HashMap<Pair<Int, Int>, Boolean>()

    directions[Pair(x, y-1)] = y != 0
    directions[Pair(x, y+1)] = y < levels.size - 1
    directions[Pair(x-1, y)] = x != 0
    directions[Pair(x+1, y)] = x < levels.first().size - 1

    return directions.filter { it.value }.map { it.key }
}

/** Part two map expansion code
 */

private fun expandMap(map: List<List<Int>>): List<List<Int>>{
    val expandedRight = map.map{ row -> (1 until 5).fold(row) { acc, step -> acc + increaseAndCapList(row, step) } }
    return (1 until 5).fold(expandedRight) { acc, step -> acc + increaseList(expandedRight, step) }
}

private fun increaseList(list: List<List<Int>>, by: Int): List<List<Int>>{
    return list.map { row -> increaseAndCapList(row, by) }
}

private fun increaseAndCapList(list: List<Int>, by: Int): List<Int>{
    return list.map { level -> (level + by).let { if (it > 9) it - 9 else it } }
}


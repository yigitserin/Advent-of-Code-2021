fun main() {
    fun part1(input: List<String>): Int {
        var map = generateMap(input).mapValues { Pair(it.value, false) }.toMutableMap()
        var flashes = 0
        repeat(100){
            val calculateStep = calculateStep(map, input[0].length, input.size)
            map = calculateStep.first
            flashes += calculateStep.second
        }
        return flashes
    }

    fun part2(input: List<String>): Int {
        var map = generateMap(input).mapValues { Pair(it.value, false) }.toMutableMap()
        repeat(Int.MAX_VALUE){ step ->
            map =  calculateStep(map, input[0].length, input.size).first
            if (isSync(map)){
                return step + 1
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

private fun generateMap(input: List<String>): HashMap<String, Int>{
    val map = HashMap<String, Int>()
    input.forEachIndexed { y, line ->
        line.toList().forEachIndexed { x, value ->
            map["$x,$y"] = value.toString().toInt()
        }
    }
    return map
}

private fun generateDirections(coordinate: String, maxX: Int, maxY: Int): HashMap<String, Boolean>{

    val coordinateParts = coordinate.split(",")
    val x = coordinateParts[0].toInt()
    val y = coordinateParts[1].toInt()

    val directions = HashMap<String, Boolean>()
    //Top
    directions["$x,${y-1}"] = y != 0
    //Bottom
    directions["$x,${y+1}"] = y < maxY - 1
    //Left
    directions["${x-1},$y"] = x != 0
    //Right
    directions["${x+1},$y"] = x < maxX - 1
    //Top-Left
    directions["${x-1},${y-1}"] = y != 0 && x != 0
    //Top-Right
    directions["${x+1},${y-1}"] = y != 0 && x < maxX - 1
    //Bottom-Left
    directions["${x-1},${y+1}"] = y < maxY - 1 && x != 0
    //Bottom-Right
    directions["${x+1},${y+1}"] = y < maxY - 1 && x < maxX - 1

    return directions
}

private fun calculateStep(octopusMap: OctopusMap, maxX: Int, maxY: Int): Pair<OctopusMap, Int> {

    var map = octopusMap
    var flashes = 0

    //First increment all values.
    map = map.mapValues { Pair(it.value.first + 1, it.value.second) }.toMutableMap()

    //While any octopus have energy greater than 9, repeat:
    while (map.values.count { it.first > 9 && !it.second } > 0) {
        //For each octopus that should flash, increment its values and its neighbours
        map.filter { it.value.first > 9 && !it.value.second }.forEach { (coordinate, octopus) ->
            val calculateOctopus = calculateOctopus(coordinate, map, maxX, maxY)
            map = calculateOctopus.first
            flashes += calculateOctopus.second
        }
    }

    //Reset flashes
    map = map.mapValues { Pair(if(it.value.second) 0 else it.value.first, false) }.toMutableMap()

    return Pair(map, flashes)
}

private fun calculateOctopus(coordinate: String, octopusMap: OctopusMap, maxX: Int, maxY: Int): Pair<OctopusMap, Int>{
    val directions = generateDirections(coordinate, maxX, maxY).filter { it.value }
    var flashes = 0

    if (octopusMap[coordinate]!!.first > 9){
        flashes += 1
        octopusMap[coordinate] = Pair(octopusMap[coordinate]!!.first, true)
    }

    directions.forEach { direction ->
        octopusMap[direction.key] = Pair(octopusMap[direction.key]!!.first + 1, octopusMap[direction.key]!!.second)
    }

    return Pair(octopusMap, flashes)
}

private fun isSync(octopusMap: OctopusMap): Boolean{
    return octopusMap.values.map { it.first }.distinct().count() == 1
}

private typealias OctopusMap = MutableMap<String, Pair<Int, Boolean>>
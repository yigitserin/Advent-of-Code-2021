fun main() {
    fun part1(input: List<String>): Int {
        val map = generateMap(input)

        return map.keys.mapNotNull {
            getLowestPointCoordinates(it, arrayListOf(), map, input[0].length, input.size)
        }.sumOf { map[it]!! + 1 }
    }

    fun part2(input: List<String>): Int {
        val map = generateMap(input)

        val lowestPoints = map.keys.mapNotNull {
            getLowestPointCoordinates(it, arrayListOf(), map, input[0].length, input.size)
        }

        val largestBasins = lowestPoints.map {
            checkCurrentBasin(it, map, HashMap(), input[0].length, input.size)
        }.sortedByDescending { it.keys.count() }.take(3)

        return largestBasins.fold(1) { total, next -> total * next.keys.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
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
    directions["$x,${y-1}"] = y != 0
    directions["$x,${y+1}"] = y < maxY - 1
    directions["${x-1},$y"] = x != 0
    directions["${x+1},$y"] = x < maxX - 1

    return directions
}

private fun getLowestPointCoordinates(coordinate: String, exclude: List<String>, map: HashMap<String, Int>, maxX: Int, maxY: Int): String?{

    val directions = generateDirections(coordinate, maxX, maxY)
    val neighborValues = arrayListOf<Int>()
    directions.forEach { (direction, condition) ->
        if (condition && !exclude.contains(coordinate)){
            neighborValues.add(map[direction]!!)
        }
    }

    return if (neighborValues.filter { it <= map[coordinate]!! }.count { true } != 0){
        null
    }else{
        coordinate
    }
}

private fun checkCurrentBasin(coordinate: String, map: HashMap<String, Int>, currentBasin: HashMap<String, Int>, maxX: Int, maxY: Int): HashMap<String, Int>{
    var mutableBasin = currentBasin
    mutableBasin[coordinate] = map[coordinate]!!

    //Directions and conditions to check if they exist.
    val directions = generateDirections(coordinate, maxX, maxY)

    directions.forEach { (direction, condition) ->
        //Check if direction exists && not already added to basin && not has value of 9 && bigger then current
        if (condition && mutableBasin[direction] == null && map[direction] != 9 && map[direction]!! > map[coordinate]!!){
            mutableBasin[direction] = map[direction]!!
            mutableBasin = checkCurrentBasin(direction, map, mutableBasin, maxX, maxY)
        }
    }
    return mutableBasin
}
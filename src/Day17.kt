import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val ranges = getRanges(input)
        var currentMaxY = 0
        getSuccessfulTrajectories(ranges){ trajectory, _, _ ->
            currentMaxY = max(currentMaxY, trajectory.maxOf { it.second })
        }
        return currentMaxY
    }

    fun part2(input: List<String>): Int {
        val ranges = getRanges(input)
        var count = 0
        getSuccessfulTrajectories(ranges){ _, _ ,_ ->
            count += 1
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

private fun getRanges(input: List<String>): Pair<IntRange, IntRange>{
    val ranges = input[0].removePrefix("target area: x=").replace(" y=", "").split(",")
    val xTargetValues = ranges[0].split("..").map { it.toInt() }
    val yTargetValues = ranges[1].split("..").map { it.toInt() }
    val xRange = xTargetValues[0]..xTargetValues[1]
    val yRange = yTargetValues[0]..yTargetValues[1]
    return Pair(xRange, yRange)
}

private fun calculateTrajectoryForInitialSpeed(Vx: Int, Vy: Int, xRange: IntRange, yRange: IntRange): Pair<Boolean, ArrayList<Coordinate>>{
    val trajectory = arrayListOf<Coordinate>()
    var currentX = 0
    var currentY = 0
    var currentVx = Vx
    var currentVy = Vy

    while (true){
        //Velocity
        currentX += currentVx
        currentY += currentVy
        //Gravity
        currentVy -= 1
        //Drag
        if (currentVx > 0) currentVx -= 1 else if (currentVx < 0) currentVx += 1
        //Add to trajectory
        trajectory.add(Coordinate(currentX,currentY))
        //Target is hit
        if (currentX in xRange && currentY in yRange){
            return Pair(true, trajectory)
        }
        //Check if probe is past the target area
        if (currentX < 0 || currentX > xRange.last || currentY <yRange.first){
            return Pair(false, arrayListOf())
        }
    }
}

private fun getSuccessfulTrajectories(ranges: Pair<IntRange, IntRange>, onSuccess: (trajectory: ArrayList<Coordinate>, x: Int, y: Int) -> Unit){
    val initialSpeedRange = 200

    for (x in -initialSpeedRange..initialSpeedRange){
        for (y in -initialSpeedRange..initialSpeedRange){
            val trajectory = calculateTrajectoryForInitialSpeed(x,y, ranges.first, ranges.second)
            if (trajectory.first){
                onSuccess(trajectory.second, x, y)
            }
        }
    }
}
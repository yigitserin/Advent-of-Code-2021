fun main() {
    fun part1(input: List<String>): Int {
        var fishList = input[0].split(",").map { it.toInt() }

        repeat(80){
            fishList = calculateNextDay(fishList)
        }

        return fishList.size
    }

    fun part2(input: List<String>): Long {
        val fishList = input[0].split(",").map { it.toInt() }
        var lifeSpans = HashMap<Int, Long>()

        fishList.forEach {
            lifeSpans[it] = lifeSpans.getOrDefault(it, 0) + 1
        }

        repeat(256){
            lifeSpans = calculateNextDayWithLifespans(lifeSpans)
        }

        return lifeSpans.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

private fun calculateNextDay(fishList:  List<Int>):  List<Int>{
    var newbornFishCount = 0
    val fishArrayList = ArrayList(fishList)

    fishArrayList.forEachIndexed { index, fishAge ->
        if (fishAge == 0){
            newbornFishCount += 1
            fishArrayList[index] = 6
        }else{
            fishArrayList[index] -= 1
        }
    }

    //Add newborn fish
    repeat(newbornFishCount){
        fishArrayList.add(8)
    }

    return fishArrayList.toList()
}

private fun calculateNextDayWithLifespans(lifeSpans: HashMap<Int, Long>): HashMap<Int, Long>{

    val newLifeSpans = HashMap<Int, Long>()

    lifeSpans.forEach { (lifeSpan, count) ->
        if (lifeSpan == 0){
            newLifeSpans[6] = newLifeSpans.getOrDefault(6, 0) + count
            newLifeSpans[8] = newLifeSpans.getOrDefault(8, 0) + count
        }else{
            newLifeSpans[lifeSpan - 1] = newLifeSpans.getOrDefault(lifeSpan - 1, 0) + count
        }
    }

    return newLifeSpans
}
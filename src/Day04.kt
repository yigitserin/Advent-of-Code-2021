fun main() {
    fun part1(input: List<String>): Int {
        val pulls = input[0].split(",").map { it.toInt() }
        val playerBoards =  setBoards(input)
        val currentPulls = arrayListOf<Int>()

        pulls.forEach { pull ->
            currentPulls.add(pull)

            checkBingo(currentPulls, playerBoards)?.let { playerIndex ->
                return calculateScore(currentPulls, playerBoards[playerIndex])
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val pulls = input[0].split(",").map { it.toInt() }
        var playerBoards =  setBoards(input)
        val currentPulls = arrayListOf<Int>()

        pulls.forEach { pull ->
            currentPulls.add(pull)

            val bingoPlayerIndex = checkBingo(currentPulls, playerBoards)

            if (playerBoards.size == 1 && bingoPlayerIndex != null){
                return calculateScore(currentPulls, playerBoards[bingoPlayerIndex])
            }else{
                playerBoards = playerBoards.filterNot { checkPlayerBingo(currentPulls, it) }
            }
        }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun setBoards(input: List<String>): List<List<List<Int>>>{
    return input.subList(1,input.size)
        .asSequence()
        .filter{ it.isNotEmpty() }
        .map{ it.trim().replace("  ", " ") }
        .map{ it.split( " ") }
        .map { it.map { it.toInt() } }
        .chunked(5)
        .toList()
}

private fun checkBingo(pulls: ArrayList<Int>, boards: List<List<List<Int>>>): Int?{
    boards.forEachIndexed { playerIndex, playerBoards ->
        if (rowsHasBingo(pulls, playerBoards) || columnsHasBingo(pulls, playerBoards)){
            return playerIndex
        }
    }

    return null
}

private fun checkPlayerBingo(pulls: ArrayList<Int>, boards: List<List<Int>>): Boolean{
    if (rowsHasBingo(pulls, boards) || columnsHasBingo(pulls, boards)){
        return true
    }

    return false
}

private fun rowsHasBingo(pulls: ArrayList<Int>, row: List<List<Int>>): Boolean{
    return row.any { pulls.containsAll(it) }
}

private fun columnsHasBingo(pulls: ArrayList<Int>, row: List<List<Int>>): Boolean{
    val column = (row.indices).map { row.map { l -> l[it] } }
    return column.any { pulls.containsAll(it) }
}

private fun calculateScore(pulls: ArrayList<Int>, board: List<List<Int>>): Int{
    val sum = board.sumOf { it.filter { !pulls.contains(it) }.sum() }
    return sum * pulls.last()
}


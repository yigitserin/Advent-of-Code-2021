fun main() {
    fun part1(input: List<String>): Int {
        val nodes = generateNodes(input)
        return checkNodes(nodes)
    }

    fun part2(input: List<String>): Int {
        val nodes = generateNodes(input)
        return checkNodes2(nodes)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun generateNodes(input: List<String>): HashMap<String, ArrayList<String>>{
    val paths = input.map { it.split("-")[0] to it.split("-")[1] }
    val nodes = HashMap<String, ArrayList<String>>()

    paths.forEach { path ->
        if (nodes[path.first] == null){
            nodes[path.first] = arrayListOf()
        }

        if (nodes[path.second] == null){
            nodes[path.second] = arrayListOf()
        }

        if (path.first != "start" && path.second != "end"){
            nodes[path.second]!!.add(path.first)
        }

        if (path.second != "start" && path.first != "end"){
            nodes[path.first]!!.add(path.second)
        }
    }

    return nodes
}

private fun checkNodes(nodes: Map<String, List<String>>, currentNode: String = "start", currentPath: List<String> = listOf()): Int {

    val visitedAlready = currentPath.contains(currentNode)

    if (currentNode == "end"){
        return 1
    }else if (currentNode.first().isLowerCase() && visitedAlready){
        return 0
    }else{
        return nodes[currentNode]!!.sumOf { checkNodes(nodes, it, currentPath + currentNode) }
    }
}

private fun checkNodes2(nodes: Map<String, List<String>>, currentNode: String = "start", currentPath: List<String> = listOf()): Int {

    val counts = currentPath.filter { it.first().isLowerCase() }.groupingBy { it }.eachCount()
    val visitedTwice = currentNode in counts.keys && counts.any { it.value > 1 }

    if (currentNode == "end"){
        return 1
    }else if (currentNode.first().isLowerCase() && visitedTwice){
        return 0
    }else{
        return nodes[currentNode]!!.sumOf { checkNodes2(nodes, it, currentPath + currentNode) }
    }
}

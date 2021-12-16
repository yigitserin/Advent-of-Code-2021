fun main() {
    fun part1(input: List<String>, index: Int): Int {
        val hexString = input[index]
        val binaryString = hexString.hexStringToBinaryString()
        val packets = getPacket(binaryString, arrayListOf())
        return packets.second.sumOf { it.take(3).toInt(2) }
    }

    fun part2(input: List<String>, index: Int): Long {
        val hexString = input[index]
        val binaryString = hexString.hexStringToBinaryString()
        return parse(binaryString).value()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput, 0) == 16)
    check(part1(testInput, 1) == 12)
    check(part1(testInput, 2) == 23)
    check(part1(testInput, 3) == 31)

    check(part2(testInput,4) == 3L)
    check(part2(testInput,5) == 54L)
    check(part2(testInput,6) == 7L)
    check(part2(testInput,7) == 9L)
    check(part2(testInput,8) == 1L)
    check(part2(testInput,9) == 0L)
    check(part2(testInput,10) == 0L)
    check(part2(testInput,11) == 1L)

    val input = readInput("Day16")
    println(part1(input,0))
    println(part2(input, 0))
}

private fun getPacket(packet: String, packets: ArrayList<String>): Pair<String, ArrayList<String>>{

    if (packet.length < 11){
        return Pair(packet, packets)
    }

    var mutablePacket = packet
    var mutablePackets = packets

    val version = mutablePacket.take(3).toInt(2)
    mutablePacket = mutablePacket.drop(3)
    val type = mutablePacket.take(3).toInt(2)
    mutablePacket = mutablePacket.drop(3)

    if (type == 4){
        //Literal Type
        val literalsList = arrayListOf<String>()
        var foundZero = false
        while (mutablePacket.length >= 5 && !foundZero){
            val number = mutablePacket.take(5)
            literalsList.add(number)
            mutablePacket = mutablePacket.drop(5)
            foundZero = number.startsWith("0")
        }

        //val result = literalsList.joinToString("") { it.drop(1) }.toInt(2)
        val literalPacket = packet.removeSuffix(mutablePacket)
        mutablePackets.add(literalPacket)
        return getPacket(mutablePacket, mutablePackets)
    }else{
        //Operator Type
        //type 0 == SUM
        //type 1 == PRODUCT
        //type 2 == MINIMUM
        //type 3 == MAXIMUM
        //type 5 == GREATER THAN
        //type 6 == LESS THAN
        //type 7 == EQUAL

        val lengthType = mutablePacket.take(1).toInt(2)
        mutablePacket = mutablePacket.drop(1)

        if (!mutablePackets.contains(packet)) mutablePackets.add(packet)

        if (lengthType == 0){
            //Total length is given.
            val totalLength = mutablePacket.take(15).toInt(2)
            mutablePacket = mutablePacket.drop(15)
            val subPacket = mutablePacket.take(totalLength)
            mutablePacket.drop(totalLength)
            val remainder = mutablePacket.removePrefix(subPacket)
            val result = getPacket(subPacket, mutablePackets)

            result.second.forEach { if(!mutablePackets.contains(it)) mutablePackets.add(it) }

            return getPacket(remainder, mutablePackets)

        }else{
            //Number of sub packages are given
            val subPackageCount = mutablePacket.take(11).toInt(2)
            mutablePacket = mutablePacket.drop(11)

            repeat(subPackageCount){
                val result = getPacket(mutablePacket, mutablePackets)
                mutablePacket = result.first
                result.second.forEach { if(!mutablePackets.contains(it)) mutablePackets.add(it) }
            }

            
            return getPacket(mutablePacket, mutablePackets)
        }
    }
}

private fun String.hexStringToBinaryString() = toList().map { it.toString() }.joinToString("") { it.hexDigitToBinaryString() }

private fun String.hexDigitToBinaryString(): String {
    var result = Integer.toBinaryString(Integer.parseInt(this, 16))
    while (result.length < 4){
        result = "0$result"
    }
    return result
}

/**
 * Part two parsing.
 */

private fun parse(packet: String): Packet {
    val type = packet.drop(3).take(3).toInt(2)
    val rest = packet.drop(6)

    return when (type) {
        4 -> {
            rest.chunked(5)
                .let { it.takeWhile { g -> g.first() == '1' } + it.first { g -> g.first() == '0' } }
                .let { Literal("${packet.take(6)}${it.joinToString("")}") }
        }
        else -> {
            when (rest.first()) {
                '0' -> {
                    val totalLength = rest.drop(1).take(15).toInt(2)
                    val subPackets = buildList<Packet> {
                        while (totalLength - sumOf { it.bits.length } > 0) {
                            parse(rest.drop(16 + sumOf { it.bits.length })).also { add(it) }
                        }
                    }
                    Operator("${packet.take(22)}${subPackets.joinToString("") { it.bits }}", subPackets)
                }
                else -> {
                    val subPacketsNumber = rest.drop(1).take(11).toInt(2)
                    val subPackets = buildList<Packet> {
                        repeat(subPacketsNumber) {
                            parse(rest.drop(12 + sumOf { it.bits.length })).also { add(it) }
                        }
                    }
                    Operator("${packet.take(18)}${subPackets.joinToString("") { it.bits }}", subPackets)
                }
            }
        }
    }
}

sealed class Packet(val bits: String){
    abstract fun sumOfVersions(): Int
    abstract fun value(): Long

    val type = bits.drop(3).take(3).toInt(2)
}

class Literal(bits: String): Packet(bits){
    override fun sumOfVersions() = bits.take(2).toInt()

    override fun value() = bits.drop(6)
        .chunked(5)
        .joinToString("") { it.drop(1) }
        .toLong(2)
}

class Operator(bits: String, private val subPackets: List<Packet>): Packet(bits){
    override fun sumOfVersions() = bits.take(3).toInt(2) + subPackets.sumOf { it.sumOfVersions() }

    override fun value(): Long = when (type) {
        0 -> subPackets.sumOf { it.value() }
        1 -> subPackets.fold(1) { acc, packet -> acc * packet.value() }
        2 -> subPackets.minOf { it.value() }
        3 -> subPackets.maxOf { it.value() }
        5 -> (subPackets[0].value() > subPackets[1].value()).toLong()
        6 -> (subPackets[0].value() < subPackets[1].value()).toLong()
        7 -> (subPackets[0].value() == subPackets[1].value()).toLong()
        else -> error("Unsupported type $type")
    }

    private fun Boolean.toLong() = if (this) 1L else 0L
}
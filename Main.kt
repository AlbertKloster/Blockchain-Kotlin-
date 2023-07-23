package blockchain

fun main() {
    val blockchainService = BlockchainService()

    print("Enter how many zeros the hash must start with: ")
    blockchainService.setNumberOfZeros(readln().toInt())

    repeat(5) {
        val timeStart = System.currentTimeMillis()
        val block = blockchainService.createBlock()
        val timeEnd = System.currentTimeMillis()
        printBlock(block)
        println("Block was generating for ${(timeEnd - timeStart) / 1000} seconds")
        println()
    }

}

private fun printBlock(block: Block) {
    println("""
            Block:
            Id: ${block.id}
            Timestamp: ${block.timeStamp}
            Magic number: ${block.magicNumber}
            Hash of the previous block:
            ${block.hashPrevious}
            Hash of the block:
            ${block.hash}
        """.trimIndent())
}
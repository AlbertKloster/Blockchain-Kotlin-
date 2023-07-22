package blockchain

fun main() {
    val blockchainService = BlockchainService()
    repeat(5) { blockchainService.createBlock() }

    for (id in 1..5) {
        val block = blockchainService.getBlockById(id.toLong())
        println("""
            Block:
            Id: ${block.id}
            Timestamp: ${block.timeStamp}
            Hash of the previous block:
            ${block.hashPrevious}
            Hash of the block:
            ${block.hash}
            
        """.trimIndent())
    }
}
package blockchain

class BlockchainService {
    private val blockchain = Blockchain(0)
    private val lowerTimeLimit = 0
    private val upperTimeLimit = 5

    fun createBlockchain(length: Int) {
        val minerMax = 10
        val minerList = mutableListOf<Miner>()
        var minerNumber = 1

        while (true) {
            val activeMiners = minerList.count { it.isAlive }

            if (activeMiners <= minerMax) {

                val increment = if (blockchain.isEmpty()) 0
                else if (blockchain.last().generationTime <= lowerTimeLimit) 1
                else if (blockchain.last().generationTime >= upperTimeLimit) -1
                else 0

                val miner = Miner(
                    "# $minerNumber",
                    blockchain,
                    if (blockchain.isEmpty()) "0" else blockchain.last().hash,
                    if (blockchain.isEmpty()) 0 else blockchain.last().numberOfLeadingZeros + increment
                )
                minerList.add(miner)
                miner.start()
                minerNumber++
            }

            if (!blockchain.isEmpty() && blockchain.last().id >= length) {
                minerList.forEach { it.interrupt() }
                return
            }
        }
    }

    fun print() {
        val result = mutableListOf<Block>()
        result.addAll(blockchain.getBlocks())
        result.forEach { printBlock(it) }
    }

    private fun printBlock(block: Block) {
        println(
            """
            Block:
            Created by miner ${block.minerName}
            Id: ${block.id}
            Timestamp: ${block.timeStamp}
            Magic number: ${block.magicNumber}
            Hash of the previous block:
            ${block.hashPrevious}
            Hash of the block:
            ${block.hash}
            Block was generating for ${block.generationTime} seconds
            ${if (block.generationTime <= lowerTimeLimit) "N increased to ${block.numberOfLeadingZeros + 1}" else if (block.generationTime >= upperTimeLimit) "N was decreased by 1" else "N stays the same"}
            
        """.trimIndent()
        )
    }

}
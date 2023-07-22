package blockchain

class BlockchainService {
    private val blockchain = Blockchain()
    private val cryptographer = Cryptographer()

    fun createBlock() {
        val id = if (blockchain.isEmpty()) 1L else blockchain.last().id + 1
        val timeStamp = System.currentTimeMillis()
        val hashPrevious = if (blockchain.isEmpty()) "0" else blockchain.last().hash
        val hash = cryptographer.applySha256(id.toString() + timeStamp.toString() + hashPrevious)

        blockchain.add(Block(id, timeStamp, hashPrevious, hash))
    }

    fun getBlockById(id: Long): Block {
        return blockchain.blocks.first { it.id == id }
    }

}
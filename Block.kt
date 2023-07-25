package blockchain

class Block(
    var previousBlock: Block? = null,
    var miner: User? = null,
    var id: Long = 1L,
    var timeStamp: Long = 0L,
    var magicNumber: Int = 0,
    var hash: String = "",
    var generationTime: Long = 0L,
    var numberOfLeadingZeros: Int = 0,
    var data: Data = Data()
) {
    private val cryptographer = Cryptographer()
    override fun toString(): String {
        return "Block(minerName='$miner', id=$id, timeStamp=$timeStamp, magicNumber=$magicNumber, hashPrevious='${previousBlock?.hash ?: "0"}', generationTime=$generationTime, numberOfLeadingZeros=$numberOfLeadingZeros, data=$data)"
    }

    @Synchronized
    fun size(): Int {
        var count = 1
        var previousBlock = previousBlock
        while (previousBlock != null) {
            previousBlock = previousBlock.previousBlock
            count++
        }
        return count
    }

    fun isValid() = cryptographer.getHash(this) == hash

    fun getLastTransactionId(): Int {
        var block: Block? = this
        while (block != null) {
            val lastTransactionId = block.getLastTransactionId()
            if (lastTransactionId != 0) return lastTransactionId
            block = block.previousBlock
        }
        return 0
    }

}

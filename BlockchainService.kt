package blockchain

import java.util.*

class BlockchainService {
    private val blockchain = Blockchain()
    private val cryptographer = Cryptographer()
    private var numberOfZeros = 0

    fun createBlock(): Block {
        val id = if (blockchain.isEmpty()) 1L else blockchain.last().id + 1
        val timeStamp = System.currentTimeMillis()
        val hashPrevious = if (blockchain.isEmpty()) "0" else blockchain.last().hash
        var magicNumber: Int
        var hash: String
        val random = Random()
        while (true) {
            magicNumber = random.nextInt(Int.MAX_VALUE)
            hash = cryptographer.applySha256(id.toString() + timeStamp.toString() + magicNumber.toString() + hashPrevious)
            if (hash.substring(0, numberOfZeros) == "0".repeat(numberOfZeros)) break
        }
        val block = Block(id, timeStamp, magicNumber, hashPrevious, hash)
        blockchain.add(block)
        return block
    }

    fun setNumberOfZeros(numberOfZeros: Int) {
        this.numberOfZeros = numberOfZeros
    }

}
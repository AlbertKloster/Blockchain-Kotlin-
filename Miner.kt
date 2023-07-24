package blockchain

import java.util.*

class Miner(
    name: String,
    private val blockchain: Blockchain,
    private val hashPrevious: String,
    private val numberOfLeadingZeros: Int,
    private val data: List<String>
) : Thread(name) {
    private val cryptographer = Cryptographer()

    override fun run() {
        val id = if (blockchain.isEmpty()) 1L else blockchain.last().id + 1
        val timeStamp = System.currentTimeMillis()
        var magicNumber: Int
        var hash: String
        val random = Random()
        val startTime = System.currentTimeMillis()
        while (true) {

            magicNumber = random.nextInt(Int.MAX_VALUE)
            val block = Block(name, id, timeStamp, magicNumber, hashPrevious, data = data)
            hash = cryptographer.getHash(block)
            if (hash.substring(0, numberOfLeadingZeros) == "0".repeat(numberOfLeadingZeros)) {
                val finishTime = System.currentTimeMillis()
                block.hash = hash
                block.generationTime = (finishTime - startTime) / 1000
                block.numberOfLeadingZeros = numberOfLeadingZeros
                blockchain.add(block)
                break
            }
        }
    }
}
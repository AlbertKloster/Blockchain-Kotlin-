package blockchain

import java.util.*

class Miner(
    private var miner: User,
    private val blockchain: Blockchain
) : Thread() {
    private val cryptographer = Cryptographer()

    private var isMining = true

    fun stopMining() {
        isMining = false
    }

    override fun run() {
        val startTime = System.currentTimeMillis()

        val block = Block()
        val lastBlock = blockchain.getLastBock()
        block.previousBlock = lastBlock
        block.miner = miner
        block.id = blockchain.getLastBock()?.id?.plus(1) ?: 1L
        block.timeStamp = System.currentTimeMillis()
        block.data = getData(lastBlock)
        block.numberOfLeadingZeros = getNumberOfLeadingZeros(lastBlock)
        block.magicNumber = getMagicNumber(block)
        block.hash = cryptographer.getHash(block)

        val finishTime = System.currentTimeMillis()

        block.generationTime = (finishTime - startTime) / 1000

        if (isMining) blockchain.append(block)

    }

    private fun getMagicNumber(block: Block): Int {
        var magicNumber = 0
        while (isMining) {
            magicNumber = Random().nextInt(Int.MAX_VALUE)
            block.magicNumber = magicNumber
            val hash = cryptographer.getHash(block)
            if (hash.substring(0, block.numberOfLeadingZeros) == "0".repeat(block.numberOfLeadingZeros)) break
        }
        return magicNumber
    }

    private fun getData(lastBlock: Block?): Data {
        val data = Data()
        if (lastBlock != null) {
            val numberOfTransactions = Random().nextInt(blockchain.userList.size)
            repeat(numberOfTransactions) {
                val from = blockchain.userList.random()
                val to = blockchain.userList.random()
                if (from != to) {
                    val maxValue = blockchain.getValueByUser(from)
                    if (maxValue > 0) {
                        val bound = (maxValue / 10) / numberOfTransactions
                        if (bound > 0) {
                            val value = Random().nextInt(bound)
                            val transactionId = lastBlock.data.getLastTransactionId() + 1
                            if (value > 0) {
                                val transaction = Transaction(transactionId, from, to, value)
                                transaction.signature = BlockchainAuthentication.signTransaction(transaction, blockchain.usernamePrivateKeyMap[from.name]!!)
                                data.add(transaction)
                            }
                        }
                    }
                }
            }
        }
        return data
    }

    private fun getNumberOfLeadingZeros(lastBlock: Block?): Int {
        return if (lastBlock == null) 0
        else if (lastBlock.generationTime < blockchain.upperTimeLimit)
            (lastBlock.numberOfLeadingZeros + 1).coerceAtMost(blockchain.maxNumberOfLeadingZeros)
//            else if (lastBlock.generationTime < blockchain.lowerTimeLimit) (lastBlock.numberOfLeadingZeros + 1)
        else if (lastBlock.generationTime > blockchain.upperTimeLimit) lastBlock.numberOfLeadingZeros - 1
        else lastBlock.numberOfLeadingZeros
    }

}
package blockchain

import java.util.Random

class BlockchainService {
    private val blockchain = Blockchain()
    private val lowerTimeLimit = 0
    private val upperTimeLimit = 5

    fun createBlockchain(length: Int) {
        val minerMax = 10
        val minerList = mutableListOf<Miner>()
        var minerNumber = 1
        val messageList = mutableListOf<String>()
        val messengerList = listOf(
            Messenger("Tom", messageList),
            Messenger("Sarah", messageList),
            Messenger("Jim", messageList),
            Messenger("Andrew", messageList),
            Messenger("Nick", messageList),
        )

        while (true) {
            val activeMiners = minerList.count { it.isAlive }

            if (activeMiners <= minerMax) {

                val messenger = messengerList[Random().nextInt(messengerList.size)]
                if (messenger.state == Thread.State.NEW) messenger.start()

                val increment = if (blockchain.isEmpty()) 0
                else if (blockchain.last().generationTime <= lowerTimeLimit) 1
                else if (blockchain.last().generationTime >= upperTimeLimit) -1
                else 0

                val miner = Miner(
                    "# $minerNumber",
                    blockchain,
                    if (blockchain.isEmpty()) "0" else blockchain.last().hash,
                    if (blockchain.isEmpty()) 0 else blockchain.last().numberOfLeadingZeros + increment,
                    copyAndClearMessageList(messageList)
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

    @Synchronized private fun copyAndClearMessageList(messageList: MutableList<String>): List<String> {
        val copy = messageList.toList()
        messageList.clear()
        return copy
    }

    fun print() {
        val result = mutableListOf<Block>()
        result.addAll(blockchain.getBlocks())
        result.forEach { printBlock(it) }
    }

    private fun printBlock(block: Block) {
        val builder = StringBuilder("Block:\n")
        builder.append("Created by miner ${block.minerName}\n")
        builder.append("Id: ${block.id}\n")
        builder.append("Timestamp: ${block.timeStamp}\n")
        builder.append("Magic number: ${block.magicNumber}\n")
        builder.append("Hash of the previous block:\n")
        builder.append("${block.hashPrevious}\n")
        builder.append("Hash of the block:\n")
        builder.append("${block.hash}\n")
        builder.append("Block data: ${if (block.data.isEmpty()) "no messages" else ""}\n")
        if (block.data.isNotEmpty())
            builder.append("${block.data.joinToString("\n")}\n")
        builder.append("Block was generating for ${block.generationTime} seconds\n")
        builder.append(if (block.generationTime <= lowerTimeLimit) "N increased to ${block.numberOfLeadingZeros + 1}" else if (block.generationTime >= upperTimeLimit) "N was decreased by 1" else "N stays the same")
        builder.append("\n")

        println(builder)
    }

}
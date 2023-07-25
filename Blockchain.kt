package blockchain

import java.security.PrivateKey

class Blockchain(private val length: Int) {
    private var lastBlock: Block? = null
    val upperTimeLimit = 2
    val maxNumberOfLeadingZeros = 4
    private val initialValue = 100
    private val blockReward = 100
    private var isMined = true

    val userList: List<User> = listOf(
        User(1, "miner1", null, true),
        User(2, "miner2", null, true),
        User(3, "miner3", null, true),
        User(4, "miner4", null, true),
        User(5, "miner5", null, true),
        User(6, "miner6", null, true),
        User(7, "miner7", null, true),
        User(8, "miner8", null, true),
        User(9, "miner9", null, true),
        User(10, "Alice", null, false),
        User(11, "John", null, false),
        User(12, "Mike", null, false),
        User(13, "CarShop", null, false),
        User(14, "CarPartsShop", null, false),
        User(15, "FastFood", null, false),
        User(16, "ShoesShop", null, false),
        User(17, "Library", null, false),
        User(18, "Travel", null, false),
        User(19, "Fitness", null, false),
        User(20, "Church", null, false),
    )

    val usernamePrivateKeyMap = mutableMapOf<String, PrivateKey>()
    private val minerList = mutableListOf<Miner>()

    init {
        for (user in userList) {
            val keyPair = BlockchainAuthentication.createKeyPair()
            usernamePrivateKeyMap[user.name] = keyPair.private
            user.publicKey = keyPair.public
            if (user.isMiner) minerList.add(Miner(user, this))
        }
    }


    fun start() {
        while ((lastBlock?.size() ?: 0) < length) {
            Thread.sleep(100)
            if (isMined) {
                mineBlock()
            }
        }
    }

    private fun mineBlock() {
        isMined = false
        minerList.clear()
        for (user in userList) {
            if (user.isMiner) minerList.add(Miner(user, this))
        }
        minerList.forEach { it.start() }
    }

    @Synchronized
    fun append(block: Block) {
        if (
            block.isValid() &&
            isValidPreviousBlockHash(block) &&
            isValidData(block)

        ) {
            this.lastBlock = block
            printBlock(block)
            stopMining()
        }
    }

    @Synchronized
    private fun isValidData(block: Block): Boolean {
        val allTransactions = block.data.getAllTransactions()
        if (allTransactions.isEmpty()) return true
        if (isNotValidIdSequence(allTransactions)) return false
        for (transaction in allTransactions) {
            if (!BlockchainAuthentication.verifyTransaction(transaction)) return false
        }
        return true
    }

    @Synchronized
    private fun isNotValidIdSequence(transactions: MutableList<Transaction>): Boolean {
        if (transactions.isEmpty() || transactions.size == 1) return false
        for (i in 1..transactions.lastIndex) {
            if (transactions[i] != transactions[i - 1]) return true
        }
        return false
    }

    private fun isValidPreviousBlockHash(block: Block) = (lastBlock?.hash ?: "0") == (block.previousBlock?.hash ?: "0")

    private fun stopMining() {
        minerList.forEach {
            it.stopMining()
        }
        isMined = true
    }

    fun getLastBock() = lastBlock

    private fun printBlock(block: Block) {
        val builder = StringBuilder("Block:\n")
        builder.append("Created by ${block.miner?.name}\n")
        builder.append("${block.miner?.name} gets 100 VC\n")
        builder.append("Id: ${block.id}\n")
        builder.append("Timestamp: ${block.timeStamp}\n")
        builder.append("Magic number: ${block.magicNumber}\n")
        builder.append("Hash of the previous block:\n")
        builder.append(
            if (block.previousBlock == null) "0\n"
            else "${block.previousBlock!!.hash}\n"
        )
        builder.append("Hash of the block:\n")
        builder.append("${block.hash}\n")
        builder.append("Block data:\n")
        builder.append(
            if (block.data.toString().isNotEmpty()) "${block.data}\n"
            else "No transactions\n"
        )
        builder.append("Block was generating for ${block.generationTime} seconds\n")
        builder.append(
            if (block.generationTime < upperTimeLimit && block.numberOfLeadingZeros > maxNumberOfLeadingZeros) "N increased to ${block.numberOfLeadingZeros + 1}"
            else if (block.generationTime > upperTimeLimit) "N was decreased by 1"
            else "N stays the same"
        )
        builder.append("\n")

        println(builder)
    }

    fun getValueByUser(user: User): Int {
        var block = lastBlock
        var value = initialValue
        while (block != null) {
            if (block.miner == user) value += blockReward
            value += block.data.getValueByUser(user)
            block = block.previousBlock
        }
        return value
    }

}

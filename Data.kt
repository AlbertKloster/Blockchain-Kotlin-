package blockchain

class Data(private val data: MutableList<Transaction> = mutableListOf()) {
    override fun toString(): String {
        return data.joinToString("\n")
    }

    fun add(transaction: Transaction) = data.add(transaction)

    fun getValueByUser(user: User): Int {
        val debit = data.filter { it.from == user }.sumOf { it.value }
        val credit = data.filter { it.to == user }.sumOf { it.value }
        return credit - debit
    }

    fun getLastTransactionId() = if (data.isEmpty()) 0L else data.last().id

    fun getAllTransactions() = data

}

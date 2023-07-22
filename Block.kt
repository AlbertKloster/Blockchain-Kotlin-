package blockchain

data class Block(val id: Long, val timeStamp: Long, val hashPrevious: String, val hash: String)

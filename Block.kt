package blockchain

data class Block(val minerName: String, val id: Long, val timeStamp: Long, var magicNumber: Int, val hashPrevious: String, var hash: String = "", var generationTime: Long = 0L, var numberOfLeadingZeros: Int = 0)

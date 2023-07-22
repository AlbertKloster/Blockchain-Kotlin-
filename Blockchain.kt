package blockchain

data class Blockchain(val blocks: MutableList<Block> = mutableListOf()) {
    fun isEmpty() = blocks.isEmpty()
    fun last() = blocks.last()
    fun add(block: Block) = blocks.add(block)
}

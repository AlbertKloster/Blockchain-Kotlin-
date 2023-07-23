package blockchain

data class Blockchain(var initialNumberOfLeadingZeros: Int) {
    private val blocks: MutableList<Block> = mutableListOf()
    private val cryptographer = Cryptographer()

    fun getBlocks() = blocks

    @Synchronized fun isEmpty() = blocks.isEmpty()

    @Synchronized fun last() = blocks.last()

    @Synchronized fun add(block: Block) {
        if (blocks.isEmpty() && block.hash == cryptographer.getHash(block) ||
            last().hash == block.hashPrevious && block.hash == cryptographer.getHash(block)) {
            blocks.add(block)
        }
    }
}

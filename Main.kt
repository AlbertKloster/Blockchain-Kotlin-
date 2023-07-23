package blockchain

fun main() {
    val blockchainService = BlockchainService()
    blockchainService.createBlockchain(5)
    blockchainService.print()
}
package blockchain

class Transaction(val id: Long, val from: User, val to: User, val value: Int, var signature: ByteArray = byteArrayOf()) {
    override fun toString() = "${from.name} sent $value VC to ${to.name}"
    fun toByteArray() = (this.from.toString() + this.to.toString() + this.value.toString()).toByteArray()

}
package blockchain

import java.security.PublicKey

data class User(val id: Long, val name: String, var publicKey: PublicKey?, val isMiner: Boolean) {
    fun toByteArray() = (id.toString() + name).toByteArray()
}

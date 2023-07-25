package blockchain

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Signature

const val KEYPAIR_ALGORITHM = "RSA"
const val KEY_SIZE = 2048
const val SIGNATURE_ALGORITHM = "SHA256withRSA"
class BlockchainAuthentication {

    companion object {
        fun createKeyPair(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM)
            keyPairGenerator.initialize(KEY_SIZE)
            return keyPairGenerator.genKeyPair()
        }

        fun signUser(user: User, privateKey: PrivateKey): ByteArray {
            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            signature.initSign(privateKey)
            signature.update(user.toByteArray())
            return signature.sign()
        }

        fun signTransaction(transaction: Transaction, privateKey: PrivateKey): ByteArray {
            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            signature.initSign(privateKey)
            signature.update(transaction.toByteArray())
            return signature.sign()
        }

        fun verifyTransaction(transaction: Transaction): Boolean {
            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            signature.initVerify(transaction.from.publicKey!!)
            signature.update(transaction.toByteArray())
            return signature.verify(transaction.signature)
        }
    }
}
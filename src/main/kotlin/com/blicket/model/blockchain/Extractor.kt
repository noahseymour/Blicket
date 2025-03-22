package com.blicket.model.blockchain

class Extractor(
    private val address: Address,
    private val privateKey: Int,
    private val username: String
) {
    fun extractMessages(blockchain: Blockchain) {
        for (transaction in blockchain) {
            val (id, timestamp, sender, signature, message) = parseTransaction(transaction)
            if (verifyTransaction(id, timestamp, sender, signature, message)) {}
        }
    }

    private fun parseTransaction(transaction: String): Transaction = TODO()

    private fun verifyTransaction(id: String, timestamp: String, sender: String, signature: String, message: String): Boolean = TODO()

    override fun toString(): String = "Extractor object for $username"
}
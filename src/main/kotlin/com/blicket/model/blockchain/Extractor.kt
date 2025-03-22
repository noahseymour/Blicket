package com.blicket.model.blockchain

class Extractor(
    private val address: Address,
    private val privateKey: Int,
    private val username: String
) {
    fun extract(blockchain: Blockchain): String {
        val messages = extractMessages(blockchain).joinToString(",") { it.toString() }
        return "{'messages': [$messages]}"
    }

    private fun getLatestTick() {
        "$BASE_URL/latestTick"
    }

    private fun extractMessages(blockchain: Blockchain): List<Message> {
        val messages: MutableList<Message> = mutableListOf()
        for (transaction in blockchain) {
            val (id, timestamp, sender, signature, message) = Transaction(transaction)
            if (verifyTransaction(id, timestamp, sender, signature, message)) {
                val decryptAttempt = decrypt(message)
                if (verifyDecrypt(decryptAttempt)) {
                    messages.add(Message(
                        username,
                        sender,
                        timestamp,
                        decryptAttempt
                    ))
                }
            }
        }
        return messages.toList()
    }

    private fun verifyTransaction(
        id: String,
        timestamp: String,
        sender: String,
        signature: String,
        message: String
    ): Boolean {
        TODO()
    }

    private fun decrypt(message: String): String {
        TODO()
    }

    private fun verifyDecrypt(attempt: String): Boolean =
        (attempt.substring(CHECK_STRING_POSITIONS) == address)

    override fun toString(): String = "Extractor object for $username"
}
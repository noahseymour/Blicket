package com.blicket.model.blockchain

import kotlinx.serialization.json.*


class Extractor(
    private val receiverAddress: Address,
    private val privateKey: String,
) {
    suspend fun extractLatest(): String {
        val tick = getLatestTransactions()
        tick?.let {
            val messages = extractMessages(tick).joinToString(",") { it.toJSONString() }
            return "{'status':'success','messages': [$messages]}"
        }
        return "{'status':'empty'}"
    }

    private suspend fun getLatestTransactions(): Tick? {
        val transactionPage = makeGetRequest("$BASE_URL/v2/ticks/${latestTick()}/transactions")
        val transactionObject = Json.decodeFromString<Map<String, JsonArray>>(transactionPage)

        val transactions: List<JsonObject>? = transactionObject["transactions"]?.mapNotNull { it.jsonObject["transaction"]?.jsonObject }
        val destIds: List<JsonPrimitive?>? =  transactions?.map { it["destId"]?.jsonPrimitive }
        return destIds?.filterNotNull()?.filter { it.content == receiverAddress }
    }

    private fun extractMessages(tick: Tick): List<Message> {
        val messages: MutableList<Message> = mutableListOf()
        for (transaction in tick) {
            val (timestamp, sender, message, transactionID) = Transaction(transaction)
            val decryptedMessage = RSA.decryptFromBase64(message, privateKey)
            /**
             * Transaction objects are input from the blockchain (tick-chain)
             * These will always contain full metadata, without omissions,
             * The shallow objects can only occur if transactions are made manually, which
             * in this case they never will be.
             */
            messages.add(Message(
                receiver = receiverAddress,
                sender = sender,
                timestamp = timestamp,
                text = decryptedMessage,
                identifier = transactionID
            ))
        }
        return messages.toList()
    }
}
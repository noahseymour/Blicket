package com.blicket.model.blockchain

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.JsonPrimitive


class Extractor(
    private val address: Address,
    private val privateKey: String,
    private val username: String
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
        return transactionObject["transactions"]?.filter { (it.jsonObject["destId"] as JsonPrimitive).content == address } as JsonArray?
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
                username,
                sender,
                timestamp,
                decryptedMessage,
                transactionID
            ))
        }
        return messages.toList()
    }

    override fun toString(): String = "Extractor object for $username"
}
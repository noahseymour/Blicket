package com.blicket.model.blockchain

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.*

class Extractor(
    private val address: Address,
    private val privateKey: Int,
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
        val page: String = makeGetRequest("$BASE_URL/v1/latestTick")
        val latestTick = Json.decodeFromString<Map<String, JsonObject>>(page)["latestTick"]
        val transactionPage = makeGetRequest("$BASE_URL/v2/ticks/$latestTick/transactions")
        val transactionObject = Json.decodeFromString<Map<String, JsonArray>>(transactionPage)
        return transactionObject["transactions"]?.filter { (it.jsonObject["destId"] as JsonPrimitive).content == address } as JsonArray?
    }

    private suspend fun makeGetRequest(url: String): String {
        val client = HttpClient(CIO)
        return client.use {
            val response: HttpResponse = it.get(url)
            response.bodyAsText()
        }
    }

    private fun extractMessages(tick: Tick): List<Message> {
        val messages: MutableList<Message> = mutableListOf()
        for (transaction in tick) {
            val (timestamp, sender, message, transactionID) = Transaction(transaction)
            val decryptedMessage = decrypt(message)
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

    private fun decrypt(message: String): String {
        TODO()
    }

    private fun verifyDecrypt(attempt: String): Boolean =
        (attempt.substring(CHECK_STRING_POSITIONS) == address)

    override fun toString(): String = "Extractor object for $username"
}
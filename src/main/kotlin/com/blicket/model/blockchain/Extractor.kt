package com.blicket.model.blockchain

import com.fasterxml.jackson.databind.ser.Serializers.Base
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import kotlinx.serialization.json.JsonObject
import io.ktor.server.routing.*
import io.ktor.server.response.*

import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

class Extractor(
    private val address: Address,
    private val privateKey: Int,
    private val username: String
) {
    suspend fun extractLatest(): String {
        val tick = getLatestTransactions()
        val messages = extractMessages(tick).joinToString(",") { it.toString() }
        return "{'messages': [$messages]}"
    }

    private suspend fun getLatestTransactions(): Tick {
        val page: String = makeGetRequest("$BASE_URL/v1/latestTick")
        val latestTick = Json.decodeFromString<Map<String, JsonObject>>(page)["latestTick"]
        val transactionPage = makeGetRequest("$BASE_URL/v2/ticks/$latestTick/transactions")
        val transactionObject = Json.decodeFromString<Map<String, JsonArray>>(transactionPage)
        return transactionObject["transactions"]!!
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
            val decryptAttempt = decrypt(message)
            if (verifyDecrypt(decryptAttempt)) {
                messages.add(Message(
                    username,
                    sender,
                    timestamp,
                    decryptAttempt,
                    transactionID
                ))
            }
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
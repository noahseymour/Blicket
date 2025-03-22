package com.blicket.model.blockchain

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

class Transaction(
    private val transactionObject: JsonElement
) {
    private val timestamp: String = (transactionObject.jsonObject["timestamp"] as JsonPrimitive).content
    private val sender: String = transactionInfo("sourceId")
    private val message: String = transactionInfo("inputHex")
    private val transactionID: String = transactionInfo("txId")

    private fun transactionInfo(key: String) =
        (transactionObject.jsonObject["transaction"]!!.jsonObject[key] as JsonPrimitive).content

    operator fun component1() = timestamp

    operator fun component2() = sender

    operator fun component3() = message

    operator fun component4() = transactionID
}
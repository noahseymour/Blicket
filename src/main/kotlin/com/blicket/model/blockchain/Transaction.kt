package com.blicket.model.blockchain

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

class Transaction(
    private val transactionObject: JsonElement,
) {
    private val timestamp: String
    private val sender: String
    private val recipient: String
    private val message: String
    private val transactionID: String
    private val paymentAmount: String

    init {
        val transactionInfo: (String) -> String = { key: String ->
            (transactionObject.jsonObject["transaction"]!!.jsonObject[key] as JsonPrimitive).content
        }
        timestamp = (transactionObject.jsonObject["timestamp"] as JsonPrimitive).content
        sender = transactionInfo("sourceId")
        recipient = transactionInfo("destId")
        message = transactionInfo("inputHex")
        transactionID = transactionInfo("txId")
        paymentAmount = transactionInfo("amount")
    }

    operator fun component1() = timestamp

    operator fun component2() = sender

    operator fun component3() = message

    operator fun component4() = transactionID

    operator fun component5() = paymentAmount
}
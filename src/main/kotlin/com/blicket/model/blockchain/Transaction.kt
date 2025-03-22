package com.blicket.model.blockchain

import at.qubic.api.domain.MessageType
import at.qubic.api.domain.QubicHeader
import at.qubic.api.domain.QubicMessage
import at.qubic.api.domain.std.SignedTransaction
import at.qubic.api.service.TransactionService
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.random.Random

class Transaction{
    private val timestamp: String?
    private val sender: String
    private val recipient: String
    private val message: String
    private val transactionID: String?

    //Constructor for transaction to process
    constructor(transactionObject: JsonElement) {
        val transactionInfo = { key: String ->
            (transactionObject.jsonObject["transaction"]!!.jsonObject[key] as JsonPrimitive).content
        }

        timestamp = (transactionObject.jsonObject["timestamp"] as JsonPrimitive).content
        sender = transactionInfo("sourceId")
        recipient = transactionInfo("destId")
        message = transactionInfo("inputHex")
        transactionID = transactionInfo("txId")
    }

    //Constructor for transaction to broadcast
    constructor(
        sender: String,
        recipient: String,
        message: String,
    ) {
        this.timestamp = null
        this.sender = sender
        this.recipient = recipient
        this.message = message
        this.transactionID = null
    }

    operator fun component1() = timestamp ?: throw UnsupportedOperationException("No timestamp specified")

    operator fun component2() = sender

    operator fun component3() = message

    operator fun component4() = transactionID ?: throw UnsupportedOperationException("No identifier specified")

    suspend fun broadcast(): String {
        require(timestamp == null)
        val result = makePostRequest("$BASE_URL/v1/broadcast-transaction", generateEncodedTransaction())
        return if (result.status == HttpStatusCode.OK) "SUCCESS" else result.toString()
    }

    private suspend fun generateEncodedTransaction(amount: Currency = 0): ByteArray {
        val transactionService = TransactionService()

        val transaction: SignedTransaction = transactionService.createTransactionWithNonceK(
            Integer.parseInt(latestTick()) + 5,
            Random.nextBytes(RANDOM_SEED_LENGTH),
            sender,
            recipient,
            amount
        )

        val rawTransaction: ByteArray = transaction.toBytes()

        val qubicHeader: QubicHeader = QubicHeader.builder()
            .type(MessageType.BROADCAST_TRANSACTION)
            .dejavu(0)
            .size(TRANSACTION_HEADER_SIZE)
            .build()

        val encryptedMessage = RSA.encryptToBase64(message, recipient)
        val qubicMessage: QubicMessage = QubicMessage.builder()
            .header(qubicHeader)
            .payload(encryptedMessage.toByteArray() + rawTransaction) // TODO SHADY AS FUCK
            .build()

        return qubicMessage.toBytes()
    }
}
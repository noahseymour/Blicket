package com.blicket.model.blockchain

import at.qubic.api.domain.MessageType
import at.qubic.api.domain.QubicHeader
import at.qubic.api.domain.QubicMessage
import at.qubic.api.domain.std.SignedTransaction
import at.qubic.api.service.TransactionService
import io.ktor.http.HttpStatusCode
import java.security.SecureRandom
import kotlin.random.Random

class TransactionPost(
    private val recipient: ByteArray,
    private val sender: ByteArray,
    private val message: String,
    private val paymentAmount: Currency = 0
) {

    suspend fun broadcast(): String {
        val result = makePostRequest("$BASE_URL/v1/broadcast-transaction", generateEncodedTransaction())
        return if (result.status == HttpStatusCode.OK) "SUCCESS" else result.toString()
    }

    private suspend fun generateEncodedTransaction(): ByteArray {
        val transactionService = TransactionService()

        val transaction: SignedTransaction = transactionService.createTransactionWithNonceK(
            Integer.parseInt(latestTick()) + TICK_OFFSET,
            ByteArray(RANDOM_SEED_LENGTH).apply { Random.nextBytes(this) },
            getIdentityFromPublicKey(sender, false),
            getIdentityFromPublicKey(recipient, false),
            paymentAmount,
            0,
            FourQCrypto.encrypt(message.toByteArray(), FourQPublicKey(recipient))
        )
        println(transaction)

        return transaction.toBytes()
    }
}
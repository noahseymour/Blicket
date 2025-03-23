package com.blicket.model.blockchain

class GroupTransactionPost(
    private val recipients: List<ByteArray>,
    private val sender: ByteArray,
    private val message: String,
    private val paymentAmounts: List<Currency> = (1..recipients.size).map { 0 }
) {

    init { require(paymentAmounts.size == recipients.size) }

    suspend fun batchBroadcast(): List<String> {
        val individualTransactions: List<TransactionPost> = recipients.zip(paymentAmounts).map { (receiver, amount) ->
            TransactionPost(receiver, sender, message, amount)
        }
        return individualTransactions.map { it.broadcast() }
    }
}
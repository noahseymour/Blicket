package com.blicket.model.blockchain

class GroupTransactionPost(
    private val recipients: List<ByteArray>,
    private val sender: ByteArray,
    private val message: String,
    private val paymentAmount: Currency = 0
) {
    suspend fun batchBroadcast(): List<String> {
        val individualTransactions: List<TransactionPost> = recipients.map { TransactionPost(it, sender, message, paymentAmount) }
        return individualTransactions.map { it.broadcast() }
    }
}
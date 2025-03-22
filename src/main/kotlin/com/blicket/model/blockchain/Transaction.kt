package com.blicket.model.blockchain

class Transaction(
    transactionString: String
) {
    private val id: String = transactionString.substring(ID_POSITIONS)
    private val timestamp: String = transactionString.substring(TIMESTAMP_POSITIONS)
    private val sender: String = transactionString.substring(SENDER_POSITIONS)
    private val signature: String = transactionString.substring(SIGNATURE_POSITIONS)
    private val message: String = transactionString.substring(MESSAGE_POSITIONS)

    operator fun component1() = id

    operator fun component2() = timestamp

    operator fun component3() = sender

    operator fun component4() = signature

    operator fun component5() = message
}
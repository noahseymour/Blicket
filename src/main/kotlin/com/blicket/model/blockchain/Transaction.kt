package com.blicket.model.blockchain

class Transaction(
    transactionString: String
) {
    private lateinit var id: String
    private lateinit var timestamp: String
    private lateinit var sender: String
    private lateinit var signature: String
    private lateinit var message: String

    init {
        val offsets = listOf(
            ID_LENGTH,
            TIMESTAMP_LENGTH,
            SENDER_LENGTH,
            SIGNATURE_LENGTH,
            MESSAGE_LENGTH
        )
        require(offsets.sum() == transactionString.length)

        val values: MutableList<String> = mutableListOf()
        var position = 0
        for (offset in offsets) {
            values.add(transactionString.slice(position..position + offset))
            position += offset
        }

        val (id, timestamp, sender, signature, message) = values
    }

    operator fun component1() = id

    operator fun component2() = timestamp

    operator fun component3() = sender

    operator fun component4() = signature

    operator fun component5() = message
}
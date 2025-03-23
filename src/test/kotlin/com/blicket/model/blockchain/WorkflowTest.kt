package com.blicket.model.blockchain

import kotlinx.coroutines.runBlocking
import org.junit.Test

class WorkflowTest {
    @Test
    fun testSendAndReadMessage() {
        val senderPair = RSA.generateKeyPair()
        val receiverPair = RSA.generateKeyPair()

        val senderTransaction = Transaction(
            sender = senderPair.second.toString(Charsets.UTF_8),     //TODO maybe problems with toString()
            recipient = receiverPair.second.toString(Charsets.UTF_8),
            "Hello World"
        )
        val receiverExtractor = Extractor(
            receiverAddress = receiverPair.second.toString(),
            privateKey = receiverPair.first.toString()
        )

        runBlocking {
            println(senderTransaction.broadcast())
            println(receiverExtractor.extractLatest())
        }
    }
}
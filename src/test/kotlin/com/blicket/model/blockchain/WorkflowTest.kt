package com.blicket.model.blockchain

import kotlinx.coroutines.runBlocking
import org.junit.Test

class WorkflowTest {
    @Test
    fun testSendAndReadMessage() {
        val senderPair = RSA.generateKeyPair()
        val receiverPair = RSA.generateKeyPair()

        val senderTransaction = Transaction(
            sender = senderPair.public.toString(),     //TODO maybe problems with toString()
            recipient = receiverPair.public.toString(),
            "Hello World"
        )
        val receiverExtractor = Extractor(
            receiverAddress = receiverPair.public.toString(),
            privateKey = receiverPair.private.toString()
        )

        runBlocking {
            println(senderTransaction.broadcast())
            println(receiverExtractor.extractLatest())
        }
    }
}
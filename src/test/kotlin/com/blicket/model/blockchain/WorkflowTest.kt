package com.blicket.model.blockchain

import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class WorkflowTest {
    @Test
    fun testSendAndReadMessage() {
        val senderPair = generateKeyPair()
        val receiverPair = generateKeyPair()

        val senderIdentity = getIdentityFromPublicKey(senderPair.second, false)
        val recipientIdentity = getIdentityFromPublicKey(receiverPair.second, false)

        val senderTransaction = TransactionPost(
            sender = senderPair.second,
            recipient = receiverPair.second,
            message = "Hello World"
        )

        val receiverExtractor = Extractor(
            receiverAddress = recipientIdentity,
            privateKey = receiverPair.first
        )

        runBlocking {
            assertNotNull(senderTransaction.broadcast())
            println(receiverExtractor.extractLatest())
        }
    }
}

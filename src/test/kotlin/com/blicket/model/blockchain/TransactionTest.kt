package com.blicket.model.blockchain

import kotlin.test.Test
import kotlin.test.fail

class TransactionTest {
    @Test
    fun broadcastDestructuringTest() {
        val testTransaction = Transaction(
            testSender,
            testRecipient,
            testMessage
        )
        try {
            val (_, _, _, _) = testTransaction
            fail("Should have thrown UnsupportedOperationException")
        } catch (_: UnsupportedOperationException) {
            // Good: Test passed
        }
    }

    @Test
    fun broadcastNonNullTest() {
        val testTransaction = Transaction(
            testSender,
            testRecipient,
            testMessage
        )

    }
}
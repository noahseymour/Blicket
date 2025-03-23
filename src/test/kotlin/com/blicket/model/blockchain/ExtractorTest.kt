package com.blicket.model.blockchain

import kotlinx.coroutines.runBlocking
import org.junit.Test

@Deprecated("Deprecated test")
class ExtractorTest {
    @Test
    fun extractLatestNode() {
        val extractorTest = Extractor(
            receiverAddress = testRecipient,
            privateKey = testSender.toByteArray()
        )

        runBlocking {
            val returned: String = extractorTest.extractLatest()
            print(returned)
        }
    }

    @Test
    fun latestTickTest() {
        runBlocking {
            println(latestTick())
        }
    }
}

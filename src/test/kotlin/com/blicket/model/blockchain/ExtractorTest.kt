package com.blicket.model.blockchain

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.assertNotNull

class ExtractorTest {

    private val sourceId: String = "ysagjasrgjwougwjoqr32901wrhw4y5aeh24"
    private val destinationID: String = "ysar1433ethrgwrgrwgw514hrew4y5aeh27"

    @Test
    fun extractLatestNode() {
        val extractorTest: Extractor = Extractor(
            receiverAddress = destinationID,
            privateKey = sourceId
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
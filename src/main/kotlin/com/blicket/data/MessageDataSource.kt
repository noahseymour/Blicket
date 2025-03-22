package com.blicket.data

import com.blicket.model.blockchain.Message

interface MessageDataSource {
    suspend fun getAllMessagesStored(): List<Message>

    suspend fun insertMessage(message: Message)
}
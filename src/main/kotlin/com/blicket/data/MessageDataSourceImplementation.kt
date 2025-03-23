package com.blicket.data

import com.blicket.model.blockchain.Message
import org.litote.kmongo.coroutine.CoroutineDatabase

class MessageDataSourceImplementation(
    private val db: CoroutineDatabase,
): MessageDataSource {

    private val messages = db.getCollection<Message>()

    override suspend fun getAllMessagesStored(): List<Message> = messages
            .find()
            .descendingSort(Message::timestamp)
            .toList()

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}
package com.blicket.model.blockchain

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Message (
    val receiver: Address,
    val sender: Address,
    val timestamp: Time,
    val text: String,
    @BsonId
    val identifier: String,
)
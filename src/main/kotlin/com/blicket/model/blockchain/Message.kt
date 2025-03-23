package com.blicket.model.blockchain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
open class Message (
    val receiver: Address,
    val sender: Address,
    val timestamp: Time,
    val text: String,
    @BsonId
    val identifier: String,
    val amount: Currency
) {
    open fun toJSONString(): String = Json.encodeToString(this)
}
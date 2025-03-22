package com.blicket.model.blockchain

import kotlinx.serialization.Serializable

@Serializable
data class Message (
    val receiver: Address,
    val sender: Address,
    val timestamp: Time,
    val text: String,
    val identifier: String,
)
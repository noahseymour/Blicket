package com.blicket.model.blockchain

data class Message (
    private val receiver: Address,
    private val sender: Address,
    private val timestamp: Time,
    private val text: String,
    private val identifier: String,
)
package com.blicket.model.blockchain

import kotlinx.serialization.json.JsonArray

typealias Address = String
typealias Tick = JsonArray
typealias Time = String
typealias JSON = String

const val TRANSACTION_LENGTH = 1024
val ID_POSITIONS = 0..15
val TIMESTAMP_POSITIONS = 16..31
val SENDER_POSITIONS = 32..63
val SIGNATURE_POSITIONS = 64..79
val MESSAGE_POSITIONS = 80..<TRANSACTION_LENGTH

val CHECK_STRING_POSITIONS = 0..15

const val BASE_URL = "https://rpc.qubic.org"
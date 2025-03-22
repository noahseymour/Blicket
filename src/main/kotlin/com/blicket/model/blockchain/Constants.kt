package com.blicket.model.blockchain

import kotlinx.serialization.json.JsonArray

typealias Address = String
typealias Tick = JsonArray
typealias Time = String
typealias JSON = String

val CHECK_STRING_POSITIONS = 0..15

const val BASE_URL = "https://rpc.qubic.org"
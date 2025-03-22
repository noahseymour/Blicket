package com.blicket.model.blockchain

import kotlinx.serialization.json.JsonArray

typealias Address = String
typealias Tick = JsonArray
typealias Time = String
typealias JSON = String
typealias Currency = Long

const val BASE_URL = "https://testnet-rpc.qubic.org"

const val RANDOM_SEED_LENGTH = 128

const val TRANSACTION_HEADER_SIZE = 152
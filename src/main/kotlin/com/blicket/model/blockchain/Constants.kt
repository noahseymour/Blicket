package com.blicket.model.blockchain

import kotlinx.serialization.json.JsonElement

typealias Address = String
typealias Tick = List<JsonElement>
typealias Time = String
typealias JSON = String
typealias Currency = Long

const val BASE_URL = "https://testnet-rpc.qubic.org"

const val RANDOM_SEED_LENGTH = 64

const val TRANSACTION_HEADER_SIZE = 152
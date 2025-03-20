package com.blicket.model.blockchain

interface JSONRepresentable {
    fun buildFromJSON(json: JSON)

    fun createJSONFrom(): JSON
}

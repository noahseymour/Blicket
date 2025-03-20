package com.blicket.model.blockchain

class Ledger : JSONRepresentable {
    override fun buildFromJSON(json: JSON) = TODO()

    override fun createJSONFrom(): JSON = TODO()

    override fun toString(): String = createJSONFrom()
}
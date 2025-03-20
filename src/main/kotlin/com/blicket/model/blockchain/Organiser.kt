package com.blicket.model.blockchain

class Organiser(
    val name: String
) : JSONRepresentable {
    override fun buildFromJSON(json: JSON) = TODO()

    override fun createJSONFrom(): JSON = TODO()

    override fun toString(): String = "Organiser: $name"
}
package blicket

interface JSONRepresentable {
    fun buildFromJSON(json: JSON)

    fun createJSONFrom(): JSON
}

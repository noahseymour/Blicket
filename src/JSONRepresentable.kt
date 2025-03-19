package blicket

typealias JSON = String

interface JSONRepresentable {
    fun buildFromJSON(json: JSON)

    fun createJSONFrom(): JSON
}

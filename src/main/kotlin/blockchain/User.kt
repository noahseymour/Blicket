package blockchain

class User(
    val name: String
) : JSONRepresentable {
    override fun buildFromJSON(json: JSON) = TODO()

    override fun createJSONFrom(): JSON = TODO()

    override fun toString(): String = "User: $name"
}
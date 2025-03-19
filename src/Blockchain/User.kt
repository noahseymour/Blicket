package blicket

class User(
    val name: String
) : JSONRepresentable {
    override fun buildFromJSON(json: JSON) = TODO()

    override fun buildFromJSON(json: JSON) = TODO()

    override fun toString(): String = "User: $name"
}
package blicket

class Ticket(
    val name: String,
    val event: Event,
) : JSONRepresentable {
    override fun buildFromJSON(json: JSON) = TODO()

    override fun createJSONFrom(json: JSON) = TODO()
}
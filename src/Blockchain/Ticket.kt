package blicket

class Ticket(
    val name: String,
    val event: Event,
    val seatNumber: SeatNumber
) : JSONRepresentable {
    override fun buildFromJSON(json: JSON) = TODO()

    override fun createJSONFrom(): JSON = TODO()

    override fun toString(): String = "Ticket for $event for $name at seat number $seatNumber"
}
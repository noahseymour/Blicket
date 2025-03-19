package blockchain

class Event(
    val name: String
) : JSONRepresentable {
    private val tickets: MutableList<Ticket> = mutableListOf()

    override fun buildFromJSON(json: JSON) = TODO()

    override fun createJSONFrom(): JSON = TODO()

    override fun toString(): String = "Event: $name"

    private fun getNextAvailableSeat(): SeatNumber = TODO()

    fun createNewTicket(user: User): Ticket {
        val ticket = Ticket("Ticket for $name", this, getNextAvailableSeat())
        tickets.add(ticket)
        return ticket
    }
}
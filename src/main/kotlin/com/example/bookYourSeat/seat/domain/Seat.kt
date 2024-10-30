import com.example.book_your_seat.concert.domain.Concert
import jakarta.persistence.*

@Entity
class Seat(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    val concert: Concert,

    @Column(name = "seat_number", nullable = false)
    val seatNumber: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    var id: Long? = null
        private set

    @Enumerated(EnumType.STRING)
    val zone: Zone = Zone.setZone(seatNumber)

    var isSold: Boolean = false
        private set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation? = null
        private set

    init {
        concert.addSeat(this)
    }

    fun selectSeat() {
        isSold = true
    }

    fun assignReservation(reservation: Reservation) {
        this.reservation = reservation
        reservation.addSeat(this)
    }

    fun releaseSeat() {
        isSold = false
        this.reservation = null
    }
}

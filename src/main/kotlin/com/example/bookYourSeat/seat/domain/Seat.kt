import com.example.bookYourSeat.concert.domain.Concert
import com.example.bookYourSeat.reservation.domain.Reservation
import com.example.book_your_seat.seat.domain.Zone
import jakarta.persistence.*

@Entity
class Seat(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    var id: Concert = null,

    @Column(name = "seat_number", nullable = false)
    val seatNumber: Int = 0,

    @Enumerated(EnumType.STRING)
    val zone: Zone = Zone.setZone(seatNumber),

    var isSold: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    val concert: Concert? = null,

    ) {

    init {
        concert!!.addSeat(this)
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

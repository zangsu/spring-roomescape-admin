package roomescape;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private List<Reservation> reservations = new ArrayList<>();
    private AtomicLong index = new AtomicLong(1);

    @GetMapping
    public ResponseEntity<List<FindReservationResponse>> getReservation() {
        List<FindReservationResponse> reservationsDtos = reservations.stream()
                .map(FindReservationResponse::of)
                .toList();
        return ResponseEntity.ok(reservationsDtos);
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(
            @RequestBody CreateReservationRequest createReservationRequest) {
        long id = index.getAndIncrement();
        Reservation newReservation = createReservationRequest.to(id);
        reservations.add(newReservation);
        return ResponseEntity.ok()
                .body(CreateReservationResponse.of(newReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .ifPresent(reservations::remove);
        return ResponseEntity.ok().build();
    }
}

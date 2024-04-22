package roomescape.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public class ReservationMemoryRepository implements ReservationRepository {

    private final List<Reservation> reservations;
    private final List<ReservationTime> reservationTimes;
    private final AtomicLong reservationIndex;

    public ReservationMemoryRepository(List<Reservation> reservations, List<ReservationTime> reservationTimes,
                                       AtomicLong reservationIndex) {
        this.reservations = reservations;
        this.reservationTimes = reservationTimes;
        this.reservationIndex = reservationIndex;
    }

    @Override
    public List<Reservation> findReservations() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findReservationById(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.isIdOf(id))
                .findFirst();
    }

    @Override
    public Long createReservation(ReservationCreateRequest reservationCreateRequest) {
        Long createdReservationId = reservationIndex.incrementAndGet();
        reservations.add(new Reservation(
                createdReservationId,
                reservationCreateRequest.name(),
                CustomDateTimeFormatter.getLocalDate(reservationCreateRequest.date()),
                findReservationTimeById(reservationCreateRequest.timeId())
        ));

        return createdReservationId;
    }

    @Override
    public void deleteReservationById(Long id) {
        reservations.removeIf(reservation -> reservation.isIdOf(id));
    }

    public ReservationTime findReservationTimeById(Long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.isIdOf(id))
                .findFirst()
                .get();
    }

    public void cleanUp() {
        reservations.clear();
    }
}

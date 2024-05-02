package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.model.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByTimeId(Long timeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);

    boolean existsByDateAndTime(LocalDate date, Long timeId);

    void deleteById(Long id);

    List<Reservation> findAllByThemeId(Long themeId);
}

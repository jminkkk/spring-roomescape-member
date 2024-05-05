package roomescape.reservationtime.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public CreateReservationTimeResponse createReservationTime(
            final CreateReservationTimeRequest createReservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRepository.save(
                createReservationTimeRequest.toReservationTime());
        return CreateReservationTimeResponse.from(reservationTime);
    }

    public List<FindReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(FindReservationTimeResponse::from)
                .toList();
    }

    public FindReservationTimeResponse getReservationTime(final Long id) {
        ReservationTime reservationTime = findReservationTime(id);
        return FindReservationTimeResponse.from(reservationTime);
    }

    private ReservationTime findReservationTime(final Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("조회하려는 예약 시간이 존재하지 않습니다."));
    }

    public void deleteById(final Long id) {
        validateExistReservationTime(id);
        validateReservationTimeUsage(id);

        reservationTimeRepository.deleteById(id);
    }

    private void validateExistReservationTime(final Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NoSuchElementException("삭제하려는 예약 시간이 존재하지 않습니다.");
        }
    }

    private void validateReservationTimeUsage(final Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalStateException("삭제하려는 시간을 사용 중인 예약이 존재합니다.");
        }
    }
}

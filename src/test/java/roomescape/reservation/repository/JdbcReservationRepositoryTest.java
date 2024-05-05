package roomescape.reservation.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.reservation.model.Reservation;
import roomescape.util.DummyDataFixture;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationRepositoryTest extends DummyDataFixture {

    @Autowired
    private JdbcReservationRepository jdbcReservationRepository;

    @Test
    @DisplayName("Reservation를 저장한 후 저장한 row의 id값을 반환한다.")
    void save() {
        Reservation newReservation = Reservation.of(null, "포비", LocalDate.of(2024, 4, 23),
                getReservationTimeById(1L), getThemeById(1L));
        assertThat(jdbcReservationRepository.save(newReservation))
                .isEqualTo(Reservation.of(14L, "포비", LocalDate.of(2024, 4, 23),
                        getReservationTimeById(1L), getThemeById(1L)));
    }

    @Test
    @DisplayName("Reservation 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        List<Reservation> preparedReservations = super.getPreparedReservations();
        assertThat(jdbcReservationRepository.findAll()).isEqualTo(preparedReservations);
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        Reservation reservation = super.getReservationById(1L);
        assertThat(jdbcReservationRepository.findById(1L)).isEqualTo(Optional.of(reservation));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(jdbcReservationRepository.findById(20L)).isNotPresent();
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 date, theme_id와 동일한 데이터를 조회한다.")
    void findAllByDateAndThemeId() {
        Reservation reservation = getReservationById(1L);
        assertThat(jdbcReservationRepository.findAllByDateAndThemeId(
                reservation.getDate(), reservation.getTheme().getId())).isEqualTo(List.of(reservation));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 date, theme_id와 없는 경우 빈 리스트를 반환한다.")
    void findAllByDateAndThemeId_Return_EmptyCollection() {
        assertThat(jdbcReservationRepository.findAllByDateAndThemeId(LocalDate.of(1000, 03, 03), 20L)).isEmpty();
    }

    @Test
    @DisplayName("해당 id값과 일치하는 예약이 존재하는 경우 참을 반환한다.")
    void existsById() {
        assertTrue(jdbcReservationRepository.existsById(1L));
    }

    @Test
    @DisplayName("해당 id값과 일치하는 예약이 존재하지 않은 경우 거짓을 반환한다.")
    void existsById_WhenNotExist() {
        assertFalse(jdbcReservationRepository.existsById(100L));
    }

    @Test
    @DisplayName("해당 예약 시간 id값과 일치하는 예약이 존재하는 경우 참을 반환한다.")
    void existsByTimeId() {
        assertTrue(jdbcReservationRepository.existsByTimeId(1L));
    }

    @Test
    @DisplayName("해당 예약 시간 id값과 일치하는 예약이 존재하지 않은 경우 거짓을 반환한다.")
    void existsByTimeId_WhenNotExist() {
        assertFalse(jdbcReservationRepository.existsByTimeId(7L));
    }

    @Test
    @DisplayName("해당 테마 id값과 일치하는 예약이 존재하는 경우 참을 반환한다.")
    void existsByThemeId() {
        assertTrue(jdbcReservationRepository.existsByThemeId(1L));
    }

    @Test
    @DisplayName("해당 테마 id값과 일치하는 예약이 존재하지 않은 경우 거짓을 반환한다.")
    void existsByThemeId_WhenNotExist() {
        assertFalse(jdbcReservationRepository.existsByThemeId(11L));
    }

    @Test
    @DisplayName("날짜와 시간 컬럼의 값이 동일할 경우 참을 반환한다.")
    void existsByDateAndTime_WhenSameName() {
        assertTrue(jdbcReservationRepository.existsByDateAndTimeAndTheme(
                getReservationById(1L).getDate(),
                getReservationById(1L).getId(),
                getReservationById(1L).getTheme().getId()));
    }

    @Test
    @DisplayName("날짜 또는 시간 중 하나라도 다를 경우 거짓을 반환한다.")
    void existsByDateAndTime_isFalse() {
        assertFalse(jdbcReservationRepository.existsByDateAndTimeAndTheme(
                getReservationById(3L).getDate(),
                getReservationById(1L).getId(),
                getReservationById(1L).getTheme().getId()));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        jdbcReservationRepository.deleteById(2L);
        assertThat(jdbcReservationRepository.findById(2L)).isNotPresent();
    }
}

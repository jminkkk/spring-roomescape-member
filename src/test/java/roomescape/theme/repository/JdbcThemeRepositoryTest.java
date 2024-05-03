package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.theme.model.Theme;
import roomescape.util.DummyDataFixture;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcThemeRepositoryTest extends DummyDataFixture {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("Theme 저장한 후 저장한 row의 id값을 반환한다.")
    void save() {
        Theme theme = new Theme(null, "마크", "노력함", "https://asd.cmom");
        assertThat(themeRepository.save(theme)).isEqualTo(new Theme(11L, "마크", "노력함", "https://asd.cmom"));
    }

    @Test
    @DisplayName("Theme 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        assertThat(themeRepository.findAll()).isEqualTo(super.getPreparedThemes());
    }

    @Test
    @DisplayName("Theme 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        assertThat(themeRepository.findById(1L)).isEqualTo(Optional.of(super.getThemeById(1L)));
    }

    @Test
    @DisplayName("Theme 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(themeRepository.findById(20L)).isNotPresent();
    }

    @Test
    @DisplayName("Theme 테이블에서 많이 예약된 테마 10개를 내림차순으로 가져온다.")
    void findOrderByReservation() {
        assertThat(themeRepository.findOrderByReservation())
                .isEqualTo(super.getPreparedThemes())
                .hasSize(10);
    }

    @Test
    @DisplayName("해당 테마 id값과 일치하는 예약이 존재하는 경우 참을 반환한다.")
    void existsById() {
        assertTrue(themeRepository.existsById(1L));
    }

    @Test
    @DisplayName("해당 테마 id값과 일치하는 예약이 존재하지 않은 경우 거짓을 반환한다.")
    void existsById_WhenNotExist() {
        assertFalse(themeRepository.existsById(12L));
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        themeRepository.deleteById(10L);
        assertThat(themeRepository.findById(10L)).isNotPresent();
    }
}

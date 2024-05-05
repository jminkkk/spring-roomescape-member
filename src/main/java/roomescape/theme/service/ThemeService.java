package roomescape.theme.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(@Qualifier("jdbcThemeRepository") final ThemeRepository themeRepository,
                        @Qualifier("jdbcReservationRepository") final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public CreateThemeResponse createTheme(CreateThemeRequest createThemeRequest) {
        Theme theme = themeRepository.save(createThemeRequest.toTheme());
        return CreateThemeResponse.from(theme);
    }

    public List<FindThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(FindThemeResponse::from)
                .toList();
    }

    public List<FindPopularThemesResponse> getPopularThemes() {
        return themeRepository.findOrderByReservation().stream()
                .map(FindPopularThemesResponse::from)
                .toList();
    }

    public void deleteById(final Long id) {
        validateExistTheme(id);
        validateThemeUsage(id);

        themeRepository.deleteById(id);
    }

    private void validateExistTheme(final Long id) {
        if (!themeRepository.existsById(id)) {
            throw new NoSuchElementException("삭제하려는 테마가 존재하지 않습니다.");
        }
    }

    private void validateThemeUsage(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalStateException("삭제하려는 테마를 사용 중인 예약이 존재합니다.");
        }
    }
}

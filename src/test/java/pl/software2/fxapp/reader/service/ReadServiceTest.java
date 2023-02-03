package pl.software2.fxapp.reader.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.software2.fxapp.model.Currency;
import pl.software2.fxapp.model.Price;
import pl.software2.fxapp.reader.repository.ReadOnlyPriceRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReadServiceTest {

  private static final Currency PLN = new Currency("PLN");
  private static final Currency EUR = new Currency("EUR");
  @Mock
  private ReadOnlyPriceRepository repository;

  @InjectMocks
  private ReadService testedService;

  @Test
  void shouldMapObjectProperly() {
    // given
    given(repository.loadOne(any(), any())).willReturn(Price.builder()
      .from(PLN)
      .to(EUR)
      .bidRate("1")
      .askRate("2")
      .build());

    // when
    final var dto = testedService.getRate(PLN, EUR);

    // then
    assertThat(dto.getFrom()).isEqualTo("PLN");
    assertThat(dto.getTo()).isEqualTo("EUR");
    assertThat(dto.getBid()).isEqualTo("1");
    assertThat(dto.getAsk()).isEqualTo("2");
  }

  @Test
  void shouldThrowAnExceptionInCaseThatExchangeRateWasNotFund() {
    // given
    given(repository.loadOne(any(), any())).willReturn(null);

    // when
    final var exception = assertThrows(ResponseStatusException.class, () -> testedService.getRate(PLN, EUR));

    // then
    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

  }
}
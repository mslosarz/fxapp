package pl.software2.fxapp.consumer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.software2.fxapp.consumer.model.CurrencyPair;
import pl.software2.fxapp.consumer.model.PriceEntry;
import pl.software2.fxapp.consumer.service.config.CommissionSetup;
import pl.software2.fxapp.model.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BuildPriceEntityServiceTest {

  @Mock
  private CommissionApplier commissionApplier;

  @Test
  void shouldBuildReadEntity() {
    // given
    final var originalAsk = new BigDecimal("0.22");
    final var originalBid = new BigDecimal("0.20");
    final var modifiedAsk = new BigDecimal("0.221");
    final var modifiedBid = new BigDecimal("0.199");
    given(commissionApplier.apply(eq(originalAsk), any())).willReturn(modifiedAsk);
    given(commissionApplier.apply(eq(originalBid), any())).willReturn(modifiedBid);

    // when
    final var processedPrice = testedService().buildPriceEntity(PriceEntry.builder()
      .currencies(new CurrencyPair(new Currency("PLN"), new Currency("EUR")))
      .ask(originalAsk)
      .bid(originalBid)
      .timestamp(LocalDateTime.of(2020,12,12,12,12))
      .build());

    // then
    assertThat(processedPrice).isNotNull();
    assertThat(processedPrice.getAskRate())
      .isNotNull()
      .isEqualTo("0.221000");
    assertThat(processedPrice.getBidRate())
      .isNotNull()
      .isEqualTo("0.199000");
    assertThat(processedPrice.getTimestamp())
      .isEqualTo(1607775120000L);

  }

  private BuildPriceEntityService testedService(){
    return new BuildPriceEntityService(commissionApplier, CommissionSetup.DEFAULT);
  }
}
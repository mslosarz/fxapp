package pl.software2.fxapp.consumer.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class CommissionApplierTest {

  private final CommissionApplier testedClass = new CommissionApplier();

  @ParameterizedTest(name = "{index} {0} with commission equals {1} should be equal {2}")
  @CsvSource({
    "1000,0.001,1001.0000",
    "1000,-0.001,999.0000",
    "1.555,-0.001,1.5534",
    "1.555,0.001,1.5566",
  })
  void shouldApplyCommission(String base, String commission, String result) {
    // given & when
    final var applied = testedClass.apply(new BigDecimal(base), new BigDecimal(commission));

    // then
    assertThat(applied.setScale(4, RoundingMode.HALF_DOWN).toPlainString()).isEqualTo(result);
  }


}
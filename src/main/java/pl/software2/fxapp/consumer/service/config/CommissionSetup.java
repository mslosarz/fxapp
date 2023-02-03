package pl.software2.fxapp.consumer.service.config;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CommissionSetup(
  BigDecimal bidCommission,
  RoundingMode bidCommissionRoundingMode,
  BigDecimal askCommission,
  RoundingMode askCommissionRoundingMode,
  int scaleUsedForFormatting
) {
  public static final CommissionSetup DEFAULT = new CommissionSetup(
    BigDecimal.valueOf(-1, 4),
    RoundingMode.FLOOR,
    BigDecimal.valueOf(1, 4),
    RoundingMode.CEILING,
    6
  );
}

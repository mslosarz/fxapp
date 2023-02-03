package pl.software2.fxapp.consumer.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class PriceEntry {
  private long id;
  private CurrencyPair currencies;
  private BigDecimal bid;
  private BigDecimal ask;
  private LocalDateTime timestamp;
}

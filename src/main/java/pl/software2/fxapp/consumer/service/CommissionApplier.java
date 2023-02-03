package pl.software2.fxapp.consumer.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CommissionApplier {

  public BigDecimal apply(BigDecimal rate, BigDecimal commissionValue) {
    return rate.add(rate.multiply(commissionValue));
  }
}

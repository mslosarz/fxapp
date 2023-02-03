package pl.software2.fxapp.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.software2.fxapp.consumer.model.PriceEntry;
import pl.software2.fxapp.consumer.service.config.CommissionSetup;
import pl.software2.fxapp.model.Price;

import java.time.ZoneOffset;

@Service
public class BuildPriceEntityService {

  private final CommissionApplier commissionApplier;
  private final CommissionSetup commissionSetup;

  @Autowired
  public BuildPriceEntityService(CommissionApplier commissionApplier, CommissionSetup commissionSetup) {
    this.commissionApplier = commissionApplier;
    this.commissionSetup = commissionSetup;
  }

  public Price buildPriceEntity(PriceEntry entry) {
    return Price.builder()
      .from(entry.getCurrencies().left())
      .to(entry.getCurrencies().right())
      .bidRate(commissionApplier.apply(entry.getBid(), commissionSetup.bidCommission())
        .setScale(commissionSetup.scaleUsedForFormatting(), commissionSetup.bidCommissionRoundingMode())
        .toPlainString())
      .askRate(commissionApplier.apply(entry.getAsk(), commissionSetup.askCommission())
        .setScale(commissionSetup.scaleUsedForFormatting(), commissionSetup.askCommissionRoundingMode())
        .toPlainString())
      .timestamp(entry.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli())
      .build();
  }
}

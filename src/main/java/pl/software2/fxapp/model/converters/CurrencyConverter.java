package pl.software2.fxapp.model.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import pl.software2.fxapp.model.Currency;

public class CurrencyConverter implements DynamoDBTypeConverter<String, Currency> {
  @Override
  public String convert(Currency currency) {
    return currency.code();
  }

  @Override
  public Currency unconvert(String s) {
    return new Currency(s);
  }
}

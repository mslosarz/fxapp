package pl.software2.fxapp.consumer.parser;

import org.springframework.stereotype.Component;
import pl.software2.fxapp.consumer.model.CurrencyPair;
import pl.software2.fxapp.consumer.model.PriceEntry;
import pl.software2.fxapp.model.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Arrays.stream;


// TODO: Probably better idea is to use some existing library
//  for CSV parsing
@Component
public class BasicMessageParser implements MessageParser {

  private final String NEW_LINE_INDICATOR = "\n";
  private final String ENTRY_SEPARATOR = ",";
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");

  @Override
  public List<PriceEntry> parseMessage(String message) {
    if (message == null || message.isBlank()) {
      return List.of();
    }
    // TODO: It'll be better to parse every line separately
    //  so in case that k of n are broken, rest would be parsed
    //  without any issue
    return stream(message.split(NEW_LINE_INDICATOR))
      .filter(line -> !line.isEmpty())
      .map(entry -> entry.split(ENTRY_SEPARATOR))
      .map(csv -> PriceEntry
        .builder()
        .id(Long.parseLong(csv[0].trim()))
        .currencies(getCurrenciesPair(csv[1].trim()))
        .bid(new BigDecimal(csv[2].trim()))
        .ask(new BigDecimal(csv[3].trim()))
        .timestamp(LocalDateTime.parse(csv[4].trim(), dateTimeFormatter))
        .build())
      .toList();
  }

  private static CurrencyPair getCurrenciesPair(String currencies) {
    final var currencyArray = currencies.split("/");
    return new CurrencyPair(
      new Currency(currencyArray[0]),
      new Currency(currencyArray[1])
    );
  }
}

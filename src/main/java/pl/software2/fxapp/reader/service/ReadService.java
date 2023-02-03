package pl.software2.fxapp.reader.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.software2.fxapp.model.Currency;
import pl.software2.fxapp.model.Price;
import pl.software2.fxapp.reader.model.ExchangeRateDto;
import pl.software2.fxapp.reader.repository.ReadOnlyPriceRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReadService {

  private final ReadOnlyPriceRepository repository;

  public ReadService(ReadOnlyPriceRepository repository) {
    this.repository = repository;
  }

  public Set<ExchangeRateDto> getAllFor(Currency currency) {
    return repository.getExchangesForCurrency(currency)
      .stream()
      .map(this::mapPrice)
      .collect(Collectors.toSet());
  }

  public ExchangeRateDto getRate(Currency from, Currency to) {
    final var price = repository.loadOne(from, to);
    if (price == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item was not found");
    }
    return mapPrice(price);
  }

  // should be moved to mapper
  private ExchangeRateDto mapPrice(Price price) {
    return ExchangeRateDto.builder()
      .from(price.getFrom().code())
      .to(price.getTo().code())
      .ask(price.getAskRate())
      .bid(price.getBidRate())
      .build();
  }
}

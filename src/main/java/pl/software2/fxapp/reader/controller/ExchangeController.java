package pl.software2.fxapp.reader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.software2.fxapp.model.Currency;
import pl.software2.fxapp.reader.model.ExchangeRateDto;
import pl.software2.fxapp.reader.service.ReadService;

import java.util.Set;

@RestController
@RequestMapping("/fx")
public class ExchangeController {

  private final ReadService readService;

  @Autowired
  public ExchangeController(ReadService readService) {
    this.readService = readService;
  }

  @GetMapping("/rates/{currencyCode}")
  public Set<ExchangeRateDto> getExchangeRatesFor(@PathVariable String currencyCode) {
    return readService.getAllFor(new Currency(currencyCode));
  }

  @GetMapping("/rates/{fromCurrency}/{toCurrency}")
  public ExchangeRateDto getExchangeRatesFor(@PathVariable String fromCurrency, @PathVariable String toCurrency) {
    return readService.getRate(new Currency(fromCurrency), new Currency(toCurrency));
  }
}

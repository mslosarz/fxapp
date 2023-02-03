package pl.software2.fxapp.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.software2.fxapp.consumer.parser.MessageParser;
import pl.software2.fxapp.consumer.repository.PriceRepository;
import pl.software2.fxapp.consumer.service.BuildPriceEntityService;

@Component
public class MarketExchangeListener implements MessageListener {

  private final MessageParser parser;
  private final BuildPriceEntityService service;
  private final PriceRepository priceRepository;

  @Autowired
  public MarketExchangeListener(
    MessageParser parser,
    BuildPriceEntityService service,
    PriceRepository priceRepository
  ) {
    this.parser = parser;
    this.service = service;
    this.priceRepository = priceRepository;
  }

  @Override
  public void onMessage(String msg) {
    // TODO: in the final solution probable errors during processing should not affect other entries
    //  here first issue will stop processing
    parser.parseMessage(msg)
      .stream()
      .map(service::buildPriceEntity)
      .forEach(priceRepository::update);
  }
}

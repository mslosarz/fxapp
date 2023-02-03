package pl.software2.fxapp.consumer.parser;

import pl.software2.fxapp.consumer.model.PriceEntry;

import java.util.List;

public interface MessageParser {
  List<PriceEntry> parseMessage(String message);
}

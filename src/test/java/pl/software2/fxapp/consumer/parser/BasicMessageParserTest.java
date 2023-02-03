package pl.software2.fxapp.consumer.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class BasicMessageParserTest {

  @Test
  void shouldParseOneEntryAndValidateValues() {
    // given
    var line = "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:001";

    // when
    final var result = new BasicMessageParser().parseMessage(line);

    // then
    assertThat(result).hasSize(1);
    final var entry = result.get(0);
    assertThat(entry.getId()).isEqualTo(109L);
    assertThat(entry.getCurrencies().left().code()).isEqualTo("GBP");
    assertThat(entry.getCurrencies().right().code()).isEqualTo("USD");
    assertThat(entry.getBid()).isEqualTo(BigDecimal.valueOf(12499, 4));
    assertThat(entry.getAsk()).isEqualTo(BigDecimal.valueOf(12561, 4));
    assertThat(entry.getTimestamp()).isEqualTo(LocalDateTime.of(2020, 6, 1, 12, 1,2, 1_000_000));
  }

  @ParameterizedTest(name = "{index} {0} should return empty list")
  @NullAndEmptySource
  void shouldParseEmptyAndNullValues(String input) {
    assertThat(new BasicMessageParser()
      .parseMessage(input))
      .isNotNull()
      .isEmpty();
  }

  @ParameterizedTest(name = "{index} {0} should be parsed into one entry")
  @ValueSource(strings = {
    "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001",
    "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110",
  })
  void shouldParseOneLiners(String input) {
    assertThat(new BasicMessageParser()
      .parseMessage(input))
      .isNotNull()
      .hasSize(1);
  }

  @ParameterizedTest(name = "{index} {0} should be parsed into two entries")
  @ValueSource(strings = {
    """
          106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
          107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
      """,
    """
          109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100
          110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110
      """
  })
  void shouldParseEntriesWithMoreThanOneEntry(String input) {
    assertThat(new BasicMessageParser()
      .parseMessage(input))
      .isNotNull()
      .hasSize(2);
  }
}
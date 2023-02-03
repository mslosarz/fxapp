package pl.software2.fxapp.model;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Currency(String code) {
  private static final Predicate<String> VALIDATION_PATTERN =
    Pattern.compile("^[A-Z]{3}$").asMatchPredicate();

  public Currency {
    if (code == null || code.length() != 3 || !VALIDATION_PATTERN.test(code)) {
      throw new IllegalArgumentException("Given string is not a currency code");
    }
  }
}

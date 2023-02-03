package pl.software2.fxapp.reader.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.software2.fxapp.model.Currency;
import pl.software2.fxapp.model.Price;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class ReadOnlyPriceRepository {
  private final IDynamoDBMapper mapper;

  @Autowired
  private ReadOnlyPriceRepository(IDynamoDBMapper mapper) {
    this.mapper = mapper;
  }

  public Set<Price> getExchangesForCurrency(Currency currency) {
    final var queryForBids = new DynamoDBQueryExpression<Price>()
      .withKeyConditionExpression("#from = :from")
      .withExpressionAttributeNames(Map.of("#from", "from"))
      .withExpressionAttributeValues(Map.of(":from", new AttributeValue(currency.code())));
    return new HashSet<>(mapper.query(Price.class, queryForBids));
  }

  public Price loadOne(Currency from, Currency to) {
    return mapper.load(Price.class, from, to);
  }

}

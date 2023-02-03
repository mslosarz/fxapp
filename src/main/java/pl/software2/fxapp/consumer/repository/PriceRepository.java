package pl.software2.fxapp.consumer.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.stereotype.Repository;
import pl.software2.fxapp.model.Price;

import java.util.Map;

@Repository
public class PriceRepository {

  private final IDynamoDBMapper dynamoDBMapper;

  public PriceRepository(IDynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public void update(Price price) {
    try {
      dynamoDBMapper.save(price, new DynamoDBSaveExpression()
        .withExpected(
          Map.of(
            "timestamp", new ExpectedAttributeValue()
              .withComparisonOperator(ComparisonOperator.LE)
              .withValue(new AttributeValue().withN("" + price.getTimestamp())),
            "from", new ExpectedAttributeValue(false)
          )
        )
        .withConditionalOperator(ConditionalOperator.OR)
      );
    } catch (ConditionalCheckFailedException skip) {
      // newest item is in the db
    }
  }
}

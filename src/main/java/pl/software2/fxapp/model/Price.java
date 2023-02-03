package pl.software2.fxapp.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.*;
import pl.software2.fxapp.model.converters.CurrencyConverter;

@DynamoDBTable(tableName = "FXPrices")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Price {
  @DynamoDBHashKey
  @DynamoDBTypeConverted(converter = CurrencyConverter.class)
  private Currency from;
  @DynamoDBRangeKey
  @DynamoDBTypeConverted(converter = CurrencyConverter.class)
  private Currency to;
  private long timestamp;
  private String bidRate;
  private String askRate;
}

package pl.software2.fxapp;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {

  @Value("aws.region")
  private String awsRegion;

  @Bean
  public AmazonDynamoDB dynamoDbClient() {
    return AmazonDynamoDBClient.builder()
      .withRegion(awsRegion)
      .build();
  }

  @Bean
  public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB client) {
    return new DynamoDBMapper(client);
  }
}

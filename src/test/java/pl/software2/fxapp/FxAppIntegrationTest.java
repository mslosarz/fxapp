package pl.software2.fxapp;

import cloud.localstack.ServiceName;
import cloud.localstack.awssdkv1.TestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import pl.software2.fxapp.consumer.MessageListener;
import pl.software2.fxapp.model.Currency;
import pl.software2.fxapp.reader.model.ExchangeRateDto;
import pl.software2.fxapp.reader.repository.ReadOnlyPriceRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = {
  FxAppApplication.class,
  FxAppIntegrationTest.TestConfig.class
}, webEnvironment = RANDOM_PORT)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = ServiceName.DYNAMO)
class FxAppIntegrationTest {

  private final String tableName = "FXPrices";
  @Autowired
  private AmazonDynamoDB dynamoDB;
  @Autowired
  private ReadOnlyPriceRepository repo;
  @Autowired
  private MessageListener listener;

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;


  @Test
  void shouldStoreTwoRecordsForFollowingEntries() {
    // given
    final var entry = """
      108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
      101, GBP/EUR, 1.08970,1.08990,01-06-2020 12:01:02:003
      """;

    // when
    listener.onMessage(entry);

    // then
    final var gbp = repo.getExchangesForCurrency(new Currency("GBP"));
    assertThat(gbp.size()).isEqualTo(2);
    final var eur = gbp.stream().filter(p -> p.getTo().code().equals("EUR")).findFirst();
    assertThat(eur).isPresent();
    assertThat(eur.get().getAskRate()).isEqualTo("1.090009");
    assertThat(eur.get().getBidRate()).isEqualTo("1.089591");
  }

  @Test
  void shouldStoreMoreRecentEntry() {
    // given
    listener.onMessage("""
      109, GBP/USD, 2, 2, 01-06-2020 12:01:02:100
      108, GBP/USD, 1, 1, 01-06-2020 12:01:02:002
            """);

    // when
    final var rsp = restTemplate.getForEntity(url("/fx/rates/GBP/USD"), ExchangeRateDto.class);

    // then
    assertThat(rsp.getStatusCode()).isEqualTo(HttpStatus.OK);
    final var body = rsp.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getFrom()).isEqualTo("GBP");
    assertThat(body.getTo()).isEqualTo("USD");
    assertThat(body.getAsk()).startsWith("2.0");
    assertThat(body.getBid()).startsWith("1.9");
  }

  private String url(String path) {
    return "http://localhost:" + port + path;
  }

  @BeforeEach
  void setup() {
    dynamoDB.createTable(new CreateTableRequest()
      .withTableName(tableName)
      .withAttributeDefinitions(
        new AttributeDefinition("from", "S"),
        new AttributeDefinition("to", "S"))
      .withKeySchema(
        new KeySchemaElement("from", "HASH"),
        new KeySchemaElement("to", "RANGE")
      )
      .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)));
  }

  @AfterEach
  void cleanup() {
    dynamoDB.deleteTable(tableName);
  }

  @Configuration
  public static class TestConfig {
    @Primary
    @Bean
    public AmazonDynamoDB testDynamoDB() {
      return TestUtils.getClientDynamoDB();
    }
  }
}
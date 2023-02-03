package pl.software2.fxapp.consumer.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommissionConfiguration {

  /// TODO: setup could be loaded from somewhere
  @Bean
  public CommissionSetup commissionSetup() {
    return CommissionSetup.DEFAULT;
  }
}

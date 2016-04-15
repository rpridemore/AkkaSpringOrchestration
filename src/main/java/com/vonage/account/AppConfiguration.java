package com.vonage.account;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The application configuration.
 */
@Configuration
class AppConfiguration {
  private ActorSystem system;

  // the application context is needed to initialize the Akka Spring Extension
  @Autowired
  private ApplicationContext applicationContext;

  public AppConfiguration() {
    system = ActorSystem.create("Provisioning");
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        system.shutdown();
        system.awaitTermination();
      }
    });
  }

  /**
   * Actor system singleton for this application.
   */
  @Bean
  public ActorSystem actorSystem() {
    // initialize the application context in the Akka Spring Extension
    SpringExtension.factory.get(system).initialize(applicationContext);
    return system;
  }
}

package com.vonage.account;

import javax.inject.Inject;
import javax.inject.Named;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;

/**
 * An actor that communicates with the BillingService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Named("BillingActor")
@Scope("prototype")
public class BillingActor extends AbstractLoggingActor {

  @Inject
  private BillingService billingService;

  public BillingActor() {
    receive(ReceiveBuilder.
      match(CreateAccount.class, ca -> {
        try {
          billingService.create(ca);
          sender().tell(new CreateResponse("billing"), self());
        } catch(Exception e) {
          sender().tell(new ErrorResponse("billing", e.getMessage()), self());
        }
      }).
      matchAny(o -> log().info("received unknown message: " + o)).
      build());
  }

}


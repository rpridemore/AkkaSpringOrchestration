package com.vonage.account;

import javax.inject.Inject;
import javax.inject.Named;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;

/**
 * An actor that communicates with the ProvisioningService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Named("ProvisioningActor")
@Scope("prototype")
public class ProvisioningActor extends AbstractLoggingActor {

  @Inject
  private ProvisioningService provisioningService;

  public ProvisioningActor() {
    receive(ReceiveBuilder.
      match(CreateAccount.class, ca -> {
        try {
          provisioningService.create(ca);
          sender().tell(new CreateResponse("provisioning"), self());
        } catch(Exception e) {
          sender().tell(new ErrorResponse("provisioning", e.getMessage()), self());
        }
      }).
      matchAny(o -> log().info("received unknown message: " + o)).
      build());
  }

}


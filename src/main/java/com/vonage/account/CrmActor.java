package com.vonage.account;

import javax.inject.Inject;
import javax.inject.Named;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;

/**
 * An actor that communicates with the CrmService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Named("CrmActor")
@Scope("prototype")
public class CrmActor extends AbstractLoggingActor {

  @Inject
  private CrmService crmService;

  public CrmActor() {
    receive(ReceiveBuilder.
      match(CreateAccount.class, ca -> {
        try {
           crmService.create(ca);
           sender().tell(new CreateResponse("crm"), self());
        } catch(Exception e) {
           sender().tell(new ErrorResponse("crm", e.getMessage()), self());
        }
      }).
      matchAny(o -> log().info("received unknown message: " + o)).
      build());
  }

}


package com.vonage.account;

import java.util.Map;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Named;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * An actor that communicates with the BillingService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Named("BizActor")
@Scope("prototype")
public class BizActor extends AbstractLoggingActor { // would extend PersistentActor for real impl

  private ActorSystem system;
  private ActorRef sender;
  private PartialFunction<Object, BoxedUnit> initial;
  private PartialFunction<Object, BoxedUnit> orchestrate;
  private Map<String, Boolean> completedMap = new HashMap<>();
  private Map<String, ActorRef> actors = new HashMap<>();

  @Inject
  public BizActor(ActorSystem system) {
    this.system = system;
    ActorRef billing = system.actorOf(SpringExtension.factory.get(system).props("BillingActor"), "billing");
    ActorRef provisioning = system.actorOf(SpringExtension.factory.get(system).props("ProvisioningActor"), "provisioning");
    ActorRef crm = system.actorOf(SpringExtension.factory.get(system).props("CrmActor"), "crm");
    actors.put("billing", billing);
    actors.put("provisioning", provisioning);
    actors.put("crm", crm);

    initial = ReceiveBuilder
        .match(CreateAccount.class, ca -> {
          sender = sender();
          billing.tell(ca, self());
          provisioning.tell(ca, self());
          crm.tell(ca, self());
          context().become(orchestrate);
        })
        .matchAny(o -> log().info("received unknown message: " + o))
        .build();

    orchestrate = ReceiveBuilder
        .match(CreateResponse.class, complete -> {
          System.out.println(complete.getSystem() + " completed");
          Boolean present = completedMap.putIfAbsent(complete.getSystem(), true);
          context().stop(actors.get(complete.getSystem()));
          if (present != null) {
            System.err.println("Received multiple complete messages from " + complete.getSystem());
          }
          if (completedMap.size() == 3) {
            System.out.println("BizActor: all tasks completed");
            sender.tell("BizActor completed successfully", self());
            context().stop(self());
          }
        })
        .match(ErrorResponse.class, error -> {
          System.out.println(error.getSystem() + " failed with error: " + error.getError());
          completedMap.putIfAbsent(error.getSystem(), false);
          if (completedMap.size() == 3) {
            sender.tell("BizActor completed with error(s)", self());
            context().stop(self());
          }
          context().stop(actors.get(error.getSystem()));
        })
        .matchAny(o -> log().info("received unknown message: " + o))
        .build();

    receive(initial);
  }

}


package com.vonage.account;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import akka.dispatch.Futures;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

/**
 * A main class to start up the application.
 */
public class Main {
  private ActorSystem system;

  private Main() {}

  public void run() {
    // create a spring context and scan the classes
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.scan("com.vonage");
    ctx.refresh();

    // get hold of the actor system
    system = ctx.getBean(ActorSystem.class);

    // use the Spring Extension to create props for a named actor bean
    ActorRef biz = system.actorOf(SpringExtension.factory.get(system).props("BizActor"), "biz");

    CreateAccount create = new CreateAccount("1234", "ACME");

    CompletionStage<Object> cs = PatternsCS.ask(biz, create, 5000);
    cs.whenComplete((r, e) -> {
        if (e == null) {  // completed normally
          System.out.println(r.toString());
        } else {
          System.err.println(e.toString());
        }
        // shut down the actor system
        system.shutdown();
        system.awaitTermination();
      });
  }

  public static void main(String[] args) throws Exception {
    new Main().run();
  }
}

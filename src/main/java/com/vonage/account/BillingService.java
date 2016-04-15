package com.vonage.account;

import java.util.Random;
import javax.inject.Named;

@Named("BillingService")
public class BillingService {

  public void create(CreateAccount createAcct) throws Exception {
    System.out.println("Creating billing account named " + createAcct.getName());
    try {
      int i = new Random().nextInt(10);
      if (i > 6) {
        throw new Exception("Service exception");
      }
      Thread.sleep(500 * i);
    } catch(InterruptedException ignore) {}
  }
}

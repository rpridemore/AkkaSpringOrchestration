package com.vonage.account;

import java.util.Random;
import javax.inject.Named;

@Named("CrmService")
public class CrmService {

  public void create(CreateAccount createAcct) throws Exception {
    System.out.println("Creating CRM account named " + createAcct.getName());
    try {
      int i = new Random().nextInt(10);
      if (i > 6) {
        throw new Exception("Service exception");
      }
      Thread.sleep(500 * i);
    } catch(InterruptedException ignore) {}
  }
}

package com.vonage.account;

import java.util.Random;

public abstract class BaseService {

  public void create(CreateAccount createAcct) throws Exception {
    try {
      int i = new Random().nextInt(10);
      if (i > 5) {
        throw new Exception("Service exception");
      }
      Thread.sleep(500 * i);
    } catch(InterruptedException ignore) {}
  }
}

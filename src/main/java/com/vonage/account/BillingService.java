package com.vonage.account;

import java.util.Random;
import javax.inject.Named;

@Named("BillingService")
public class BillingService extends BaseService {

  public void create(CreateAccount createAcct) throws Exception {
    System.out.println("Creating billing account named " + createAcct.getName());
    super.create(createAcct);
  }
}

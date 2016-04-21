package com.vonage.account;

import java.util.Random;
import javax.inject.Named;

@Named("ProvisioningService")
public class ProvisioningService extends BaseService {

  public void create(CreateAccount createAcct) throws Exception {
    System.out.println("Creating provisioning account named " + createAcct.getName());
    super.create(createAcct);
  }
}

package com.vonage.account;

import java.util.Random;
import javax.inject.Named;

@Named("CrmService")
public class CrmService extends BaseService {

  public void create(CreateAccount createAcct) throws Exception {
    System.out.println("Creating CRM account named " + createAcct.getName());
    super.create(createAcct);
  }
}

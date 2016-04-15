package com.vonage.account;

/**
 * The createAccount message.
 */
public final class CreateAccount {
   private String id;
   private String name;

   public CreateAccount(String id, String name) {
      this.id = id;
      this.name = name;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}


package com.vonage.account;

public class ErrorResponse {
   private String system;
   private String error;

   public ErrorResponse(String system, String error) {
      this.system = system;
      this.error = error;
   }

   public String getSystem() {
      return system;
   }

   public String getError() {
      return error;
   }
}


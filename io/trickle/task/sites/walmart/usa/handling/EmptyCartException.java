package io.trickle.task.sites.walmart.usa.handling;

public class EmptyCartException extends Exception {
   public EmptyCartException() {
      super("CRT expired or empty");
   }
}

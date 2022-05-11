package io.trickle.task.sites.bestbuy;

import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

public class Login$1 extends SearchTerm {
   public boolean match(Message var1) {
      try {
         Date var2 = new Date(System.currentTimeMillis() - 300000L);
         if (var1.getReceivedDate().after(var2) && var1.getSubject().contains("verification code to complete your BestBuy.com")) {
            return true;
         }
      } catch (MessagingException var3) {
         var3.printStackTrace();
      }

      return false;
   }
}

package io.trickle.imap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

public class MailClientImpl$1 extends SearchTerm {
   public MailClientImpl this$0;

   public MailClientImpl$1(MailClientImpl var1) {
      this.this$0 = var1;
      super();
   }

   public boolean match(Message var1) {
      try {
         return var1.getSentDate().after(this.this$0.openTime);
      } catch (MessagingException var3) {
         return false;
      }
   }
}

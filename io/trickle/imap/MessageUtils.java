package io.trickle.imap;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;

public class MessageUtils {
   public static String getTextFromMessage(Message var0) {
      String var1 = "";
      if (!var0.isMimeType("text/plain") && !var0.getContentType().toLowerCase().contains("text")) {
         if (var0.isMimeType("multipart/*")) {
            MimeMultipart var2 = (MimeMultipart)var0.getContent();
            var1 = getTextFromMimeMultipart(var2);
         }
      } else {
         var1 = var0.getContent().toString();
      }

      return var1;
   }

   public static String getTextFromMimeMultipart(MimeMultipart var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = var0.getCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         BodyPart var4 = var0.getBodyPart(var3);
         if (var4.isMimeType("text/plain")) {
            var1.append("\n").append(var4.getContent());
            break;
         }

         if (var4.isMimeType("text/html")) {
            String var5 = (String)var4.getContent();
            var1.append("\n").append(Jsoup.parse(var5).text());
         } else if (var4.getContent() instanceof MimeMultipart) {
            var1.append(getTextFromMimeMultipart((MimeMultipart)var4.getContent()));
         }
      }

      return var1.toString();
   }
}

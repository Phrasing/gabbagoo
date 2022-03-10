/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  javax.mail.BodyPart
 *  javax.mail.Message
 *  javax.mail.internet.MimeMultipart
 *  org.jsoup.Jsoup
 */
package io.trickle.imap;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;

public class MessageUtils {
    public static String getTextFromMessage(Message message) {
        String string = "";
        if (message.isMimeType("text/plain")) return message.getContent().toString();
        if (message.getContentType().toLowerCase().contains("text")) return message.getContent().toString();
        if (!message.isMimeType("multipart/*")) return string;
        MimeMultipart mimeMultipart = (MimeMultipart)message.getContent();
        return MessageUtils.getTextFromMimeMultipart(mimeMultipart);
    }

    public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = mimeMultipart.getCount();
        int n2 = 0;
        while (n2 < n) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(n2);
            if (bodyPart.isMimeType("text/plain")) {
                stringBuilder.append("\n").append(bodyPart.getContent());
                return stringBuilder.toString();
            }
            if (bodyPart.isMimeType("text/html")) {
                String string = (String)bodyPart.getContent();
                stringBuilder.append("\n").append(Jsoup.parse((String)string).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                stringBuilder.append(MessageUtils.getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent()));
            }
            ++n2;
        }
        return stringBuilder.toString();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.mail.Message
 *  javax.mail.MessagingException
 *  javax.mail.search.SearchTerm
 */
package io.trickle.task.sites.bestbuy;

import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

public class Login$1
extends SearchTerm {
    public boolean match(Message message) {
        try {
            Date date = new Date(System.currentTimeMillis() - 300000L);
            if (!message.getReceivedDate().after(date)) return false;
            if (!message.getSubject().contains("verification code to complete your BestBuy.com")) return false;
            return true;
        }
        catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        return false;
    }
}

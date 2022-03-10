/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  javax.mail.Message
 *  javax.mail.MessagingException
 *  javax.mail.search.SearchTerm
 */
package io.trickle.imap;

import io.trickle.imap.MailClientImpl;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

public class MailClientImpl$1
extends SearchTerm {
    public MailClientImpl this$0;

    public boolean match(Message message) {
        try {
            return message.getSentDate().after(this.this$0.openTime);
        }
        catch (MessagingException messagingException) {
            return false;
        }
    }

    public MailClientImpl$1(MailClientImpl mailClientImpl) {
        this.this$0 = mailClientImpl;
    }
}


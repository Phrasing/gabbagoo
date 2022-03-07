/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.jctools.queues.MpmcUnboundedXaddArrayQueue
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.profile.Profile;
import org.jctools.queues.MpmcUnboundedXaddArrayQueue;

public class ZipCodeGroup {
    public boolean zipBanned = false;
    public String zipCode;
    public MpmcUnboundedXaddArrayQueue<Profile> profiles;

    public String toString() {
        return "zipCode=" + this.zipCode + " profiles={" + this.profiles + "}";
    }

    public void returnProfile(Profile profile) {
        this.profiles.offer((Object)profile);
    }

    public ZipCodeGroup(String string) {
        this.zipCode = string;
        this.profiles = new MpmcUnboundedXaddArrayQueue(256);
    }

    public Profile getProfile() {
        return (Profile)this.profiles.poll();
    }

    public ZipCodeGroup put(Profile profile) {
        this.profiles.offer((Object)profile);
        return this;
    }
}


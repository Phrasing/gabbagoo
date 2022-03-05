/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.profile.Profile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ZipCode {
    public List<Profile> profiles;
    public AtomicInteger counter;
    public String zipCode;

    public ZipCode put(Profile profile) {
        this.profiles.add(profile);
        return this;
    }

    public ZipCode(String string) {
        this.zipCode = string;
        this.counter = new AtomicInteger(0);
        this.profiles = new ArrayList<Profile>();
    }

    public String toString() {
        return "zipCode=" + this.zipCode + " profiles={" + this.profiles + "}";
    }

    public static int lambda$getProfile$0(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
    }

    public Profile getProfile() {
        Profile profile;
        int n = this.profiles.size();
        if (n == 1) {
            profile = this.profiles.get(0);
            return profile;
        }
        profile = this.profiles.get(this.counter.getAndUpdate(arg_0 -> ZipCode.lambda$getProfile$0(n, arg_0)));
        return profile;
    }
}


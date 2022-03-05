/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.profile.ZipCode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ZipCodeMap {
    public List<ZipCode> zipCodes = new ArrayList<ZipCode>();

    public String toString() {
        return this.zipCodes.toString();
    }

    public void put(String string, Profile profile) {
        ZipCode zipCode;
        Iterator<ZipCode> iterator = this.zipCodes.iterator();
        do {
            if (!iterator.hasNext()) {
                this.zipCodes.add(new ZipCode(string).put(profile));
                return;
            }
            zipCode = iterator.next();
        } while (!zipCode.zipCode.equals(string));
        zipCode.put(profile);
    }
}


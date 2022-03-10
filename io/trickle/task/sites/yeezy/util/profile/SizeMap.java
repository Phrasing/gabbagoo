/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.profile.ZipCodeGroup;
import io.trickle.task.sites.yeezy.util.profile.ZipCodeMap;
import java.util.HashMap;

public class SizeMap {
    public HashMap<String, ZipCodeMap> sizeZipMap = new HashMap();

    public String toString() {
        return this.sizeZipMap.toString();
    }

    public ZipCodeGroup get(String string) {
        return this.sizeZipMap.get(string).getZipCode();
    }

    public void put(String string, Profile profile) {
        ZipCodeMap zipCodeMap = this.sizeZipMap.get(string);
        if (zipCodeMap == null) {
            zipCodeMap = new ZipCodeMap();
            zipCodeMap.put(profile.getZip(), profile);
            this.sizeZipMap.put(string, zipCodeMap);
            return;
        }
        zipCodeMap.put(profile.getZip(), profile);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.Profile
 *  io.trickle.task.sites.yeezy.util.rotator.ZipCodes
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodes;
import java.util.HashMap;

public class SizeZipMap {
    public HashMap<String, ZipCodes> sizeToZip = new HashMap();

    public ZipCodes getZipsOfSize(String string) {
        return this.sizeToZip.get(string);
    }

    public void put(String string, Profile profile) {
        ZipCodes zipCodes = this.sizeToZip.get(string);
        if (zipCodes == null) {
            zipCodes = new ZipCodes();
            this.sizeToZip.put(string, zipCodes);
        }
        zipCodes.put(profile.getZip(), profile);
    }
}

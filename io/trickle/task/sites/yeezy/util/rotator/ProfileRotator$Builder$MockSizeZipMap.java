/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder$MockZipCodes;
import java.util.HashMap;

public class ProfileRotator$Builder$MockSizeZipMap {
    public HashMap<String, ProfileRotator$Builder$MockZipCodes> sizeToZip = new HashMap();

    public void put(String string, Profile profile) {
        ProfileRotator$Builder$MockZipCodes profileRotator$Builder$MockZipCodes = this.sizeToZip.get(string);
        if (profileRotator$Builder$MockZipCodes == null) {
            profileRotator$Builder$MockZipCodes = new ProfileRotator$Builder$MockZipCodes();
            this.sizeToZip.put(string, profileRotator$Builder$MockZipCodes);
        }
        profileRotator$Builder$MockZipCodes.put(profile.getZip(), profile);
    }
}


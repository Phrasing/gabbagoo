/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.Profile
 *  io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder$MockZipCodes
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator;
import java.util.HashMap;

public class ProfileRotator$Builder$MockSizeZipMap {
    public HashMap<String, ProfileRotator.Builder.MockZipCodes> sizeToZip = new HashMap();

    public void put(String string, Profile profile) {
        ProfileRotator.Builder.MockZipCodes mockZipCodes = this.sizeToZip.get(string);
        if (mockZipCodes == null) {
            mockZipCodes = new ProfileRotator.Builder.MockZipCodes();
            this.sizeToZip.put(string, mockZipCodes);
        }
        mockZipCodes.put(profile.getZip(), profile);
    }
}

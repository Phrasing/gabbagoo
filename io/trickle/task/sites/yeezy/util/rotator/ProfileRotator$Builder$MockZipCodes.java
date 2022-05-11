/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.Profile
 *  io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder$MockZipCodeGroup
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator;
import java.util.ArrayList;
import java.util.List;

public class ProfileRotator$Builder$MockZipCodes {
    public List<ProfileRotator.Builder.MockZipCodeGroup> zipCodeGroups = new ArrayList<ProfileRotator.Builder.MockZipCodeGroup>();

    public void put(String string, Profile profile) {
        ProfileRotator.Builder.MockZipCodeGroup mockZipCodeGroup;
        ProfileRotator.Builder.MockZipCodeGroup mockZipCodeGroup2 = this.zipCodeGroups.iterator();
        do {
            if (!mockZipCodeGroup2.hasNext()) {
                mockZipCodeGroup2 = new ProfileRotator.Builder.MockZipCodeGroup(string);
                mockZipCodeGroup2.profiles.add(profile);
                this.zipCodeGroups.add(mockZipCodeGroup2);
                return;
            }
            mockZipCodeGroup = mockZipCodeGroup2.next();
        } while (!mockZipCodeGroup.zipCode.equals(string));
        mockZipCodeGroup.profiles.add(profile);
    }
}

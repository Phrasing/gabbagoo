/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder$MockZipCodeGroup;
import java.util.ArrayList;
import java.util.List;

public class ProfileRotator$Builder$MockZipCodes {
    public List<ProfileRotator$Builder$MockZipCodeGroup> zipCodeGroups = new ArrayList<ProfileRotator$Builder$MockZipCodeGroup>();

    public void put(String string, Profile profile) {
        ProfileRotator$Builder$MockZipCodeGroup profileRotator$Builder$MockZipCodeGroup;
        Object object = this.zipCodeGroups.iterator();
        do {
            if (!object.hasNext()) {
                object = new ProfileRotator$Builder$MockZipCodeGroup(string);
                ((ProfileRotator$Builder$MockZipCodeGroup)object).profiles.add(profile);
                this.zipCodeGroups.add((ProfileRotator$Builder$MockZipCodeGroup)object);
                return;
            }
            profileRotator$Builder$MockZipCodeGroup = object.next();
        } while (!profileRotator$Builder$MockZipCodeGroup.zipCode.equals(string));
        profileRotator$Builder$MockZipCodeGroup.profiles.add(profile);
    }
}


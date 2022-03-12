/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.task.Task;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder$MockSizeZipMap;
import java.util.HashMap;

public class ProfileRotator$Builder {
    public HashMap<String, ProfileRotator$Builder$MockSizeZipMap> keywordsToSize = new HashMap();

    public ProfileRotator$Builder add(Task task) {
        ProfileRotator$Builder$MockSizeZipMap profileRotator$Builder$MockSizeZipMap = this.keywordsToSize.get(task.getKeywords()[0]);
        if (profileRotator$Builder$MockSizeZipMap == null) {
            profileRotator$Builder$MockSizeZipMap = new ProfileRotator$Builder$MockSizeZipMap();
            this.keywordsToSize.put(task.getKeywords()[0], profileRotator$Builder$MockSizeZipMap);
        }
        profileRotator$Builder$MockSizeZipMap.put(task.getSize(), task.getProfile());
        return this;
    }

    public void build() {
    }
}


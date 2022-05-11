/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.Task
 *  io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder$MockSizeZipMap
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.task.Task;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator$Builder;
import java.util.HashMap;

public class ProfileRotator$Builder {
    public HashMap<String, MockSizeZipMap> keywordsToSize = new HashMap();

    public void build() {
    }

    public ProfileRotator$Builder add(Task task) {
        MockSizeZipMap mockSizeZipMap = this.keywordsToSize.get(task.getKeywords()[0]);
        if (mockSizeZipMap == null) {
            mockSizeZipMap = new MockSizeZipMap();
            this.keywordsToSize.put(task.getKeywords()[0], mockSizeZipMap);
        }
        mockSizeZipMap.put(task.getSize(), task.getProfile());
        return this;
    }
}

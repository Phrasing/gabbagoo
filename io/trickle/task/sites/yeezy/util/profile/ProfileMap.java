/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.task.Task;
import io.trickle.task.sites.yeezy.util.profile.SizeMap;
import java.util.HashMap;

public class ProfileMap {
    public HashMap<String, SizeMap> keywordMap = new HashMap();

    public String toString() {
        return this.keywordMap.toString();
    }

    public void put(Task task) {
        SizeMap sizeMap = this.keywordMap.get(task.getKeywords()[0]);
        if (sizeMap == null) {
            sizeMap = new SizeMap();
            sizeMap.put(task.getSize(), task.getProfile());
            this.keywordMap.put(task.getKeywords()[0], sizeMap);
            return;
        }
        sizeMap.put(task.getSize(), task.getProfile());
    }
}


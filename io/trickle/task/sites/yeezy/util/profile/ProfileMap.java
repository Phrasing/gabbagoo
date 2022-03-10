/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.task.Task;
import io.trickle.task.sites.yeezy.util.profile.SizeMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ProfileMap {
    public HashMap<String, SizeMap> keywordMap = new HashMap();

    public Optional get(String string, String string2) {
        try {
            return Optional.of(this.keywordMap.get(string).get(string2));
        }
        catch (Throwable throwable) {
            return Optional.empty();
        }
    }

    public Optional getAnySku(String string) {
        try {
            SizeMap sizeMap;
            Collection<SizeMap> collection = this.keywordMap.values();
            if (collection.isEmpty()) return Optional.empty();
            Iterator<SizeMap> iterator = collection.iterator();
            do {
                if (!iterator.hasNext()) return Optional.empty();
                sizeMap = iterator.next();
            } while (!sizeMap.sizeZipMap.containsKey(string) || !ThreadLocalRandom.current().nextBoolean());
            return Optional.of(sizeMap.get(string));
        }
        catch (Throwable throwable) {
            return Optional.empty();
        }
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

    public String toString() {
        return this.keywordMap.toString();
    }
}


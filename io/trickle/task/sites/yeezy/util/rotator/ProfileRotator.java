/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.Task
 *  io.trickle.task.sites.yeezy.util.rotator.SizeZipMap
 *  io.trickle.task.sites.yeezy.util.rotator.ZipCodeGroup
 *  io.trickle.task.sites.yeezy.util.rotator.ZipCodes
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.task.Task;
import io.trickle.task.sites.yeezy.util.rotator.SizeZipMap;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodeGroup;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodes;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ProfileRotator {
    public HashMap<String, SizeZipMap> keywordToSize = new HashMap();
    public boolean finished = false;

    public void put(Task task) {
        SizeZipMap sizeZipMap = this.keywordToSize.get(task.getKeywords()[0]);
        if (sizeZipMap == null) {
            sizeZipMap = new SizeZipMap();
            this.keywordToSize.put(task.getKeywords()[0], sizeZipMap);
        }
        sizeZipMap.put(task.getSize(), task.getProfile());
    }

    public Optional get(String string, String string2) {
        try {
            return Optional.ofNullable(this.keywordToSize.get(string).getZipsOfSize(string2).get());
        }
        catch (Throwable throwable) {
            return Optional.empty();
        }
    }

    public synchronized void finish() {
        if (this.finished) return;
        this.finished = true;
        Iterator<SizeZipMap> iterator = this.keywordToSize.values().iterator();
        block0: while (iterator.hasNext()) {
            SizeZipMap sizeZipMap = iterator.next();
            Iterator iterator2 = sizeZipMap.sizeToZip.values().iterator();
            while (true) {
                if (!iterator2.hasNext()) continue block0;
                ZipCodes zipCodes = (ZipCodes)iterator2.next();
                for (ZipCodeGroup zipCodeGroup : zipCodes.writeList) {
                    zipCodeGroup.finish();
                }
                zipCodes.finish();
            }
            break;
        }
        return;
    }

    public Optional getAnySku(String string) {
        try {
            SizeZipMap sizeZipMap;
            Collection<SizeZipMap> collection = this.keywordToSize.values();
            if (collection.isEmpty()) return Optional.empty();
            Iterator<SizeZipMap> iterator = collection.iterator();
            do {
                if (!iterator.hasNext()) return Optional.empty();
                sizeZipMap = iterator.next();
            } while (!sizeZipMap.sizeToZip.containsKey(string) || !ThreadLocalRandom.current().nextBoolean());
            return Optional.ofNullable(sizeZipMap.getZipsOfSize(string).get());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return Optional.empty();
    }
}

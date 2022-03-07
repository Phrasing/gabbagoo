/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.profile;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.profile.ZipCodeGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ZipCodeMap {
    public AtomicInteger counter;
    public List<ZipCodeGroup> zipCodeGroups = new ArrayList<ZipCodeGroup>();

    public void put(String string, Profile profile) {
        ZipCodeGroup zipCodeGroup;
        Iterator<ZipCodeGroup> iterator = this.zipCodeGroups.iterator();
        do {
            if (!iterator.hasNext()) {
                this.zipCodeGroups.add(new ZipCodeGroup(string).put(profile));
                return;
            }
            zipCodeGroup = iterator.next();
        } while (!zipCodeGroup.zipCode.equals(string));
        zipCodeGroup.put(profile);
    }

    public static boolean lambda$getZipCode$1(ZipCodeGroup zipCodeGroup) {
        if (zipCodeGroup.profiles.isEmpty()) return false;
        return true;
    }

    public String toString() {
        return this.zipCodeGroups.toString();
    }

    public ZipCodeGroup getZipCode() {
        ZipCodeGroup zipCodeGroup;
        List list = this.zipCodeGroups.stream().filter(ZipCodeMap::lambda$getZipCode$0).filter(ZipCodeMap::lambda$getZipCode$1).collect(Collectors.toList());
        if (!list.isEmpty()) return (ZipCodeGroup)list.get(ThreadLocalRandom.current().nextInt(list.size()));
        int n = this.zipCodeGroups.size();
        if (n == 1) {
            zipCodeGroup = this.zipCodeGroups.get(0);
            return zipCodeGroup;
        }
        zipCodeGroup = this.zipCodeGroups.get(this.counter.getAndUpdate(arg_0 -> ZipCodeMap.lambda$getZipCode$2(n, arg_0)));
        return zipCodeGroup;
    }

    public static int lambda$getZipCode$2(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
    }

    public static boolean lambda$getZipCode$0(ZipCodeGroup zipCodeGroup) {
        if (zipCodeGroup.zipBanned) return false;
        return true;
    }

    public ZipCodeMap() {
        this.counter = new AtomicInteger(0);
    }
}


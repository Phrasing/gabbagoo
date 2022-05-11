/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.Profile
 *  io.trickle.task.sites.yeezy.util.rotator.ZipCodeGroup
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodeGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ZipCodes {
    public AtomicInteger idx;
    public List<ZipCodeGroup> writeList;
    public boolean finished = false;
    public CopyOnWriteArrayList<ZipCodeGroup> zipCodeGroups = new CopyOnWriteArrayList();

    public static int lambda$get$0(int n, int n2) {
        return ++n2 < n ? n2 : 0;
    }

    public void removeBanned(ZipCodeGroup zipCodeGroup) {
        int n = this.zipCodeGroups.indexOf(zipCodeGroup);
        if (n <= -1) return;
        this.zipCodeGroups.remove(n);
    }

    public void finish() {
        this.zipCodeGroups.addAll(this.writeList);
        this.writeList.clear();
        this.writeList = null;
        this.finished = true;
    }

    public void put(String string, Profile profile) {
        ZipCodeGroup zipCodeGroup;
        if (this.finished) return;
        ZipCodeGroup zipCodeGroup2 = this.writeList.iterator();
        do {
            if (!zipCodeGroup2.hasNext()) {
                zipCodeGroup2 = new ZipCodeGroup(string, this);
                zipCodeGroup2.add(profile);
                this.writeList.add(zipCodeGroup2);
                return;
            }
            zipCodeGroup = zipCodeGroup2.next();
        } while (!zipCodeGroup.zipCode.equals(string));
        zipCodeGroup.add(profile);
    }

    public ZipCodes() {
        this.idx = new AtomicInteger(0);
        this.writeList = new ArrayList<ZipCodeGroup>();
    }

    public ZipCodeGroup get() {
        try {
            int n = this.zipCodeGroups.size();
            if (n != 0) return n == 1 ? this.zipCodeGroups.get(0) : this.zipCodeGroups.get(this.idx.getAndUpdate(arg_0 -> ZipCodes.lambda$get$0(n, arg_0)));
            return null;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}

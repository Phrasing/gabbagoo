/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodeGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ZipCodes {
    public List<ZipCodeGroup> writeList;
    public AtomicInteger idx;
    public boolean finished = false;
    public CopyOnWriteArrayList<ZipCodeGroup> zipCodeGroups = new CopyOnWriteArrayList();

    public void removeBanned(ZipCodeGroup zipCodeGroup) {
        int n = this.zipCodeGroups.indexOf(zipCodeGroup);
        if (n <= -1) return;
        this.zipCodeGroups.remove(n);
    }

    public ZipCodes() {
        this.idx = new AtomicInteger(0);
        this.writeList = new ArrayList<ZipCodeGroup>();
    }

    public ZipCodeGroup get() {
        try {
            ZipCodeGroup zipCodeGroup;
            int n = this.zipCodeGroups.size();
            if (n == 0) {
                return null;
            }
            if (n == 1) {
                zipCodeGroup = this.zipCodeGroups.get(0);
                return zipCodeGroup;
            }
            zipCodeGroup = this.zipCodeGroups.get(this.idx.getAndUpdate(arg_0 -> ZipCodes.lambda$get$0(n, arg_0)));
            return zipCodeGroup;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static int lambda$get$0(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
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
        Object object = this.writeList.iterator();
        do {
            if (!object.hasNext()) {
                object = new ZipCodeGroup(string, this);
                ((ZipCodeGroup)object).add(profile);
                this.writeList.add((ZipCodeGroup)object);
                return;
            }
            zipCodeGroup = object.next();
        } while (!zipCodeGroup.zipCode.equals(string));
        zipCodeGroup.add(profile);
    }
}


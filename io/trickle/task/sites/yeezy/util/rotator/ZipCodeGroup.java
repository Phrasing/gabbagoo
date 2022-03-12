/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZipCodeGroup {
    public List<Profile> writeList;
    public String zipCode;
    public AtomicBoolean zipBanned;
    public ZipCodes parent;
    public boolean finished;
    public ConcurrentLinkedQueue<Profile> profiles;

    public void markBanned() {
        if (this.zipBanned.getAndSet(true)) return;
        this.parent.removeBanned(this);
    }

    public Profile getProfile() {
        return this.profiles.poll();
    }

    public void returnProfile(Profile profile) {
        if (profile == null) return;
        this.profiles.offer(profile);
    }

    public void add(Profile profile) {
        if (this.finished) return;
        this.writeList.add(profile);
    }

    public void finish() {
        Collections.shuffle(this.writeList);
        this.profiles.addAll(this.writeList);
        this.writeList.clear();
        this.writeList = null;
        this.finished = true;
    }

    public ZipCodeGroup(String string, ZipCodes zipCodes) {
        this.zipCode = string;
        this.parent = zipCodes;
        this.finished = false;
        this.writeList = new ArrayList<Profile>();
        this.profiles = new ConcurrentLinkedQueue();
        this.zipBanned = new AtomicBoolean(false);
    }

    public boolean isZipBanned() {
        return this.zipBanned.get();
    }
}


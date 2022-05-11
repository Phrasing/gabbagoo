/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.Profile
 *  io.trickle.task.sites.yeezy.util.rotator.ZipCodes
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
    public boolean finished;
    public AtomicBoolean zipBanned;
    public ConcurrentLinkedQueue<Profile> profiles;
    public List<Profile> writeList;
    public String zipCode;
    public ZipCodes parent;

    public Profile getProfile() {
        return this.profiles.poll();
    }

    public void finish() {
        Collections.shuffle(this.writeList);
        this.profiles.addAll(this.writeList);
        this.writeList.clear();
        this.writeList = null;
        this.finished = true;
    }

    public void add(Profile profile) {
        if (this.finished) return;
        this.writeList.add(profile);
    }

    public void returnProfile(Profile profile) {
        if (profile == null) return;
        this.profiles.offer(profile);
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

    public String getZipCode() {
        return this.zipCode;
    }

    public void markBanned() {
        if (this.zipBanned.getAndSet(true)) return;
        this.parent.removeBanned(this);
    }
}

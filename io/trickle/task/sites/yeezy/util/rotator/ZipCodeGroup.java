package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZipCodeGroup {
   public boolean finished;
   public AtomicBoolean zipBanned;
   public ConcurrentLinkedQueue profiles;
   public List writeList;
   public String zipCode;
   public ZipCodes parent;

   public Profile getProfile() {
      return (Profile)this.profiles.poll();
   }

   public void finish() {
      Collections.shuffle(this.writeList);
      this.profiles.addAll(this.writeList);
      this.writeList.clear();
      this.writeList = null;
      this.finished = true;
   }

   public void add(Profile var1) {
      if (!this.finished) {
         this.writeList.add(var1);
      }

   }

   public void returnProfile(Profile var1) {
      if (var1 != null) {
         this.profiles.offer(var1);
      }

   }

   public ZipCodeGroup(String var1, ZipCodes var2) {
      this.zipCode = var1;
      this.parent = var2;
      this.finished = false;
      this.writeList = new ArrayList();
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
      if (!this.zipBanned.getAndSet(true)) {
         this.parent.removeBanned(this);
      }

   }
}

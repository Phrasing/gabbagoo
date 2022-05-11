package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ZipCodes {
   public AtomicInteger idx = new AtomicInteger(0);
   public List writeList = new ArrayList();
   public boolean finished = false;
   public CopyOnWriteArrayList zipCodeGroups = new CopyOnWriteArrayList();

   public static int lambda$get$0(int var0, int var1) {
      ++var1;
      return var1 < var0 ? var1 : 0;
   }

   public void removeBanned(ZipCodeGroup var1) {
      int var2 = this.zipCodeGroups.indexOf(var1);
      if (var2 > -1) {
         this.zipCodeGroups.remove(var2);
      }

   }

   public void finish() {
      this.zipCodeGroups.addAll(this.writeList);
      this.writeList.clear();
      this.writeList = null;
      this.finished = true;
   }

   public void put(String var1, Profile var2) {
      if (!this.finished) {
         Iterator var3 = this.writeList.iterator();

         while(var3.hasNext()) {
            ZipCodeGroup var4 = (ZipCodeGroup)var3.next();
            if (var4.zipCode.equals(var1)) {
               var4.add(var2);
               return;
            }
         }

         ZipCodeGroup var5 = new ZipCodeGroup(var1, this);
         var5.add(var2);
         this.writeList.add(var5);
      }

   }

   public ZipCodeGroup get() {
      try {
         int var1 = this.zipCodeGroups.size();
         if (var1 == 0) {
            return null;
         } else {
            return var1 == 1 ? (ZipCodeGroup)this.zipCodeGroups.get(0) : (ZipCodeGroup)this.zipCodeGroups.get(this.idx.getAndUpdate(ZipCodes::lambda$get$0));
         }
      } catch (Throwable var2) {
         var2.printStackTrace();
         return null;
      }
   }
}

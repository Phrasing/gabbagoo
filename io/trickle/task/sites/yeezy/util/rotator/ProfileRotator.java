package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.task.Task;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ProfileRotator {
   public HashMap keywordToSize = new HashMap();
   public boolean finished = false;

   public void put(Task var1) {
      SizeZipMap var2 = (SizeZipMap)this.keywordToSize.get(var1.getKeywords()[0]);
      if (var2 == null) {
         var2 = new SizeZipMap();
         this.keywordToSize.put(var1.getKeywords()[0], var2);
      }

      var2.put(var1.getSize(), var1.getProfile());
   }

   public Optional get(String var1, String var2) {
      try {
         return Optional.ofNullable(((SizeZipMap)this.keywordToSize.get(var1)).getZipsOfSize(var2).get());
      } catch (Throwable var4) {
         return Optional.empty();
      }
   }

   public synchronized void finish() {
      if (!this.finished) {
         this.finished = true;
         Iterator var1 = this.keywordToSize.values().iterator();

         while(var1.hasNext()) {
            SizeZipMap var2 = (SizeZipMap)var1.next();
            Iterator var3 = var2.sizeToZip.values().iterator();

            while(var3.hasNext()) {
               ZipCodes var4 = (ZipCodes)var3.next();
               Iterator var5 = var4.writeList.iterator();

               while(var5.hasNext()) {
                  ZipCodeGroup var6 = (ZipCodeGroup)var5.next();
                  var6.finish();
               }

               var4.finish();
            }
         }
      }

   }

   public Optional getAnySku(String var1) {
      try {
         Collection var2 = this.keywordToSize.values();
         if (var2.isEmpty()) {
            return Optional.empty();
         }

         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            SizeZipMap var4 = (SizeZipMap)var3.next();
            if (var4.sizeToZip.containsKey(var1) && ThreadLocalRandom.current().nextBoolean()) {
               return Optional.ofNullable(var4.getZipsOfSize(var1).get());
            }
         }
      } catch (Throwable var5) {
      }

      return Optional.empty();
   }
}

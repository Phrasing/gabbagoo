package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import java.util.HashMap;

public class SizeZipMap {
   public HashMap sizeToZip = new HashMap();

   public ZipCodes getZipsOfSize(String var1) {
      return (ZipCodes)this.sizeToZip.get(var1);
   }

   public void put(String var1, Profile var2) {
      ZipCodes var3 = (ZipCodes)this.sizeToZip.get(var1);
      if (var3 == null) {
         var3 = new ZipCodes();
         this.sizeToZip.put(var1, var3);
      }

      var3.put(var2.getZip(), var2);
   }
}

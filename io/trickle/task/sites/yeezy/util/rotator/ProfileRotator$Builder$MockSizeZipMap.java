package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import java.util.HashMap;

public class ProfileRotator$Builder$MockSizeZipMap {
   public HashMap sizeToZip = new HashMap();

   public void put(String var1, Profile var2) {
      ProfileRotator$Builder$MockZipCodes var3 = (ProfileRotator$Builder$MockZipCodes)this.sizeToZip.get(var1);
      if (var3 == null) {
         var3 = new ProfileRotator$Builder$MockZipCodes();
         this.sizeToZip.put(var1, var3);
      }

      var3.put(var2.getZip(), var2);
   }
}

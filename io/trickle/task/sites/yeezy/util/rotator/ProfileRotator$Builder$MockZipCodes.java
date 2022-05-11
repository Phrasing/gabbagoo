package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.profile.Profile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfileRotator$Builder$MockZipCodes {
   public List zipCodeGroups = new ArrayList();

   public void put(String var1, Profile var2) {
      Iterator var3 = this.zipCodeGroups.iterator();

      ProfileRotator$Builder$MockZipCodeGroup var4;
      do {
         if (!var3.hasNext()) {
            ProfileRotator$Builder$MockZipCodeGroup var5 = new ProfileRotator$Builder$MockZipCodeGroup(var1);
            var5.profiles.add(var2);
            this.zipCodeGroups.add(var5);
            return;
         }

         var4 = (ProfileRotator$Builder$MockZipCodeGroup)var3.next();
      } while(!var4.zipCode.equals(var1));

      var4.profiles.add(var2);
   }
}

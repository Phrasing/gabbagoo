package io.trickle.task.sites.yeezy.util.rotator;

import io.trickle.task.Task;
import java.util.HashMap;

public class ProfileRotator$Builder {
   public HashMap keywordsToSize = new HashMap();

   public void build() {
   }

   public ProfileRotator$Builder add(Task var1) {
      ProfileRotator$Builder$MockSizeZipMap var2 = (ProfileRotator$Builder$MockSizeZipMap)this.keywordsToSize.get(var1.getKeywords()[0]);
      if (var2 == null) {
         var2 = new ProfileRotator$Builder$MockSizeZipMap();
         this.keywordsToSize.put(var1.getKeywords()[0], var2);
      }

      var2.put(var1.getSize(), var1.getProfile());
      return this;
   }
}

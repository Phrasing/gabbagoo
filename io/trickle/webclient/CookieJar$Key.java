package io.trickle.webclient;

public class CookieJar$Key implements Comparable {
   public String name;
   public static String NO_DOMAIN = "";
   public String domain;
   public String path;

   public int hashCode() {
      boolean var1 = true;
      int var2 = 1;
      var2 = 31 * var2 + (this.domain == null ? 0 : this.domain.hashCode());
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      var2 = 31 * var2 + (this.path == null ? 0 : this.path.hashCode());
      return var2;
   }

   public CookieJar$Key(String var1, String var2, String var3) {
      if (var1 != null && var1.length() != 0) {
         while(var1.charAt(0) == '.') {
            var1 = var1.substring(1);
         }

         while(true) {
            if (var1.charAt(var1.length() - 1) != '.') {
               if (var1.length() == 0) {
                  this.domain = "";
               } else {
                  String[] var4 = var1.split("\\.");
                  int var6 = 0;

                  for(int var7 = var4.length - 1; var6 < var4.length / 2; --var7) {
                     String var5 = var4[var7];
                     var4[var7] = var4[var6];
                     var4[var6] = var5;
                     ++var6;
                  }

                  this.domain = String.join(".", var4);
               }
               break;
            }

            var1 = var1.substring(0, var1.length() - 1);
         }
      } else {
         this.domain = "";
      }

      this.path = var2 == null ? "" : var2;
      this.name = var3;
   }

   public int compareTo(CookieJar$Key var1) {
      int var2 = this.domain.compareTo(var1.domain);
      if (var2 == 0) {
         var2 = this.path.compareTo(var1.path);
      }

      if (var2 == 0) {
         var2 = this.name.compareTo(var1.name);
      }

      return var2;
   }

   public String toString() {
      return "Key{domain='" + this.domain + "', path='" + this.path + "', name='" + this.name + "'}";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         CookieJar$Key var2 = (CookieJar$Key)var1;
         if (this.domain == null) {
            if (var2.domain != null) {
               return false;
            }
         } else if (!this.domain.equals(var2.domain)) {
            return false;
         }

         if (this.name == null) {
            if (var2.name != null) {
               return false;
            }
         } else if (!this.name.equals(var2.name)) {
            return false;
         }

         if (this.path == null) {
            return var2.path == null;
         } else {
            return this.path.equals(var2.path);
         }
      }
   }

   public int compareTo(Object var1) {
      return this.compareTo((CookieJar$Key)var1);
   }
}

package io.trickle.webclient;

import io.trickle.core.VertxSingleton;
import io.trickle.util.Storage;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.net.ssl.X509TrustManager;
import org.conscrypt.Conscrypt;

public class ClientType$TrustWrapper implements X509TrustManager {
   public X509TrustManager manager;

   public static String validateChain(X509Certificate[] var0) {
      X509Certificate[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         X509Certificate var4 = var1[var3];
         String var5 = var4.getIssuerDN().getName().toLowerCase(Locale.ROOT);
         if (var5.contains("charles") || var5.contains("mitm") || var5.contains("fiddler") || var5.contains("toolkit")) {
            return var5;
         }
      }

      return null;
   }

   public X509Certificate[] getAcceptedIssuers() {
      return this.manager.getAcceptedIssuers();
   }

   public void checkClientTrusted(X509Certificate[] var1, String var2) {
      this.manager.checkClientTrusted(var1, var2);
   }

   public void checkServerTrusted(X509Certificate[] var1, String var2) {
      if (!Storage.ACCESS_KEY.startsWith("fcd6b1ab-53e8-458a-a809")) {
         try {
            String var3 = validateChain(var1);
            if (var3 != null) {
               VertxSingleton.INSTANCE.get().eventBus().send("login.loader", var3);
               throw new CertificateException(var3);
            }

            this.manager.checkServerTrusted(var1, var2);
         } catch (CertificateException var4) {
            if (!var4.getMessage().contains("GENERIC")) {
               throw var4;
            }
         }
      }

   }

   public ClientType$TrustWrapper() {
      try {
         this.manager = Conscrypt.getDefaultX509TrustManager();
      } catch (KeyManagementException var2) {
         throw new RuntimeException(var2);
      }
   }
}

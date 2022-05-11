package io.trickle.task.sites.bestbuy;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Encryption {
   public static String CONSTANT = "00960001";

   static {
      Security.addProvider(new BouncyCastleProvider());
   }

   public static String padCC(String var0) {
      String var10000 = var0.substring(0, 6);
      return var10000 + "0".repeat(var0.length() - 10) + var0.substring(var0.length() - 4);
   }

   public static String encryptGeneral(String var0, String var1) {
      var0 = var0.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\n", "").replace("\r", "").replace("\t", "");
      byte[] var2 = Base64.getDecoder().decode(var0);
      Cipher var3 = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding", "BC");
      OAEPParameterSpec var4 = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSpecified.DEFAULT);
      X509EncodedKeySpec var5 = new X509EncodedKeySpec(var2);
      KeyFactory var6 = KeyFactory.getInstance("RSA");
      PublicKey var7 = var6.generatePublic(var5);
      var3.init(1, var7, var4);
      return Base64.getEncoder().encodeToString(var3.doFinal(var1.getBytes(StandardCharsets.UTF_8)));
   }

   public static String encrypt(String var0, String var1) {
      byte[] var2 = Base64.getDecoder().decode(var0);
      Cipher var3 = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding", "BC");
      OAEPParameterSpec var4 = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSpecified.DEFAULT);
      X509EncodedKeySpec var5 = new X509EncodedKeySpec(var2);
      KeyFactory var6 = KeyFactory.getInstance("RSA");
      PublicKey var7 = var6.generatePublic(var5);
      var3.init(1, var7, var4);
      return Base64.getEncoder().encodeToString(var3.doFinal(("00960001" + var1).getBytes(StandardCharsets.UTF_8)));
   }

   public static String getFullEncrypted(String var0, String var1, String var2) {
      return encrypt(var1, var0) + ":3:" + var2 + ":" + padCC(var0);
   }
}

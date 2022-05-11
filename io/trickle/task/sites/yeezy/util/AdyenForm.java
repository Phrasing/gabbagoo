package io.trickle.task.sites.yeezy.util;

import io.netty.util.concurrent.FastThreadLocal;
import io.trickle.core.Engine;
import io.trickle.profile.Profile;
import io.trickle.util.Utils;
import io.vertx.core.json.JsonObject;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AdyenForm {
   public static String initializeCount = "1";
   public PublicKey pubKey;
   public static String DEFAULT_ADYEN_KEY = "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1";
   public Cipher aesCipher;
   public static String PREFIX = "adyenjs_";
   public static String sjclStrength = "10";
   public static String VERSION = "0_1_21";
   public static String luhnSameLengthCount = "1";
   public static String SEPARATOR = "$";
   public static FastThreadLocal threadLocal = new FastThreadLocal();
   public static String dfValue = null;
   public static String luhnOkCount = "1";
   public Cipher rsaCipher;
   public static String luhnCount = "1";

   public static String getCSEToken(Profile var0) {
      return getCSEToken(profileToPaymentForm(var0));
   }

   public AdyenForm() {
      String[] var1 = getEncryptionKey();
      String var2 = var1[1];
      String var3 = var1[0];
      KeyFactory var4 = KeyFactory.getInstance("RSA");
      RSAPublicKeySpec var5 = new RSAPublicKeySpec(new BigInteger(var2, 16), new BigInteger(var3, 16));
      this.pubKey = var4.generatePublic(var5);
      this.aesCipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
      this.rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
   }

   public static String profileToPaymentForm(Profile var0) {
      JsonObject var1 = new JsonObject();
      var1.put("cvc", var0.getCvv());
      var1.put("dfValue", dfValue);
      var1.put("expiryMonth", var0.getExpiryMonth().charAt(0) == '0' ? var0.getExpiryMonth().substring(1) : var0.getExpiryMonth());
      var1.put("expiryYear", var0.getExpiryYear());
      var1.put("generationtime", Utils.ISO_8901_JS.format(Instant.now()));
      String var10002 = var0.getFirstName().substring(0, 1).toUpperCase();
      var1.put("holderName", var10002 + var0.getFirstName().substring(1).toLowerCase() + " " + var0.getLastName().substring(0, 1).toUpperCase() + var0.getLastName().substring(1).toLowerCase());
      var1.put("initializeCount", "1");
      var1.put("luhnCount", "1");
      var1.put("luhnOkCount", "1");
      var1.put("luhnSameLengthCount", "1");
      var1.put("number", var0.getCardNumber());
      var1.put("sjclStrength", "10");
      return var1.encode();
   }

   public static SecretKey generateAESKey(int var0) {
      KeyGenerator var1 = KeyGenerator.getInstance("AES");
      var1.init(var0);
      return var1.generateKey();
   }

   public static String getCSEToken(String var0) {
      return getOrCreate().encryptAES(var0);
   }

   public static String[] getEncryptionKey() {
      try {
         String var0 = Engine.get().getClientConfiguration().getString("adyenKey", "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1");
         return var0.split("\\|");
      } catch (Throwable var1) {
         return "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1".split("\\|");
      }
   }

   public static byte[] generateIV(int var0) {
      byte[] var1 = new byte[var0];
      ThreadLocalRandom.current().nextBytes(var1);
      return var1;
   }

   public static AdyenForm getOrCreate() {
      if (threadLocal.isSet()) {
         return (AdyenForm)threadLocal.get();
      } else {
         try {
            AdyenForm var0 = new AdyenForm();
            threadLocal.set(var0);
            return var0;
         } catch (InvalidKeySpecException | NoSuchPaddingException | NoSuchProviderException | InvalidKeyException | NoSuchAlgorithmException var1) {
            throw new RuntimeException(var1);
         }
      }
   }

   public String encryptAES(String var1) {
      try {
         SecretKey var2 = generateAESKey(256);
         byte[] var3 = generateIV(12);
         this.aesCipher.init(1, var2, new IvParameterSpec(var3));
         byte[] var4 = this.aesCipher.doFinal(var1.getBytes());
         byte[] var5 = new byte[var3.length + var4.length];
         System.arraycopy(var3, 0, var5, 0, var3.length);
         System.arraycopy(var4, 0, var5, var3.length, var4.length);
         this.rsaCipher.init(1, this.pubKey);
         byte[] var6 = this.rsaCipher.doFinal(var2.getEncoded());
         return String.format("%s%s%s%s%s%s", "adyenjs_", "0_1_21", "$", Base64.getEncoder().encodeToString(var6), "$", Base64.getEncoder().encodeToString(var5));
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }
}

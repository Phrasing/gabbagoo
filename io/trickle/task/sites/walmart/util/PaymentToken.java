package io.trickle.task.sites.walmart.util;

import io.trickle.task.sites.walmart.util.encryption.Encryptor;
import java.util.Objects;
import java.util.UUID;

public class PaymentToken {
   public String encryptedCvv;
   public String encryptedPan;
   public String vid;
   public String integrityCheck;
   public String phase;
   public String sid;
   public String encrypted4111;
   public String piHash;
   public String keyId;

   public void set4111Encrypted(String var1) {
      this.encrypted4111 = var1;
   }

   public String getVid() {
      return this.vid;
   }

   public PaymentToken(String var1, String var2, String var3, String var4, String var5) {
      this.encryptedPan = var1;
      this.encryptedCvv = var2;
      this.integrityCheck = var3;
      this.keyId = var4;
      this.phase = var5;
      this.sid = UUID.randomUUID().toString();
      this.vid = UUID.randomUUID().toString();
   }

   public static PaymentToken prepareAndGenerate(String var0, String var1, String var2) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      return fromArr(Encryptor.prepareAndEncrypt(var0, var1, var2));
   }

   public static PaymentToken generate(String var0, String var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      return fromArr(Encryptor.encrypt(var0, var1));
   }

   public String getIntegrityCheck() {
      return this.integrityCheck;
   }

   public String getEncryptedPan() {
      return this.encryptedPan;
   }

   public String getPiHash() {
      return this.piHash;
   }

   public String getPhase() {
      return this.phase;
   }

   public static PaymentToken fromString(String var0) {
      String[] var1 = var0.split("#");
      return fromArr(var1);
   }

   public String getSid() {
      return this.sid;
   }

   public String toString() {
      return "PaymentToken{encryptedPan='" + this.encryptedPan + "', encryptedCvv='" + this.encryptedCvv + "', integrityCheck='" + this.integrityCheck + "', keyId='" + this.keyId + "', phase='" + this.phase + "', piHash='" + this.piHash + "'}";
   }

   public void setPiHash(String var1) {
      this.piHash = var1;
   }

   public static PaymentToken fromArr(String[] var0) {
      Objects.requireNonNull(var0);
      return new PaymentToken(var0[0], var0[1], var0[2], var0[3], var0[4]);
   }

   public boolean isPiHashSet() {
      return this.piHash != null && !this.piHash.isBlank();
   }

   public String getEncrypted4111() {
      return this.encrypted4111;
   }

   public String getEncryptedCvv() {
      return this.encryptedCvv;
   }

   public String getKeyId() {
      return this.keyId;
   }
}

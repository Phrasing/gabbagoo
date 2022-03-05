/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.util;

import io.trickle.task.sites.walmart.util.encryption.Encryptor;
import java.util.Objects;
import java.util.UUID;

public class PaymentToken {
    public String encryptedPan;
    public String vid;
    public String sid;
    public String piHash;
    public String phase;
    public String keyId;
    public String integrityCheck;
    public String encrypted4111;
    public String encryptedCvv;

    public void set4111Encrypted(String string) {
        this.encrypted4111 = string;
    }

    public String getSid() {
        return this.sid;
    }

    public static PaymentToken fromString(String string) {
        String[] stringArray = string.split("#");
        return PaymentToken.fromArr(stringArray);
    }

    public static PaymentToken prepareAndGenerate(String string, String string2, String string3) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        Objects.requireNonNull(string3);
        return PaymentToken.fromArr(Encryptor.prepareAndEncrypt(string, string2, string3));
    }

    public String getEncrypted4111() {
        return this.encrypted4111;
    }

    public static PaymentToken fromArr(String[] stringArray) {
        Objects.requireNonNull(stringArray);
        return new PaymentToken(stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4]);
    }

    public static PaymentToken generate(String string, String string2) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        return PaymentToken.fromArr(Encryptor.encrypt(string, string2));
    }

    public String getKeyId() {
        return this.keyId;
    }

    public String getPhase() {
        return this.phase;
    }

    public String getEncryptedPan() {
        return this.encryptedPan;
    }

    public PaymentToken(String string, String string2, String string3, String string4, String string5) {
        this.encryptedPan = string;
        this.encryptedCvv = string2;
        this.integrityCheck = string3;
        this.keyId = string4;
        this.phase = string5;
        this.sid = UUID.randomUUID().toString();
        this.vid = UUID.randomUUID().toString();
    }

    public String getPiHash() {
        return this.piHash;
    }

    public boolean isPiHashSet() {
        if (this.piHash == null) return false;
        if (this.piHash.isBlank()) return false;
        return true;
    }

    public String toString() {
        return "PaymentToken{encryptedPan='" + this.encryptedPan + "', encryptedCvv='" + this.encryptedCvv + "', integrityCheck='" + this.integrityCheck + "', keyId='" + this.keyId + "', phase='" + this.phase + "', piHash='" + this.piHash + "'}";
    }

    public String getIntegrityCheck() {
        return this.integrityCheck;
    }

    public String getVid() {
        return this.vid;
    }

    public String getEncryptedCvv() {
        return this.encryptedCvv;
    }

    public void setPiHash(String string) {
        this.piHash = string;
    }
}


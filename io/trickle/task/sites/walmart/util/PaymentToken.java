/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.util;

import io.trickle.task.sites.walmart.util.encryption.Encryptor;
import java.util.Objects;
import java.util.UUID;

public class PaymentToken {
    public String encrypted4111;
    public String sid;
    public String keyId;
    public String integrityCheck;
    public String phase;
    public String vid;
    public String piHash;
    public String encryptedPan;
    public String encryptedCvv;

    public String getEncrypted4111() {
        return this.encrypted4111;
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

    public String getPhase() {
        return this.phase;
    }

    public static PaymentToken generate(String string, String string2) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        return PaymentToken.fromArr(Encryptor.encrypt(string, string2));
    }

    public void set4111Encrypted(String string) {
        this.encrypted4111 = string;
    }

    public String getSid() {
        return this.sid;
    }

    public String getEncryptedCvv() {
        return this.encryptedCvv;
    }

    public static PaymentToken prepareAndGenerate(String string, String string2, String string3) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        Objects.requireNonNull(string3);
        return PaymentToken.fromArr(Encryptor.prepareAndEncrypt(string, string2, string3));
    }

    public String toString() {
        return "PaymentToken{encryptedPan='" + this.encryptedPan + "', encryptedCvv='" + this.encryptedCvv + "', integrityCheck='" + this.integrityCheck + "', keyId='" + this.keyId + "', phase='" + this.phase + "', piHash='" + this.piHash + "'}";
    }

    public String getVid() {
        return this.vid;
    }

    public static PaymentToken fromArr(String[] stringArray) {
        Objects.requireNonNull(stringArray);
        return new PaymentToken(stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4]);
    }

    public String getPiHash() {
        return this.piHash;
    }

    public boolean isPiHashSet() {
        if (this.piHash == null) return false;
        if (this.piHash.isBlank()) return false;
        return true;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setPiHash(String string) {
        this.piHash = string;
    }

    public String getEncryptedPan() {
        return this.encryptedPan;
    }

    public static PaymentToken fromString(String string) {
        String[] stringArray = string.split("#");
        return PaymentToken.fromArr(stringArray);
    }

    public String getIntegrityCheck() {
        return this.integrityCheck;
    }
}


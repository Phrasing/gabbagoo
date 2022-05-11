/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.walmart.util.encryption.Encryptor
 */
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

    public void set4111Encrypted(String string) {
        this.encrypted4111 = string;
    }

    public String getVid() {
        return this.vid;
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

    public static PaymentToken prepareAndGenerate(String string, String string2, String string3) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        Objects.requireNonNull(string3);
        return PaymentToken.fromArr(Encryptor.prepareAndEncrypt((String)string, (String)string2, (String)string3));
    }

    public static PaymentToken generate(String string, String string2) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        return PaymentToken.fromArr(Encryptor.encrypt((String)string, (String)string2));
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

    public static PaymentToken fromString(String string) {
        String[] stringArray = string.split("#");
        return PaymentToken.fromArr(stringArray);
    }

    public String getSid() {
        return this.sid;
    }

    public String toString() {
        return "PaymentToken{encryptedPan='" + this.encryptedPan + "', encryptedCvv='" + this.encryptedCvv + "', integrityCheck='" + this.integrityCheck + "', keyId='" + this.keyId + "', phase='" + this.phase + "', piHash='" + this.piHash + "'}";
    }

    public void setPiHash(String string) {
        this.piHash = string;
    }

    public static PaymentToken fromArr(String[] stringArray) {
        Objects.requireNonNull(stringArray);
        return new PaymentToken(stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4]);
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

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.FastThreadLocal
 *  io.trickle.core.Engine
 *  io.trickle.profile.Profile
 *  io.trickle.util.Utils
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.yeezy.util;

import io.netty.util.concurrent.FastThreadLocal;
import io.trickle.core.Engine;
import io.trickle.profile.Profile;
import io.trickle.util.Utils;
import io.vertx.core.json.JsonObject;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
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
    public static String initializeCount;
    public PublicKey pubKey;
    public static String DEFAULT_ADYEN_KEY;
    public Cipher aesCipher;
    public static String PREFIX;
    public static String sjclStrength;
    public static String VERSION;
    public static String luhnSameLengthCount;
    public static String SEPARATOR;
    public static FastThreadLocal<AdyenForm> threadLocal;
    public static String dfValue;
    public static String luhnOkCount;
    public Cipher rsaCipher;
    public static String luhnCount;

    public static String getCSEToken(Profile profile) {
        return AdyenForm.getCSEToken(AdyenForm.profileToPaymentForm(profile));
    }

    public AdyenForm() {
        String[] stringArray = AdyenForm.getEncryptionKey();
        String string = stringArray[1];
        String string2 = stringArray[0];
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rSAPublicKeySpec = new RSAPublicKeySpec(new BigInteger(string, 16), new BigInteger(string2, 16));
        this.pubKey = keyFactory.generatePublic(rSAPublicKeySpec);
        this.aesCipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
        this.rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
    }

    public static String profileToPaymentForm(Profile profile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("cvc", (Object)profile.getCvv());
        jsonObject.put("dfValue", (Object)dfValue);
        jsonObject.put("expiryMonth", (Object)(profile.getExpiryMonth().charAt(0) == '0' ? profile.getExpiryMonth().substring(1) : profile.getExpiryMonth()));
        jsonObject.put("expiryYear", (Object)profile.getExpiryYear());
        jsonObject.put("generationtime", (Object)Utils.ISO_8901_JS.format(Instant.now()));
        jsonObject.put("holderName", (Object)(profile.getFirstName().substring(0, 1).toUpperCase() + profile.getFirstName().substring(1).toLowerCase() + " " + profile.getLastName().substring(0, 1).toUpperCase() + profile.getLastName().substring(1).toLowerCase()));
        jsonObject.put("initializeCount", (Object)"1");
        jsonObject.put("luhnCount", (Object)"1");
        jsonObject.put("luhnOkCount", (Object)"1");
        jsonObject.put("luhnSameLengthCount", (Object)"1");
        jsonObject.put("number", (Object)profile.getCardNumber());
        jsonObject.put("sjclStrength", (Object)"10");
        return jsonObject.encode();
    }

    static {
        luhnOkCount = "1";
        VERSION = "0_1_21";
        sjclStrength = "10";
        initializeCount = "1";
        SEPARATOR = "$";
        DEFAULT_ADYEN_KEY = "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1";
        PREFIX = "adyenjs_";
        luhnSameLengthCount = "1";
        luhnCount = "1";
        dfValue = null;
        threadLocal = new FastThreadLocal();
    }

    public static SecretKey generateAESKey(int n) {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    public static String getCSEToken(String string) {
        return AdyenForm.getOrCreate().encryptAES(string);
    }

    public static String[] getEncryptionKey() {
        try {
            String string = Engine.get().getClientConfiguration().getString("adyenKey", "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1");
            return string.split("\\|");
        }
        catch (Throwable throwable) {
            return "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1".split("\\|");
        }
    }

    public static byte[] generateIV(int n) {
        byte[] byArray = new byte[n];
        ThreadLocalRandom.current().nextBytes(byArray);
        return byArray;
    }

    public static AdyenForm getOrCreate() {
        if (threadLocal.isSet()) {
            return (AdyenForm)threadLocal.get();
        }
        try {
            AdyenForm adyenForm = new AdyenForm();
            threadLocal.set((Object)adyenForm);
            return adyenForm;
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | NoSuchPaddingException generalSecurityException) {
            throw new RuntimeException(generalSecurityException);
        }
    }

    public String encryptAES(String string) {
        try {
            SecretKey secretKey = AdyenForm.generateAESKey(256);
            byte[] byArray = AdyenForm.generateIV(12);
            this.aesCipher.init(1, (Key)secretKey, new IvParameterSpec(byArray));
            byte[] byArray2 = this.aesCipher.doFinal(string.getBytes());
            byte[] byArray3 = new byte[byArray.length + byArray2.length];
            System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
            System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
            this.rsaCipher.init(1, this.pubKey);
            byte[] byArray4 = this.rsaCipher.doFinal(secretKey.getEncoded());
            return String.format("%s%s%s%s%s%s", "adyenjs_", "0_1_21", "$", Base64.getEncoder().encodeToString(byArray4), "$", Base64.getEncoder().encodeToString(byArray3));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}

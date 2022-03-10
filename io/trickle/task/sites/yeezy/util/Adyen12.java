/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.core.Engine;
import io.trickle.profile.Profile;
import io.vertx.core.json.JsonObject;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Adyen12 {
    public static PublicKey pubKey;
    public static SimpleDateFormat simpleDateFormat;
    public static SecureRandom srandom;
    public static String PREFIX;
    public static Cipher aesCipher;
    public static String luhnOkCount;
    public static String luhnCount;
    public static String sjclStrength;
    public static String VERSION;
    public static String luhnSameLengthCount;
    public static String dfValue;
    public static String initializeCount;
    public static String SEPARATOR;
    public static Cipher rsaCipher;
    public static boolean $assertionsDisabled;

    public static synchronized byte[] generateIV(int n) {
        byte[] byArray = new byte[n];
        srandom.nextBytes(byArray);
        return byArray;
    }

    static {
        luhnCount = "1";
        VERSION = "0_1_12";
        SEPARATOR = "$";
        initializeCount = "1";
        luhnSameLengthCount = "1";
        sjclStrength = "10";
        luhnOkCount = "1";
        PREFIX = "adyenjs_";
        $assertionsDisabled = !Adyen12.class.desiredAssertionStatus();
        srandom = new SecureRandom();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dfValue = null;
        Security.addProvider((Provider)new BouncyCastleProvider());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String getCSEToken(Profile profile) {
        String string = Adyen12.getCardJSON(profile);
        Adyen12.initRSA();
        return Adyen12.encryptAES(string);
    }

    public static String encryptAES(String string) {
        try {
            SecretKey secretKey = Adyen12.generateAESKey(256);
            byte[] byArray = Adyen12.generateIV(12);
            aesCipher.init(1, (Key)secretKey, new IvParameterSpec(byArray));
            byte[] byArray2 = aesCipher.doFinal(string.getBytes());
            byte[] byArray3 = new byte[byArray.length + byArray2.length];
            System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
            System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
            byte[] byArray4 = rsaCipher.doFinal(secretKey.getEncoded());
            return String.format("%s%s%s%s%s%s", "adyenjs_", "0_1_12", "$", Base64.getEncoder().encodeToString(byArray4), "$", Base64.getEncoder().encodeToString(byArray3));
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static void initRSA() {
        String string = Engine.get().getClientConfiguration().getString("adyenKey", "10001|C4F415A1A41A283417FAB7EF8580E077284BCC2B06F8A6C1785E31F5ABFD38A3E80760E0CA6437A8DC95BA4720A83203B99175889FA06FC6BABD4BF10EEEF0D73EF86DD336EBE68642AC15913B2FC24337BDEF52D2F5350224BD59F97C1B944BD03F0C3B4CA2E093A18507C349D68BE8BA54B458DB63D01377048F3E53C757F82B163A99A6A89AD0B969C0F745BB82DA7108B1D6FD74303711065B61009BC8011C27D1D1B5B9FC5378368F24DE03B582FE3490604F5803E805AEEA8B9EF86C54F27D9BD3FC4138B9DC30AF43A58CFF7C6ECEF68029C234BBC0816193DF9BD708D10AAFF6B10E38F0721CF422867C8CC5C554A357A8F51BA18153FB8A83CCBED1");
        String[] stringArray = string.split("\\|");
        String string2 = stringArray[1];
        String string3 = stringArray[0];
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rSAPublicKeySpec = new RSAPublicKeySpec(new BigInteger(string2, 16), new BigInteger(string3, 16));
            pubKey = keyFactory.generatePublic(rSAPublicKeySpec);
            aesCipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
            rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            rsaCipher.init(1, pubKey);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String getCardJSON(Profile profile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("cvc", (Object)profile.getCvv());
        jsonObject.put("dfValue", (Object)dfValue);
        jsonObject.put("expiryMonth", (Object)(profile.getExpiryMonth().charAt(0) == '0' ? profile.getExpiryMonth().substring(1) : profile.getExpiryMonth()));
        jsonObject.put("expiryYear", (Object)profile.getExpiryYear());
        jsonObject.put("generationtime", (Object)simpleDateFormat.format(new Date()));
        jsonObject.put("holderName", (Object)(profile.getFirstName().substring(0, 1).toUpperCase() + profile.getFirstName().substring(1).toLowerCase() + " " + profile.getLastName().substring(0, 1).toUpperCase() + profile.getLastName().substring(1).toLowerCase()));
        jsonObject.put("initializeCount", (Object)"1");
        jsonObject.put("luhnCount", (Object)"1");
        jsonObject.put("luhnOkCount", (Object)"1");
        jsonObject.put("luhnSameLengthCount", (Object)"1");
        jsonObject.put("number", (Object)profile.getCardNumber());
        jsonObject.put("sjclStrength", (Object)"10");
        return jsonObject.toString();
    }

    public static SecretKey generateAESKey(int n) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            // empty catch block
        }
        if (!$assertionsDisabled && keyGenerator == null) {
            throw new AssertionError();
        }
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }
}


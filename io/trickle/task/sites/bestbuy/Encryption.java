/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 */
package io.trickle.task.sites.bestbuy;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Encryption {
    public static String CONSTANT = "00960001";

    static {
        Security.addProvider((Provider)new BouncyCastleProvider());
    }

    public static String padCC(String string) {
        return string.substring(0, 6) + "0".repeat(string.length() - 10) + string.substring(string.length() - 4);
    }

    public static String encryptGeneral(String string, String string2) {
        string = string.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\n", "").replace("\r", "").replace("\t", "");
        byte[] byArray = Base64.getDecoder().decode(string);
        Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding", "BC");
        OAEPParameterSpec oAEPParameterSpec = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byArray);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        cipher.init(1, (Key)publicKey, oAEPParameterSpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(string2.getBytes(StandardCharsets.UTF_8)));
    }

    public static String getFullEncrypted(String string, String string2, String string3) {
        return Encryption.encrypt(string2, string) + ":3:" + string3 + ":" + Encryption.padCC(string);
    }

    public static String encrypt(String string, String string2) {
        byte[] byArray = Base64.getDecoder().decode(string);
        Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding", "BC");
        OAEPParameterSpec oAEPParameterSpec = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byArray);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        cipher.init(1, (Key)publicKey, oAEPParameterSpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(("00960001" + string2).getBytes(StandardCharsets.UTF_8)));
    }
}


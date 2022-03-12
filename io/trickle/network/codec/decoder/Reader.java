/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.auth0.jwt.JWT
 *  com.auth0.jwt.JWTVerifier
 *  com.auth0.jwt.algorithms.Algorithm
 *  com.auth0.jwt.interfaces.DecodedJWT
 *  org.bouncycastle.util.encoders.Base64
 */
package io.trickle.network.codec.decoder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bouncycastle.util.encoders.Base64;

public class Reader {
    public JWTVerifier verifier;

    public static void lambda$new$0() {
        System.exit(-3);
    }

    public static Optional loadKey() {
        String string = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnR3U4yTjS2h9/fYujHDz3C58QVJE2QH1\nQCoHe0n22zg703Ac8lDo47A5LIqLs2TKB0HfRnX8NDxiT4O1Y9tuTdZZd06cV6qaHXM5C5TAc02g\nuPdFgkkoq5/3OcTVD9ExOA+SNHOBEAOyndmtaOsWYd+jhaf5lD906fS5qlNScooL0zf57t/TDK/f\nBRrU4+Rs6urQxgN6NBbqVfLQvvzB0T2fpR+vWw45MKD1h8ntF8iXbnKbbKhm59uDZ4HojxPX5oOb\nvxOqG0dK9qByA5QcOfwugxv8II8THQB8Fnc7SqoJb2nuyzFqim5agg4yyl5BODX0Kq/KJ7VwRQOi\nP9IXUQIDAQAB\n-----END PUBLIC KEY-----";
        string = string.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(string));
            return Optional.ofNullable((RSAPublicKey)keyFactory.generatePublic(x509EncodedKeySpec));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException generalSecurityException) {
            return Optional.empty();
        }
    }

    public byte[] read(String string) {
        try {
            DecodedJWT decodedJWT = this.verifier.verify(string);
            Objects.requireNonNull(decodedJWT);
            String string2 = decodedJWT.getClaim("mtob").asString();
            Objects.requireNonNull(string2);
            return Base64.decode((String)string2);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    public Reader() {
        Optional optional = Reader.loadKey();
        if (optional.isPresent()) {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)((RSAPublicKey)optional.get()), null);
            this.verifier = JWT.require((Algorithm)algorithm).withIssuer("m66").build();
            return;
        }
        CompletableFuture.runAsync(Reader::lambda$new$0);
        throw new RuntimeException("Signing key not found");
    }
}


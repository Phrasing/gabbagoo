package io.trickle.network.codec.decoder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Reader {
   public JWTVerifier verifier;

   public static Optional loadKey() {
      String var0 = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnR3U4yTjS2h9/fYujHDz3C58QVJE2QH1\nQCoHe0n22zg703Ac8lDo47A5LIqLs2TKB0HfRnX8NDxiT4O1Y9tuTdZZd06cV6qaHXM5C5TAc02g\nuPdFgkkoq5/3OcTVD9ExOA+SNHOBEAOyndmtaOsWYd+jhaf5lD906fS5qlNScooL0zf57t/TDK/f\nBRrU4+Rs6urQxgN6NBbqVfLQvvzB0T2fpR+vWw45MKD1h8ntF8iXbnKbbKhm59uDZ4HojxPX5oOb\nvxOqG0dK9qByA5QcOfwugxv8II8THQB8Fnc7SqoJb2nuyzFqim5agg4yyl5BODX0Kq/KJ7VwRQOi\nP9IXUQIDAQAB\n-----END PUBLIC KEY-----";
      var0 = var0.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");

      try {
         KeyFactory var1 = KeyFactory.getInstance("RSA");
         X509EncodedKeySpec var2 = new X509EncodedKeySpec(Base64.getDecoder().decode(var0));
         return Optional.ofNullable((RSAPublicKey)var1.generatePublic(var2));
      } catch (InvalidKeySpecException | NoSuchAlgorithmException var3) {
         return Optional.empty();
      }
   }

   public Reader() {
      Optional var1 = loadKey();
      if (var1.isPresent()) {
         Algorithm var2 = Algorithm.RSA256((RSAPublicKey)var1.get(), (RSAPrivateKey)null);
         this.verifier = JWT.require(var2).withIssuer("m66").build();
      } else {
         CompletableFuture.runAsync(Reader::lambda$new$0);
         throw new RuntimeException("Signing key not found");
      }
   }

   public byte[] read(String var1) {
      try {
         DecodedJWT var2 = this.verifier.verify(var1);
         Objects.requireNonNull(var2);
         String var3 = var2.getClaim("mtob").asString();
         Objects.requireNonNull(var3);
         return org.bouncycastle.util.encoders.Base64.decode(var3);
      } catch (Throwable var4) {
         return null;
      }
   }

   public static void lambda$new$0() {
      System.exit(-3);
   }
}

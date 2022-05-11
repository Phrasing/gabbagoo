/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.VertxSingleton
 *  io.trickle.util.Storage
 *  org.conscrypt.Conscrypt
 */
package io.trickle.webclient;

import io.trickle.core.VertxSingleton;
import io.trickle.util.Storage;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.net.ssl.X509TrustManager;
import org.conscrypt.Conscrypt;

public class ClientType$TrustWrapper
implements X509TrustManager {
    public X509TrustManager manager;

    public static String validateChain(X509Certificate[] x509CertificateArray) {
        X509Certificate[] x509CertificateArray2 = x509CertificateArray;
        int n = x509CertificateArray2.length;
        int n2 = 0;
        while (n2 < n) {
            X509Certificate x509Certificate = x509CertificateArray2[n2];
            String string = x509Certificate.getIssuerDN().getName().toLowerCase(Locale.ROOT);
            if (string.contains("charles")) return string;
            if (string.contains("mitm")) return string;
            if (string.contains("fiddler")) return string;
            if (string.contains("toolkit")) {
                return string;
            }
            ++n2;
        }
        return null;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.manager.getAcceptedIssuers();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
        this.manager.checkClientTrusted(x509CertificateArray, string);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
        if (Storage.ACCESS_KEY.startsWith("fcd6b1ab-53e8-458a-a809")) return;
        try {
            String string2 = ClientType$TrustWrapper.validateChain(x509CertificateArray);
            if (string2 != null) {
                VertxSingleton.INSTANCE.get().eventBus().send("login.loader", (Object)string2);
                throw new CertificateException(string2);
            }
            this.manager.checkServerTrusted(x509CertificateArray, string);
        }
        catch (CertificateException certificateException) {
            if (certificateException.getMessage().contains("GENERIC")) return;
            throw certificateException;
        }
    }

    public ClientType$TrustWrapper() {
        try {
            this.manager = Conscrypt.getDefaultX509TrustManager();
        }
        catch (KeyManagementException keyManagementException) {
            throw new RuntimeException(keyManagementException);
        }
    }
}

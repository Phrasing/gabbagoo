/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.webclient;

import io.trickle.core.VertxSingleton;
import io.trickle.webclient.ClientType;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.net.ssl.X509TrustManager;

public class ClientType$8
implements X509TrustManager {
    public ClientType this$0;

    public ClientType$8(ClientType clientType) {
        this.this$0 = clientType;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
        X509Certificate[] x509CertificateArray2 = x509CertificateArray;
        int n = x509CertificateArray2.length;
        int n2 = 0;
        while (n2 < n) {
            X509Certificate x509Certificate = x509CertificateArray2[n2];
            if (x509Certificate.getIssuerDN().getName().equals(x509Certificate.getSubjectDN().getName()) && (x509Certificate.getIssuerDN().getName().toLowerCase(Locale.ROOT).contains("charles") || x509Certificate.getIssuerDN().getName().toLowerCase(Locale.ROOT).contains("mitm") || x509Certificate.getIssuerDN().getName().toLowerCase(Locale.ROOT).contains("fiddler") || x509Certificate.getIssuerDN().getName().toLowerCase(Locale.ROOT).contains("toolkit"))) {
                VertxSingleton.INSTANCE.get().eventBus().send("login.loader", (Object)x509Certificate.getIssuerDN().getName());
                throw new CertificateException();
            }
            ++n2;
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}


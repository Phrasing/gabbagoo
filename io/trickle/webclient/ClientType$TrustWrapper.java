/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.conscrypt.Conscrypt
 *  org.conscrypt.TrustManagerImpl
 */
package io.trickle.webclient;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import org.conscrypt.Conscrypt;
import org.conscrypt.TrustManagerImpl;

public class ClientType$TrustWrapper
implements X509TrustManager {
    public TrustManagerImpl manager = (TrustManagerImpl)Conscrypt.getDefaultX509TrustManager();

    @Override
    public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
        this.manager.checkClientTrusted(x509CertificateArray, string);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
        this.manager.checkServerTrusted(x509CertificateArray, string);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.manager.getAcceptedIssuers();
    }
}


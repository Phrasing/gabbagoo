/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.net.TrustOptions
 *  io.vertx.ext.web.client.WebClientOptions
 *  org.conscrypt.Conscrypt
 */
package io.trickle.webclient;

import io.trickle.webclient.ClientType$1;
import io.trickle.webclient.ClientType$2;
import io.trickle.webclient.ClientType$3;
import io.trickle.webclient.ClientType$4;
import io.trickle.webclient.ClientType$5;
import io.trickle.webclient.ClientType$6;
import io.trickle.webclient.ClientType$7;
import io.trickle.webclient.ClientType$8;
import io.vertx.core.net.TrustOptions;
import io.vertx.ext.web.client.WebClientOptions;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.TrustManager;
import org.conscrypt.Conscrypt;

public class ClientType
extends Enum {
    public static /* enum */ ClientType H1;
    public static /* enum */ ClientType PX_SDK_PIXEL_3;
    public static /* enum */ ClientType BASIC;
    public static /* enum */ ClientType PX_SDK_IOS;
    public static ClientType[] $VALUES;
    public static /* enum */ ClientType CHROME;
    public static /* enum */ ClientType HIBBETT_ANDROID;
    public static /* enum */ ClientType WALMART_PIXEL_3;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public ClientType() {
        void var2_-1;
        void var1_-1;
    }

    public static TrustOptions getDefault() {
        try {
            return TrustOptions.wrap((TrustManager)Conscrypt.getDefaultX509TrustManager());
        }
        catch (KeyManagementException keyManagementException) {
            return null;
        }
    }

    public WebClientOptions baseOptions(WebClientOptions webClientOptions) {
        return webClientOptions.setLogActivity(false).setUserAgentEnabled(false).setSsl(true).setUseAlpn(true).setTrustAll(false).setVerifyHost(true).setForceSni(true).setConnectTimeout(150000).setSslHandshakeTimeoutUnit(TimeUnit.SECONDS).setSslHandshakeTimeout(150L).setIdleTimeoutUnit(TimeUnit.SECONDS).setIdleTimeout(150).setKeepAlive(true).setKeepAliveTimeout(30).setHttp2KeepAliveTimeout(100).setHttp2MaxPoolSize(10).setHttp2MultiplexingLimit(250).setPoolCleanerPeriod(15000).setMaxPoolSize(10).setTryUseCompression(true).setTcpFastOpen(true).setTcpKeepAlive(true).setTcpNoDelay(true).setTcpQuickAck(true).setFollowRedirects(false).setTrustOptions(TrustOptions.wrap((TrustManager)new ClientType$8(this)));
    }

    static {
        BASIC = new ClientType$1();
        H1 = new ClientType$2();
        CHROME = new ClientType$3();
        WALMART_PIXEL_3 = new ClientType$4();
        HIBBETT_ANDROID = new ClientType$5();
        PX_SDK_PIXEL_3 = new ClientType$6();
        PX_SDK_IOS = new ClientType$7();
        $VALUES = new ClientType[]{BASIC, H1, CHROME, WALMART_PIXEL_3, HIBBETT_ANDROID, PX_SDK_PIXEL_3, PX_SDK_IOS};
    }

    public static ClientType[] values() {
        return (ClientType[])$VALUES.clone();
    }

    public static ClientType valueOf(String string) {
        return Enum.valueOf(ClientType.class, string);
    }

    public int getWindowUpdate() {
        return -1;
    }

    public WebClientOptions options() {
        return null;
    }

    public List ciphers() {
        return new ArrayList();
    }
}


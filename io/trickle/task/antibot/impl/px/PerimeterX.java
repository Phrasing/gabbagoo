/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Vertx
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.antibot.impl.px;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW;
import io.trickle.task.antibot.impl.px.payload.token.MobilePX;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Vertx;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public abstract class PerimeterX {
    public Logger logger;
    public static Pattern PXDE_PATTERN;
    public RealClient client;
    public T result;
    public String pxhd;
    public static Pattern BAKE_PATTERN;
    public Vertx vertx;

    public abstract void reset();

    public static PerimeterX createDesktopAPI(TaskActor taskActor) {
        return new DesktopPXAPI3(taskActor);
    }

    public Boolean updatePxhd(String string) {
        if (string == null) {
            return null;
        }
        this.pxhd = string;
        return true;
    }

    public Object tokenValue() {
        return this.result;
    }

    static {
        BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
        PXDE_PATTERN = Pattern.compile("_pxde\\|330\\|(.*?)\\|");
    }

    public abstract CompletableFuture initialise();

    public PerimeterX(TaskActor taskActor) {
        Objects.requireNonNull(taskActor.getVertx());
        Objects.requireNonNull(taskActor.getLogger());
        this.vertx = taskActor.getVertx();
        this.logger = taskActor.getLogger();
        this.client = RealClientFactory.fromOther(this.vertx, taskActor.getClient().getWebClient(), ClientType.PX_SDK_PIXEL_3);
    }

    public void restartClient(RealClient realClient) {
        try {
            RealClient realClient2 = RealClientFactory.fromOther(this.vertx, realClient, this.client.type());
            this.client.close();
            this.client = realClient2;
            return;
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("Failed to restart client: {}", (Object)throwable.getMessage(), (Object)throwable);
        }
    }

    public abstract String getDeviceAcceptEncoding();

    public void close() {
        this.client.close();
        this.client = null;
        this.reset();
    }

    public static PerimeterX createDesktop(TaskActor taskActor) {
        return new DesktopPXNEW(taskActor);
    }

    public static PerimeterX createMobile(TaskActor taskActor) {
        return new MobilePX(taskActor);
    }

    public PerimeterX(TaskActor taskActor, ClientType clientType) {
        this(taskActor.getVertx(), taskActor.getLogger(), clientType);
    }

    public abstract String getDeviceUA();

    public abstract CompletableFuture solveCaptcha(String var1, String var2, String var3);

    public abstract String getDeviceSecUA();

    public abstract String getVid();

    public abstract String getDeviceLang();

    public abstract CompletableFuture solve();

    public PerimeterX(Vertx vertx, Logger logger, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(logger);
        this.vertx = vertx;
        this.logger = logger;
        if (clientType == null) {
            this.client = RealClientFactory.build(vertx, ClientType.BASIC);
            return;
        }
        this.client = RealClientFactory.buildProxied(this.vertx, clientType);
    }
}


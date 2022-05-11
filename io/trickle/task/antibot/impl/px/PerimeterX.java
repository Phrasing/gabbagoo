/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3
 *  io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW
 *  io.trickle.task.antibot.impl.px.payload.token.MobilePX
 *  io.trickle.webclient.ClientType
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.RealClientFactory
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
    public static Pattern PXDE_PATTERN;
    public T result;
    public String pxhd;
    public Logger logger;
    public RealClient client;
    public static Pattern BAKE_PATTERN;
    public Vertx vertx;

    public void close() {
        this.client.close();
        this.client = null;
        this.reset();
    }

    public abstract String getDeviceSecUA();

    public abstract CompletableFuture solve();

    public abstract String getVid();

    public static PerimeterX createMobile(TaskActor taskActor) {
        return new MobilePX(taskActor);
    }

    public PerimeterX(TaskActor taskActor, ClientType clientType) {
        this(taskActor.getVertx(), taskActor.getLogger(), clientType);
    }

    public abstract String getDeviceAcceptEncoding();

    public void restartClient(RealClient realClient) {
        try {
            RealClient realClient2 = RealClientFactory.fromOther((Vertx)this.vertx, (RealClient)realClient, (ClientType)this.client.type());
            this.client.close();
            this.client = realClient2;
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("Failed to restart client: {}", (Object)throwable.getMessage(), (Object)throwable);
        }
    }

    public static PerimeterX createDesktopAPI(TaskActor taskActor) {
        return new DesktopPXAPI3(taskActor);
    }

    static {
        BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
        PXDE_PATTERN = Pattern.compile("_pxde\\|330\\|(.*?)\\|");
    }

    public abstract CompletableFuture initialise();

    public abstract String getDeviceLang();

    public Object tokenValue() {
        return this.result;
    }

    public Boolean updatePxhd(String string) {
        if (string == null) {
            return null;
        }
        this.pxhd = string;
        return true;
    }

    public abstract String getDeviceUA();

    public abstract CompletableFuture solveCaptcha(String var1, String var2, String var3);

    public PerimeterX(Vertx vertx, Logger logger, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(logger);
        this.vertx = vertx;
        this.logger = logger;
        this.client = clientType == null ? RealClientFactory.build((Vertx)vertx, (ClientType)ClientType.BASIC) : RealClientFactory.buildProxied((Vertx)this.vertx, (ClientType)clientType);
    }

    public PerimeterX(TaskActor taskActor) {
        Objects.requireNonNull(taskActor.getVertx());
        Objects.requireNonNull(taskActor.getLogger());
        this.vertx = taskActor.getVertx();
        this.logger = taskActor.getLogger();
        this.client = RealClientFactory.fromOther((Vertx)this.vertx, (RealClient)taskActor.getClient().getWebClient(), (ClientType)ClientType.PX_SDK_PIXEL_3);
    }

    public static PerimeterX createDesktop(TaskActor taskActor) {
        return new DesktopPXNEW(taskActor);
    }

    public abstract void reset();
}

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
   public static Pattern PXDE_PATTERN = Pattern.compile("_pxde\\|330\\|(.*?)\\|");
   public Object result;
   public String pxhd;
   public Logger logger;
   public RealClient client;
   public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
   public Vertx vertx;

   public void close() {
      this.client.close();
      this.client = null;
      this.reset();
   }

   public abstract String getDeviceSecUA();

   public abstract CompletableFuture solve();

   public abstract String getVid();

   public static PerimeterX createMobile(TaskActor var0) {
      return new MobilePX(var0);
   }

   public PerimeterX(TaskActor var1, ClientType var2) {
      this(var1.getVertx(), var1.getLogger(), var2);
   }

   public abstract String getDeviceAcceptEncoding();

   public void restartClient(RealClient var1) {
      try {
         RealClient var2 = RealClientFactory.fromOther(this.vertx, var1, this.client.type());
         this.client.close();
         this.client = var2;
      } catch (Throwable var3) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Failed to restart client: {}", var3.getMessage(), var3);
         }
      }

   }

   public static PerimeterX createDesktopAPI(TaskActor var0) {
      return new DesktopPXAPI3(var0);
   }

   public abstract CompletableFuture initialise();

   public abstract String getDeviceLang();

   public Object tokenValue() {
      return this.result;
   }

   public Boolean updatePxhd(String var1) {
      if (var1 == null) {
         return null;
      } else {
         this.pxhd = var1;
         return true;
      }
   }

   public abstract String getDeviceUA();

   public abstract CompletableFuture solveCaptcha(String var1, String var2, String var3);

   public PerimeterX(Vertx var1, Logger var2, ClientType var3) {
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      this.vertx = var1;
      this.logger = var2;
      if (var3 == null) {
         this.client = RealClientFactory.build(var1, ClientType.BASIC);
      } else {
         this.client = RealClientFactory.buildProxied(this.vertx, var3);
      }

   }

   public PerimeterX(TaskActor var1) {
      Objects.requireNonNull(var1.getVertx());
      Objects.requireNonNull(var1.getLogger());
      this.vertx = var1.getVertx();
      this.logger = var1.getLogger();
      this.client = RealClientFactory.fromOther(this.vertx, var1.getClient().getWebClient(), ClientType.PX_SDK_PIXEL_3);
   }

   public static PerimeterX createDesktop(TaskActor var0) {
      return new DesktopPXNEW(var0);
   }

   public abstract void reset();
}

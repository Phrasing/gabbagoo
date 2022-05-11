package io.trickle.harvester;

import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.UrlRequestJob.Options;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback.Response;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.client.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Harvester$RequestInterceptor implements InterceptUrlRequestCallback {
   public Harvester this$0;

   public boolean isSupportedV3Site(String var1) {
      if (var1.contains("https://www.google.com") && var1.endsWith(".js")) {
         return true;
      } else {
         return var1.contains("yeezysupply") || var1.contains("jdsports") || var1.contains("finishline") || var1.contains("/account/login") || var1.contains("/account/register") || var1.equals("https://www.abc-mart.net/shop/order/method.aspx") || var1.startsWith("https://www.abc-mart.net/shop/order/estimate.aspx?estimate=");
      }
   }

   public static void lambda$on$1(UrlRequestJob var0, HttpResponse var1) {
      var0.write(((Buffer)var1.body()).getBytes());
      var0.complete();
   }

   public InterceptUrlRequestCallback.Response on(InterceptUrlRequestCallback.Params var1) {
      String var2 = var1.urlRequest().url();
      boolean var3 = var2.contains(".ico");
      UrlRequestJob var4;
      if (this.this$0.tokenHolder.get() != null && ((CaptchaToken)this.this$0.tokenHolder.get()).isCheckpoint() && var2.contains("captcha") && (var2.contains("reload") || var2.contains("replaceimage"))) {
         this.this$0.resetClickCounter();
         var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).addHttpHeader(HttpHeader.of("Access-Control-Allow-Origin", "*")).addHttpHeader(HttpHeader.of("Cross-Origin-Resource-Policy", "cross-origin")).build());
         Harvester.executorService.submit(this::lambda$on$2);
         return Response.intercept(var4);
      } else if (var2.contains("https://www.google.com")) {
         return Response.proceed();
      } else if (!var3 && this.isSupportedV2Site(var2)) {
         var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).build());
         var4.write(Harvester.captchaPageV2().replace("%s", Sitekeys.getSitekeyFromLink(var2)).getBytes(StandardCharsets.UTF_8));
         var4.complete();
         return Response.intercept(var4);
      } else if (!var3 && this.isSupportedV3Site(var2)) {
         var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).build());
         var4.write(String.format(Harvester.captchaPageV3(), this.this$0.reloads, Sitekeys.getSitekeyFromLink(var2), Sitekeys.getSitekeyFromLink(var2), Sitekeys.getActionFromLink(var2)).getBytes(StandardCharsets.UTF_8));
         var4.complete();
         return Response.intercept(var4);
      } else if (!var3 && this.isCheckpoint(var2)) {
         var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).build());
         CaptchaToken var5 = this.this$0.solveFuture.getEmptyCaptchaToken();
         if (((CaptchaToken)this.this$0.tokenHolder.get()).getToken() == null) {
            if (var5.isHcaptcha) {
               this.this$0.hotkey = null;
               if (System.currentTimeMillis() - var5.creationTime > 8000L) {
                  var5.shopify.getCPHtml(true).thenAccept(Harvester$RequestInterceptor::lambda$on$3);
               } else {
                  var4.write(((CaptchaToken)this.this$0.tokenHolder.get()).getHtml().getBytes(StandardCharsets.UTF_8));
                  var4.complete();
               }
            } else {
               this.this$0.hotkey = 'r';
               var4.write(((CaptchaToken)this.this$0.tokenHolder.get()).getHtml().getBytes(StandardCharsets.UTF_8));
               var4.complete();
            }
         } else {
            var4.write("<html>Waiting for captcha</html>".getBytes(StandardCharsets.UTF_8));
            var4.complete();
         }

         return Response.intercept(var4);
      } else if (var2.contains("recaptcha.net")) {
         var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).build());
         return Response.intercept(var4);
      } else {
         return Response.proceed();
      }
   }

   public static RequestOptions lambda$on$0(InterceptUrlRequestCallback.Params var0) {
      return Request.convertToVertx(var0);
   }

   public boolean isSupportedV2Site(String var1) {
      return var1.contains("https://www.google.com") && var1.endsWith(".js") ? true : var1.endsWith("/challenge");
   }

   public boolean isCheckpoint(String var1) {
      return var1.contains("https://www.google.com") && var1.endsWith(".js") ? true : var1.contains("/checkpoint");
   }

   public Object on(Object var1) {
      return this.on((InterceptUrlRequestCallback.Params)var1);
   }

   public static void lambda$on$3(UrlRequestJob var0, CaptchaToken var1, String var2) {
      var0.write(var1.v2Html(var2).getBytes(StandardCharsets.UTF_8));
      var0.complete();
   }

   public Harvester$RequestInterceptor(Harvester var1) {
      this.this$0 = var1;
      super();
   }

   public void lambda$on$2(String var1, InterceptUrlRequestCallback.Params var2, UrlRequestJob var3) {
      try {
         this.this$0.transferCookies(var1);
         this.this$0.send(Harvester$RequestInterceptor::lambda$on$0, var2.uploadData()).thenAccept(Harvester$RequestInterceptor::lambda$on$1);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }
}

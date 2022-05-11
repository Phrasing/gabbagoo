package io.trickle.task.antibot.impl.px;

import io.trickle.core.actor.TaskActor;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PXTokenAPI extends PXTokenBase {
   public String uuid;
   public RealClient apiClient;
   public long requestTime;
   public String vid;
   public String sid;
   public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");

   public String getSid() {
      return "null";
   }

   public CompletableFuture requestExecutor(Supplier var1, Object var2) {
      int var3 = 0;

      while(this.client.isActive() && var3++ <= 100) {
         HttpResponse var4;
         CompletableFuture var5;
         CompletableFuture var6;
         if (var2 == null) {
            var6 = Request.send((HttpRequest)var1.get());
            if (!var6.isDone()) {
               var5 = var6;
               return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var4 = (HttpResponse)var6.join();
         } else if (var2 instanceof Buffer) {
            var6 = Request.send((HttpRequest)var1.get(), (Buffer)var2);
            if (!var6.isDone()) {
               var5 = var6;
               return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var4 = (HttpResponse)var6.join();
         } else if (var2 instanceof MultiMap) {
            var6 = Request.send((HttpRequest)var1.get(), (MultiMap)var2);
            if (!var6.isDone()) {
               var5 = var6;
               return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var4 = (HttpResponse)var6.join();
         } else {
            var4 = null;
         }

         if (var4 != null) {
            if (((HttpRequestImpl)var1.get()).uri().contains("b/g")) {
               return CompletableFuture.completedFuture((Object)null);
            }

            return CompletableFuture.completedFuture(var4.bodyAsJsonObject());
         }

         this.logger.warn("Retrying...");
         var6 = VertxUtil.randomSleep(2500L);
         if (!var6.isDone()) {
            var5 = var6;
            return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
         }

         var6.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public PXTokenAPI(TaskActor var1) {
      super(var1, ClientType.PX_SDK_PIXEL_3);
      this.apiClient = RealClientFactory.build(var1.getVertx(), ClientType.PX_SDK_PIXEL_3);
      this.requestTime = 0L;
   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      this.logger.info("Solving captcha via api");

      for(int var4 = 0; var4 < 5; ++var4) {
         CompletableFuture var10000;
         CompletableFuture var16;
         try {
            var10000 = this.requestExecutor(this::lambda$solveCaptcha$1, (new JsonObject()).put("vid", var1).put("sid", var2).put("uuid", var3).toBuffer());
            if (!var10000.isDone()) {
               var16 = var10000;
               return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
            }

            JsonObject var5 = (JsonObject)var10000.join();
            if (var5 != null && var5.containsKey("result")) {
               this.requestTime = var5.getLong("ts");
               var10000 = this.handleBundle(PXTokenAPI::lambda$solveCaptcha$2);
               if (!var10000.isDone()) {
                  var16 = var10000;
                  return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
               }

               JsonObject var6 = (JsonObject)var10000.join();
               if (var6 != null && var6.containsKey("do")) {
                  JsonObject var7 = (new JsonObject()).put("a", var6).put("b", var3).put("c", this.requestTime).put("vid", var1).put("sid", var2).put("uuid", var3);
                  var10000 = this.requestExecutor(this::lambda$solveCaptcha$3, var7.toBuffer());
                  if (!var10000.isDone()) {
                     var16 = var10000;
                     return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                  }

                  JsonObject var8 = (JsonObject)var10000.join();
                  if (var8 != null) {
                     MultiMap var9 = this.jsonToForm(PXTokenAPI::lambda$solveCaptcha$4);
                     var10000 = this.requestExecutor(this::lambda$solveCaptcha$5, (Object)null);
                     if (!var10000.isDone()) {
                        var16 = var10000;
                        return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                     }

                     var10000.join();
                     var10000 = this.requestExecutor(this::lambda$solveCaptcha$6, var7.toBuffer());
                     if (!var10000.isDone()) {
                        var16 = var10000;
                        return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                     }

                     JsonObject var10 = (JsonObject)var10000.join();
                     if (var10 != null) {
                        var10000 = this.handleBundle(PXTokenAPI::lambda$solveCaptcha$7);
                        if (!var10000.isDone()) {
                           var16 = var10000;
                           return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                        }

                        JsonObject var11 = (JsonObject)var10000.join();
                        if (var11 != null && var11.containsKey("do")) {
                           var10000 = this.requestExecutor(this::lambda$solveCaptcha$8, var7.toBuffer());
                           if (!var10000.isDone()) {
                              var16 = var10000;
                              return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                           }

                           JsonObject var12 = (JsonObject)var10000.join();
                           if (var12 != null) {
                              int var13 = (int)(var12.getDouble("delay") * Double.longBitsToDouble(4652007308841189376L));
                              this.logger.info("Sleep for {}", var13);
                              var10000 = VertxUtil.hardCodedSleep((long)var13);
                              if (!var10000.isDone()) {
                                 var16 = var10000;
                                 return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                              }

                              var10000.join();
                              var10000 = this.handleBundle(PXTokenAPI::lambda$solveCaptcha$9);
                              if (!var10000.isDone()) {
                                 var16 = var10000;
                                 return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
                              }

                              JsonObject var14 = (JsonObject)var10000.join();
                              if (var14 != null && var14.containsKey("do")) {
                                 System.out.println(var14);
                                 Optional var15 = this.parseCapResult(var14.toString());
                                 if (var15.isPresent()) {
                                    super.value = var15.get();
                                    System.out.println(var14);
                                    System.out.println((String)super.value);
                                    return CompletableFuture.completedFuture(true);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (Throwable var17) {
            var17.printStackTrace();
            this.logger.error("Error occurred solving cap: {}", var17.getMessage());
            var10000 = VertxUtil.hardCodedSleep(5000L);
            if (!var10000.isDone()) {
               var16 = var10000;
               return var16.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$solveCaptcha);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(true);
   }

   public CompletableFuture handleBundle(Supplier var1) {
      MultiMap var2 = this.jsonToForm(var1);
      return this.requestExecutor(this::bundleReq, var2);
   }

   public HttpRequest bundleReq() {
      HttpRequest var1 = this.client.postAbs("https://collector-" + "PXAJDckzHD".toLowerCase() + ".px-cloud.net/assets/js/bundle").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"88\", \"Chromium\";v=\"88\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.hibbett.com");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.hibbett.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7");
      return var1;
   }

   public HttpRequest apiRequest(int var1, String var2) {
      HttpRequest var3 = this.client.postAbs("https://px.hwkapi.com/px/" + var1).addQueryParam("domain", var2).addQueryParam("appId", "PXAJDckzHD").addQueryParam("auth", "test_67b74d34-d55b-4193-aa89-3dde8e1713a6").as(BodyCodec.buffer());
      var3.putHeader("User-Agent", "python-requests/2.24.0");
      var3.putHeader("Accept-Encoding", "gzip, deflate");
      var3.putHeader("Accept", "*/*");
      var3.putHeader("Connection", "keep-alive");
      var3.putHeader("Content-Length", "DEFAULT_VALUE");
      var3.putHeader("Content-Type", "application/json");
      return var3;
   }

   public HttpRequest lambda$solveCaptcha$8() {
      return this.apiCaptchaRequest(2, "https://www.hibbett.com/");
   }

   public HttpRequest imageReq(MultiMap var1) {
      StringBuilder var2 = new StringBuilder();
      var1.forEach(PXTokenAPI::lambda$imageReq$0);
      RealClient var10000 = this.client;
      String var10001 = "PXAJDckzHD".toLowerCase();
      HttpRequest var3 = var10000.getAbs("https://collector-" + var10001 + ".perimeterx.net/b/g?" + var2).as(BodyCodec.buffer());
      var3.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36");
      var3.putHeader("accept", "*/*");
      var3.putHeader("origin", "https://www.hibbett.com");
      var3.putHeader("sec-fetch-site", "cross-site");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", "https://www.hibbett.com/");
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7");
      return var3;
   }

   public CompletableFuture initialize() {
      return CompletableFuture.completedFuture(true);
   }

   public Optional parseCapResult(String var1) {
      if (!var1.contains("cv|0")) {
         return Optional.empty();
      } else {
         Matcher var2 = BAKE_PATTERN.matcher(var1);
         return var2.find() ? Optional.of("3:" + var2.group(1)) : Optional.empty();
      }
   }

   public HttpRequest lambda$solveCaptcha$1() {
      return this.apiCaptchaRequest(1, "https://www.hibbett.com/");
   }

   public static CompletableFuture async$requestExecutor(PXTokenAPI var0, Supplier var1, Object var2, int var3, CompletableFuture var4, HttpResponse var5, int var6, Object var7) {
      CompletableFuture var10000;
      HttpResponse var8;
      CompletableFuture var9;
      switch (var6) {
         case 0:
            var3 = 0;
            break;
         case 1:
            var8 = (HttpResponse)var4.join();
            if (var8 != null) {
               if (((HttpRequestImpl)var1.get()).uri().contains("b/g")) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               return CompletableFuture.completedFuture(var8.bodyAsJsonObject());
            }

            var0.logger.warn("Retrying...");
            var10000 = VertxUtil.randomSleep(2500L);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var10000.join();
            break;
         case 2:
            var8 = (HttpResponse)var4.join();
            if (var8 != null) {
               if (((HttpRequestImpl)var1.get()).uri().contains("b/g")) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               return CompletableFuture.completedFuture(var8.bodyAsJsonObject());
            }

            var0.logger.warn("Retrying...");
            var10000 = VertxUtil.randomSleep(2500L);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var10000.join();
            break;
         case 3:
            var8 = (HttpResponse)var4.join();
            if (var8 != null) {
               if (((HttpRequestImpl)var1.get()).uri().contains("b/g")) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               return CompletableFuture.completedFuture(var8.bodyAsJsonObject());
            }

            var0.logger.warn("Retrying...");
            var10000 = VertxUtil.randomSleep(2500L);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var10000.join();
            break;
         case 4:
            var4.join();
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var0.client.isActive() && var3++ <= 100) {
         if (var2 == null) {
            var10000 = Request.send((HttpRequest)var1.get());
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var8 = (HttpResponse)var10000.join();
         } else if (var2 instanceof Buffer) {
            var10000 = Request.send((HttpRequest)var1.get(), (Buffer)var2);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var8 = (HttpResponse)var10000.join();
         } else if (var2 instanceof MultiMap) {
            var10000 = Request.send((HttpRequest)var1.get(), (MultiMap)var2);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
            }

            var8 = (HttpResponse)var10000.join();
         } else {
            var8 = null;
         }

         if (var8 != null) {
            if (((HttpRequestImpl)var1.get()).uri().contains("b/g")) {
               return CompletableFuture.completedFuture((Object)null);
            }

            return CompletableFuture.completedFuture(var8.bodyAsJsonObject());
         }

         var0.logger.warn("Retrying...");
         var10000 = VertxUtil.randomSleep(2500L);
         if (!var10000.isDone()) {
            var9 = var10000;
            return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI::async$requestExecutor);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public HttpRequest lambda$solveCaptcha$3() {
      return this.apiCaptchaRequest(15, "https://www.hibbett.com/");
   }

   public CompletableFuture reInit() {
      return CompletableFuture.completedFuture(true);
   }

   public CompletableFuture awaitInit() {
      return super.initFuture;
   }

   public static JsonObject lambda$solveCaptcha$7(JsonObject var0) {
      return var0;
   }

   public CompletableFuture handleToken(Supplier var1) {
      MultiMap var2 = this.jsonToForm(var1);
      return this.requestExecutor(this::collectorRequest, var2);
   }

   public static JsonObject lambda$solveCaptcha$9(JsonObject var0) {
      return var0.getJsonObject("result");
   }

   public HttpRequest lambda$solveCaptcha$5(MultiMap var1) {
      return this.imageReq(var1);
   }

   public CompletableFuture solveCaptcha(String var1, String var2) {
      return CompletableFuture.completedFuture(true);
   }

   public static JsonObject lambda$solveCaptcha$2(JsonObject var0) {
      return var0.getJsonObject("result");
   }

   public HttpRequest collectorRequest() {
      String var1 = "PerimeterX Android SDK/" + "v1.13.2".substring(1);
      HttpRequest var2 = this.client.postAbs("https://collector-" + "PX9Qx3Rve4".toLowerCase() + ".perimeterx.net/api/v1/collector/mobile").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.buffer());
      var2.putHeaders(Headers$Pseudo.MPAS.get());
      var2.putHeader("user-agent", var1);
      var2.putHeader("content-type", "application/x-www-form-urlencoded");
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("accept-encoding", "gzip");
      return var2;
   }

   public static JsonObject lambda$solveCaptcha$4(JsonObject var0) {
      return var0;
   }

   public HttpRequest lambda$solveCaptcha$6() {
      return this.apiRequest(2, "https://www.hibbett.com/");
   }

   public HttpRequest apiCaptchaRequest(int var1, String var2) {
      HttpRequest var3 = this.client.postAbs("https://px.hwkapi.com/px/captcha/" + var1).addQueryParam("domain", var2).addQueryParam("appId", "PXAJDckzHD").addQueryParam("auth", "test_67b74d34-d55b-4193-aa89-3dde8e1713a6").as(BodyCodec.buffer());
      var3.putHeader("User-Agent", "python-requests/2.24.0");
      var3.putHeader("Accept-Encoding", "gzip, deflate");
      var3.putHeader("Accept", "*/*");
      var3.putHeader("Connection", "keep-alive");
      var3.putHeader("Content-Length", "DEFAULT_VALUE");
      var3.putHeader("Content-Type", "application/json");
      return var3;
   }

   public String getVid() {
      return "null";
   }

   public static void lambda$imageReq$0(StringBuilder var0, Map.Entry var1) {
      if (var0.length() > 0) {
         var0.append("&");
      }

      if (((String)var1.getValue()).contains("\udd31")) {
         var0.append((String)var1.getKey()).append("=").append(URLEncoder.encode((String)var1.getValue(), StandardCharsets.UTF_8));
      } else {
         var0.append((String)var1.getKey()).append("=").append((String)var1.getValue());
      }

   }

   public Optional parseResult(String var1) {
      Matcher var2 = BAKE_PATTERN.matcher(var1);
      return var2.find() ? Optional.of("3:" + var2.group(1)) : Optional.empty();
   }

   public MultiMap jsonToForm(Supplier var1) {
      JsonObject var2 = (JsonObject)var1.get();
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      Iterator var4 = var2.fieldNames().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         String var6 = var2.getString(var5, (String)null);
         if (var6 != null) {
            if (this.uuid == null && var5.equals("uuid")) {
               this.uuid = var6;
               this.logger.info("UUID -> {}", this.uuid);
            }

            if (var5.equals("appId")) {
               var3.add(var5, "PXAJDckzHD");
            } else {
               var3.add(var5, var6);
            }
         }
      }

      return var3;
   }

   public boolean isTokenCaptcha() {
      return false;
   }

   public static CompletableFuture async$solveCaptcha(PXTokenAPI param0, String param1, String param2, String param3, int param4, CompletableFuture param5, JsonObject param6, JsonObject param7, JsonObject param8, JsonObject param9, MultiMap param10, JsonObject param11, JsonObject param12, JsonObject param13, int param14, Throwable param15, int param16, Object param17) {
      // $FF: Couldn't be decompiled
   }
}

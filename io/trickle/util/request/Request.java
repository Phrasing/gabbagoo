package io.trickle.util.request;

import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import io.trickle.util.concurrent.VertxUtil;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.multipart.MultipartForm;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Request {
   public static int DEFAULT_DELAY = 3000;

   public static CompletableFuture executeTillOk(HttpRequest var0, int var1) {
      return execute(var0, true, var1);
   }

   public static CompletableFuture executeWithResponse(HttpRequest var0, boolean var1, int var2) {
      int var3 = 0;

      do {
         try {
            CompletableFuture var10000 = send(var0);
            CompletableFuture var5;
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Request::async$executeWithResponse);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               return CompletableFuture.completedFuture(var4);
            }

            if (var1 != 0) {
               var10000 = VertxUtil.sleep((long)var2);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(Request::async$executeWithResponse);
               }

               var10000.join();
            }
         } catch (Exception var6) {
         }

         ++var3;
      } while(var1 != 0 && var3 < 3);

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture executeTillOk(HttpRequest var0, Buffer var1) {
      return execute(var0, true, 3000, var1);
   }

   public static CompletableFuture executeTillOk(HttpRequest var0) {
      return execute(var0, true, 3000);
   }

   public static CompletableFuture send(HttpRequest var0) {
      return var0.send().toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture send(HttpRequest var0, JsonObject var1) {
      return var0.sendJson(var1).toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture execute(HttpRequest var0, boolean var1, int var2, Buffer var3) {
      int var4 = 0;

      do {
         try {
            CompletableFuture var10000 = var3 == null ? send(var0) : send(var0, var3);
            CompletableFuture var6;
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Request::async$execute$1);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               return CompletableFuture.completedFuture(var5.body());
            }

            if (var1 != 0) {
               var10000 = VertxUtil.sleep((long)var2);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Request::async$execute$1);
               }

               var10000.join();
            }
         } catch (Throwable var7) {
            ++var4;
         }
      } while(var1 != 0 && var4 < 5);

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$executeWithResponse(HttpRequest param0, int param1, int param2, int param3, CompletableFuture param4, HttpResponse param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static String getURI(HttpRequest var0) {
      try {
         if (var0.headers().contains("referer")) {
            return var0.headers().get("referer");
         } else {
            HttpRequestImpl var1 = (HttpRequestImpl)var0;
            return String.format("https://%s%s", var1.host(), var1.uri());
         }
      } catch (Throwable var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static CompletableFuture send(HttpRequest var0, MultipartForm var1) {
      return var0.sendMultipartForm(var1).toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture execute(HttpRequest var0, boolean var1, int var2) {
      return execute(var0, var1, var2, (Buffer)null);
   }

   public static CompletableFuture execute(HttpRequest var0, int var1) {
      for(int var2 = 1; var2 <= var1; ++var2) {
         try {
            CompletableFuture var10000 = send(var0);
            if (!var10000.isDone()) {
               CompletableFuture var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Request::async$execute);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               return CompletableFuture.completedFuture(var3.body());
            }
         } catch (Exception var5) {
            if (var2 == var1 && var5.getMessage().contains("HttpProxyConnectException")) {
               return CompletableFuture.failedFuture(new Exception("Invalid Proxy!"));
            }
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to execute request"));
   }

   public static CompletableFuture async$execute$1(HttpRequest param0, int param1, int param2, Buffer param3, int param4, CompletableFuture param5, HttpResponse param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static RequestOptions convertToVertx(InterceptUrlRequestCallback.Params var0) {
      MultiMap var1 = MultiMap.caseInsensitiveMultiMap();
      Iterator var2 = var0.httpHeaders().iterator();

      while(var2.hasNext()) {
         HttpHeader var3 = (HttpHeader)var2.next();
         var1.add(var3.name(), var3.value());
      }

      return (new RequestOptions()).setMethod(var0.urlRequest().method().equalsIgnoreCase("get") ? HttpMethod.GET : HttpMethod.POST).setAbsoluteURI(var0.urlRequest().url()).setHeaders(var1);
   }

   public static CompletableFuture send(HttpRequest var0, Buffer var1) {
      return var0.sendBuffer(var1).toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture send(HttpRequest var0, JsonArray var1) {
      return var0.sendJson(var1).toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture send(HttpRequest var0, MultiMap var1) {
      return var0.sendForm(var1).toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture send(HttpRequest var0, Object var1) {
      if (var1 instanceof Buffer) {
         return send(var0, (Buffer)var1);
      } else if (var1 instanceof JsonObject) {
         return send(var0, (JsonObject)var1);
      } else if (var1 instanceof JsonArray) {
         return send(var0, (JsonArray)var1);
      } else if (var1 instanceof MultiMap) {
         return send(var0, (MultiMap)var1);
      } else {
         return var1 instanceof MultipartForm ? send(var0, (MultipartForm)var1) : CompletableFuture.failedFuture(new Exception("Bad body type."));
      }
   }

   public static CompletableFuture async$execute(HttpRequest param0, int param1, int param2, CompletableFuture param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture send(HttpRequest var0, Json var1) {
      return var0.sendJson(var1).toCompletionStage().toCompletableFuture();
   }

   public static CompletableFuture execute(HttpRequest var0) {
      return execute(var0, false, 3000);
   }
}

package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.input.EnterMouseCallback;
import com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback;
import com.teamdev.jxbrowser.browser.callback.input.ReleaseMouseCallback;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.cookie.Cookie;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.navigation.event.FrameLoadFinished;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.UploadData;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback.Response;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import com.teamdev.jxbrowser.net.proxy.DirectProxyConfig;
import com.teamdev.jxbrowser.ui.MouseButton;
import com.teamdev.jxbrowser.ui.Point;
import com.teamdev.jxbrowser.ui.event.MouseMoved;
import com.teamdev.jxbrowser.ui.event.MousePressed;
import com.teamdev.jxbrowser.ui.event.MouseReleased;
import io.trickle.core.Controller;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.ProxyController;
import io.trickle.util.Storage;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Harvester {
   public WindowedBrowser browser;
   public Harvester$SolveFunction solveFunction;
   public static Pattern V2_TYPE_PATTERN = Pattern.compile("<strong style=\"font-size: ..px;\">(.*?)<");
   public Character hotkey = null;
   public static ExecutorService executorService = Executors.newFixedThreadPool(Storage.getHarvesterCountYs());
   public SolveFuture solveFuture;
   public static AtomicInteger harvesterCount = new AtomicInteger(0);
   public ExecutorService executor = Executors.newSingleThreadExecutor();
   public int number;
   public String siteURL;
   public AtomicBoolean started = new AtomicBoolean(false);
   public Engine engine;
   public static String HARVESTER_UA;
   public Proxy proxy;
   public AtomicInteger referenceCount = new AtomicInteger(0);
   public ByteArrayOutputStream byteArrayInputStream;
   public JsonObject rect;
   public boolean preserved = true;
   public AtomicReference latch = new AtomicReference();
   public AtomicInteger clickCounter = new AtomicInteger(0);
   public static AtomicBoolean isAssistedOn = new AtomicBoolean(false);
   public int reloads = 0;
   public CompletableFuture ready;
   public AtomicReference tokenHolder = new AtomicReference((Object)null);
   public static Logger logger = LogManager.getLogger(Harvester.class);

   public static BeforeUrlRequestCallback.Response lambda$setInterceptors$7(BeforeUrlRequestCallback.Params var0) {
      return var0.urlRequest().url().contains("recaptcha.net") ? Response.redirect(var0.urlRequest().url().replace("recaptcha.net", "google.com")) : Response.proceed();
   }

   public void lambda$setInterceptors$0() {
      logger.info("Clicking box");
      if (this.rect != null) {
         this.clickLocationSlide(this.rect.getInteger("left") + 35 + ThreadLocalRandom.current().nextInt(-9, 9), this.rect.getInteger("top") + this.rect.getInteger("height") / 2 + ThreadLocalRandom.current().nextInt(-9, 9));
      } else {
         logger.error("Fallback click");
         this.clickLocationSlide(ThreadLocalRandom.current().nextInt(55, 70), ThreadLocalRandom.current().nextInt(110, 115));
      }

   }

   public void submitClick() {
      this.clickLocation(268, 518);
   }

   public EngineOptions.Builder baseOpts() {
      return EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, new Harvester$RequestInterceptor(this));
   }

   public CompletableFuture init() {
      this.number = harvesterCount.incrementAndGet();
      Path var1 = Paths.get(Storage.CONFIG_PATH + "/harvester" + this.number);
      String var2 = "harvesterinit";
      synchronized("harvesterinit") {
         while(this.engine == null) {
            if (this.preserved) {
               try {
                  if (!Files.exists(var1, new LinkOption[0])) {
                     this.preserved = false;
                  }

                  this.engine = Engine.newInstance(this.baseOpts().userDataDir(var1).build());
               } catch (Throwable var7) {
                  logger.warn("Unable to locate chrome. Please wait: {}", var7.getMessage());

                  try {
                     if (this.engine != null) {
                        this.engine.close();
                     }
                  } catch (Throwable var6) {
                     logger.warn("Error loading cached data backup: {}", var7.getMessage());
                     this.sleep(5000);
                  }
               }
            } else {
               this.engine = Engine.newInstance(this.baseOpts().build());
            }
         }
      }

      this.browser = new WindowedBrowser(this.engine);
      this.browser.createWindow();
      this.setInterceptors();
      this.setProxy();
      this.waitForLogin();
      this.startSolver();
      if (HARVESTER_UA == null) {
         HARVESTER_UA = this.browser.browser().userAgent();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public ReleaseMouseCallback.Response lambda$setInterceptors$5(ReleaseMouseCallback.Params var1) {
      if (var1.event().toString().contains("key_modifiers")) {
         int var2 = this.clickCounter.incrementAndGet();
         if (isAssistedOn.get() && var2 == 3) {
            executorService.submit(this::submitClick);
         }
      } else {
         this.resetClickCounter();
      }

      return com.teamdev.jxbrowser.browser.callback.input.ReleaseMouseCallback.Response.proceed();
   }

   public static String captchaReadyCallback() {
      return "function elementReady(selector) {\n  return new Promise((resolve, reject) => {\n    const el = document.querySelector(selector);\n    if (el) {resolve(el);}\n    new MutationObserver((mutationRecords, observer) => {\n      // Query for elements matching the specified selector\n      Array.from(document.querySelectorAll(selector)).forEach((element) => {\n        resolve(element);\n        //Once we have resolved we don't need the observer anymore.\n        observer.disconnect();\n      });\n    })\n      .observe(document.documentElement, {\n        childList: true,\n        subtree: true\n      });\n  });\n}\n\nelementReady('#recaptcha-anchor').then(selector =>{ \n   console.log(20022002);    \n});\nelementReady('#checkbox').then(selector => { \n   console.log(20022002);    \n});";
   }

   public void transferCookies(String var1) {
      List var2 = this.engine.cookieStore().cookies(var1);
      CookieJar var3 = ((CaptchaToken)this.tokenHolder.get()).getCookieJar();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Cookie var5 = (Cookie)var4.next();

         try {
            var3.put(var5.name(), var5.value(), var5.domain());
         } catch (Exception var7) {
         }
      }

   }

   public static String captchaPageV2() {
      return "<html>\n    <div class=\"shopify-challenge__container\">\n        <script>\n            //<![CDATA[\n            var onCaptchaSuccess = function() {\n                var event;\n\n                try {\n                    event = new Event('captchaSuccess', {bubbles: true, cancelable: true});\n                } catch (e) {\n                    event = document.createEvent('Event');\n                    event.initEvent('captchaSuccess', true, true);\n                }\n\n                window.dispatchEvent(event);\n            }\n\n            //]]>\n        </script>\n        <script>\n            //<![CDATA[\n            window.addEventListener('captchaSuccess', function() {\n                var responseInput = document.querySelector('.g-recaptcha-response');\n                var submitButton = document.querySelector('.dialog-submit');\n\n                if (submitButton instanceof HTMLElement) {\n                    var needResponse = (responseInput instanceof HTMLElement);\n                    var responseValueMissing = !responseInput.value;\n                    submitButton.disabled = (needResponse && responseValueMissing);\n                }\n            }, false);\n\n            //]]>\n        </script>\n        <script>\n            //<![CDATA[\n            var recaptchaCallback = function() {\n                grecaptcha.render('g-recaptcha', {\n                    sitekey: \"%s\",\n                    size: (window.innerWidth > 320) ? 'normal' : 'compact',\n                    callback: 'onCaptchaSuccess',\n                });\n            };\n\n            //]]>\n        </script>\n        <script src=\"https://www.recaptcha.net/recaptcha/api.js?onload=recaptchaCallback&amp;render=6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9&amp;hl=en\" async=\"async\">\n            //<![CDATA[\n\n            //]]>\n        </script>\n        <noscript><div class=\"g-recaptcha-nojs\"><iframe class=\"g-recaptcha-nojs__iframe\" frameborder=\"0\" scrolling=\"no\" src=\"https://www.google.com/recaptcha/api/fallback?k=%s\"></iframe><div class=\"g-recaptcha-nojs__input-wrapper\"><textarea id=\"g-recaptcha-response\" name=\"g-recaptcha-response\" class=\"g-recaptcha-nojs__input\">\n</textarea></div></div></noscript>\n        <script>\n            new Promise(resolve => {\n                window.addEventListener('captchaSuccess', () => {\n                    const token = grecaptcha.getResponse();\n                    window.completion.completed(token);\n                }, false);\n            });\n        </script>\n        <div id=\"g-recaptcha\" class=\"g-recaptcha\"></div>\n    </div>\n</html>";
   }

   public void clickLocation(int var1, int var2) {
      Point var3 = Point.of(ThreadLocalRandom.current().nextInt(var1 - 2, var1 + 2), ThreadLocalRandom.current().nextInt(var2 - 2, var2 + 2));
      MousePressed var4 = MousePressed.newBuilder(var3).button(MouseButton.PRIMARY).clickCount(1).build();
      MouseReleased var5 = MouseReleased.newBuilder(var3).button(MouseButton.PRIMARY).clickCount(1).build();
      MouseMoved var6 = MouseMoved.newBuilder(var3).build();
      this.browser.browser().dispatch(var6);
      this.browser.browser().dispatch(var4);
      this.browser.browser().dispatch(var5);
   }

   public void resetClickCounter() {
      this.clickCounter.set(0);
   }

   public void sleep(int var1) {
      try {
         Thread.sleep((long)var1);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   public void setInterceptors() {
      this.browser.browser().on(ConsoleMessageReceived.class, this::lambda$setInterceptors$1);
      this.browser.browser().navigation().on(FrameLoadFinished.class, this::lambda$setInterceptors$3);
      this.browser.browser().set(EnterMouseCallback.class, this::lambda$setInterceptors$4);
      this.browser.browser().set(ReleaseMouseCallback.class, this::lambda$setInterceptors$5);
      this.browser.browser().set(PressKeyCallback.class, this::lambda$setInterceptors$6);
      this.engine.network().set(BeforeUrlRequestCallback.class, Harvester::lambda$setInterceptors$7);
   }

   public CompletableFuture start() {
      this.referenceCount.incrementAndGet();

      try {
         if (!this.started.get()) {
            if (this.ready == null) {
               this.ready = new ContextCompletableFuture();
            }

            this.executor.submit(this::init);
            logger.debug("Waiting to start!");
            CompletableFuture var10000 = this.ready;
            if (!var10000.isDone()) {
               CompletableFuture var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Harvester::async$start);
            }

            var10000.join();
            this.started.set(true);
            logger.debug("Started!");
         }
      } catch (Throwable var3) {
         logger.error("Start error on harvester {}", var3.getMessage());
      }

      return CompletableFuture.completedFuture(true);
   }

   public void waitForLogin() {
      if (!this.preserved) {
         this.executor.submit(this::lambda$waitForLogin$9);
         this.browser.browser().navigation().loadUrl("https://accounts.google.com/signin/v2/identifier?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
      } else {
         this.browser.browser().navigation().loadUrl("https://www.google.com/");
         this.ready.complete(true);
      }

   }

   public void setProxy() {
      this.proxy = ((ProxyController)io.trickle.core.Engine.get().getModule(Controller.PROXY_CAPTCHA)).getProxy(this.number - 1);
      if (this.proxy != null) {
         String var1 = this.setProxy(this.proxy);
         logger.info("Using proxy: {}", var1);
         this.browser.setTitle(this.proxy.getHost() == null ? "local" : this.proxy.toString());
      }

   }

   public void lambda$setInterceptors$1(ConsoleMessageReceived var1) {
      ConsoleMessage var2 = var1.consoleMessage();
      String var3 = var2.message();
      if (var3.indexOf("03") == 0) {
         try {
            CaptchaToken var4 = (CaptchaToken)this.tokenHolder.get();
            if (var4 != null) {
               var4.setTokenValues(var3);
               logger.info("Received token [V3]");
               this.tokenHolder.set((Object)null);
               SolveFuture var5 = this.solveFuture;
               if (var5 != null && !var5.isDone()) {
                  var5.complete(var4);
               }

               ((CountDownLatch)this.latch.get()).countDown();
            }
         } catch (Throwable var6) {
            var6.printStackTrace();
         }
      } else if (var3.equals("20022002")) {
         executorService.submit(this::lambda$setInterceptors$0);
      } else if (var3.startsWith("{\"x\":")) {
         this.rect = new JsonObject(var3);
      }

   }

   public void lambda$setInterceptors$3(FrameLoadFinished var1) {
      String var2 = var1.url();
      if (this.solveFuture != null && var2.contains("recaptcha/api2/anchor") || var2.contains("hcaptcha.com/")) {
         executorService.submit(this::lambda$setInterceptors$2);
      }

   }

   public CompletableFuture send(Supplier var1, Optional var2) {
      int var3 = 0;

      while(var3++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var10;
         try {
            RequestOptions var4 = (RequestOptions)var1.get();
            HttpRequest var9 = ((CaptchaToken)this.tokenHolder.get()).client.request(var4.getMethod(), var4);
            HttpResponse var11;
            if (var2.isEmpty()) {
               var10 = Request.send(var9);
               if (!var10.isDone()) {
                  var7 = var10;
                  return var7.exceptionally(Function.identity()).thenCompose(Harvester::async$send);
               }

               var11 = (HttpResponse)var10.join();
            } else {
               var10 = Request.send(var9, Buffer.buffer(((UploadData)var2.get()).bytes()));
               if (!var10.isDone()) {
                  var7 = var10;
                  return var7.exceptionally(Function.identity()).thenCompose(Harvester::async$send);
               }

               var11 = (HttpResponse)var10.join();
            }

            HttpResponse var6 = var11;
            if (var6 != null) {
               return CompletableFuture.completedFuture(var6);
            }

            logger.error("No response {}", var4.getURI());
         } catch (Throwable var8) {
            String var5 = var8.getMessage();
            if (var5.contains("timeout period")) {
               logger.error("request timeout. no response");
            } else if (var5.contains("proxy")) {
               logger.error("unable to connect to proxy - {}", var5);
            } else {
               logger.error("network error " + var8.getMessage());
            }
         }

         var10 = VertxUtil.randomSleep(1000L);
         if (!var10.isDone()) {
            var7 = var10;
            return var7.exceptionally(Function.identity()).thenCompose(Harvester::async$send);
         }

         var10.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$send(Harvester param0, Supplier param1, Optional param2, int param3, RequestOptions param4, HttpRequest param5, CompletableFuture param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public String setProxy(Proxy var1) {
      if (var1.isLocal()) {
         this.engine.proxy().config(DirectProxyConfig.newInstance());
         return "no-proxy";
      } else {
         String[] var2 = var1.toParams();
         this.engine.network().set(AuthenticateCallback.class, Harvester::lambda$setProxy$11);
         this.engine.proxy().config(CustomProxyConfig.newInstance(String.format("http=%s:%s;https=%s:%s", var2[0], var2[1], var2[0], var2[1])));
         return Arrays.toString(var2);
      }
   }

   public static void lambda$waitForLogin$8(Frame var0) {
      var0.executeJavaScript("location.href = \"https://www.youtube.com/\"");
   }

   public PressKeyCallback.Response lambda$setInterceptors$6(PressKeyCallback.Params var1) {
      if (this.hotkey != null && var1.event().keyChar() == this.hotkey) {
         executorService.submit(this::submitClick);
      }

      return com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback.Response.proceed();
   }

   public void lambda$setInterceptors$2(FrameLoadFinished var1) {
      Optional var2 = this.browser.browser().mainFrame();
      var1.frame().executeJavaScript(captchaReadyCallback());
      if (var2.isPresent()) {
         JsObject var3 = (JsObject)((Frame)var2.get()).executeJavaScript("window");
         if (var3 != null) {
            var3.putProperty("completion", this.solveFunction);
         }
      } else {
         logger.error("No browser frame available");
      }

   }

   public static CompletableFuture async$start(Harvester var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var8;
      label37: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var2) {
            case 0:
               var0.referenceCount.incrementAndGet();

               try {
                  if (var0.started.get()) {
                     return CompletableFuture.completedFuture(true);
                  }

                  if (var0.ready == null) {
                     var0.ready = new ContextCompletableFuture();
                  }

                  var0.executor.submit(var0::init);
                  logger.debug("Waiting to start!");
                  var10000 = var0.ready;
                  if (!var10000.isDone()) {
                     CompletableFuture var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Harvester::async$start);
                  }
                  break;
               } catch (Throwable var5) {
                  var8 = var5;
                  var10001 = false;
                  break label37;
               }
            case 1:
               var10000 = var1;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            var10000.join();
            var0.started.set(true);
            logger.debug("Started!");
            return CompletableFuture.completedFuture(true);
         } catch (Throwable var4) {
            var8 = var4;
            var10001 = false;
         }
      }

      Throwable var6 = var8;
      logger.error("Start error on harvester {}", var6.getMessage());
      return CompletableFuture.completedFuture(true);
   }

   public void lambda$startSolver$10() {
      while(!Thread.currentThread().isInterrupted()) {
         try {
            this.latch.set(new CountDownLatch(1));
            this.solveFuture = ((TokenController)io.trickle.core.Engine.get().getModule(Controller.TOKEN)).pollWaitingList();
            this.solveFunction = new Harvester$SolveFunction(this.solveFuture, this.latch);
            logger.info("Attempting solve");
            this.resetClickCounter();
            CaptchaToken var1 = this.solveFuture.getEmptyCaptchaToken();
            this.tokenHolder.set(var1);
            if (var1.isCheckpoint()) {
               this.configureCheckpointDetails();
            } else {
               this.setProxy(this.proxy);
            }

            ++this.reloads;
            this.siteURL = var1.getDomain();
            this.browser.browser().navigation().loadUrl(this.siteURL);
            ((CountDownLatch)this.latch.get()).await();
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

   }

   public void close() {
      this.referenceCount.decrementAndGet();
      if (this.referenceCount.get() == 0) {
         if (this.engine != null && !this.engine.isClosed()) {
            if (this.browser != null) {
               this.browser.close();
            }

            Iterator var1 = this.engine.browsers().iterator();

            while(var1.hasNext()) {
               Browser var2 = (Browser)var1.next();

               try {
                  if (!var2.isClosed()) {
                     var2.close();
                  }
               } catch (Throwable var4) {
               }
            }

            this.engine.close();
         }

         this.executor.shutdownNow();
      }

   }

   public EnterMouseCallback.Response lambda$setInterceptors$4(EnterMouseCallback.Params var1) {
      this.browser.frame().toFront();
      this.browser.frame().requestFocusInWindow();
      this.browser.browser().focus();
      return com.teamdev.jxbrowser.browser.callback.input.EnterMouseCallback.Response.proceed();
   }

   public static void lambda$setProxy$11(String[] var0, AuthenticateCallback.Params var1, AuthenticateCallback.Action var2) {
      if (var1.isProxy()) {
         logger.info("Enabling proxy");
         if (var0.length == 4) {
            var2.authenticate(var0[2], var0[3]);
         } else {
            var2.cancel();
         }
      } else {
         var2.cancel();
      }

   }

   public void lambda$waitForLogin$9() {
      boolean var1 = false;

      while(!Thread.currentThread().isInterrupted() && !var1) {
         try {
            Optional var2 = this.browser.browser().mainFrame();
            if (var2.isPresent()) {
               var1 = ((Frame)var2.get()).html().contains("compose");
            }

            this.sleep(2222);
         } catch (Throwable var4) {
            if (!this.engine.isClosed() && !this.browser.browser().isClosed()) {
               var4.printStackTrace();
            } else {
               this.ready.complete(false);
               this.close();
            }
         }
      }

      this.browser.frame().setVisible(false);
      this.browser.browser().navigation().loadUrl("https://www.google.com/search?q=youtube+videos");

      try {
         this.sleep(2000);
         this.browser.browser().mainFrame().ifPresent(Harvester::lambda$waitForLogin$8);
         this.sleep(1000);
         this.browser.frame().setVisible(true);
         this.ready.complete(true);
      } catch (Exception var3) {
         var3.printStackTrace();
         this.ready.complete(false);
      }

   }

   public void clickLocationSlide(int var1, int var2) {
      for(int var3 = 0; var3 < ThreadLocalRandom.current().nextInt(10, 30); ++var3) {
         Point var4 = Point.of(var1 + ThreadLocalRandom.current().nextInt(-20, 50), var2 + ThreadLocalRandom.current().nextInt(-20, 20));
         MouseMoved var5 = MouseMoved.newBuilder(var4).build();
         this.browser.browser().dispatch(var5);
         this.sleep(ThreadLocalRandom.current().nextInt(1, 3));
      }

      Point var7 = Point.of(ThreadLocalRandom.current().nextInt(var1 - 2, var1 + 2), ThreadLocalRandom.current().nextInt(var2 - 2, var2 + 2));
      MousePressed var8 = MousePressed.newBuilder(var7).button(MouseButton.PRIMARY).clickCount(1).build();
      MouseReleased var9 = MouseReleased.newBuilder(var7).button(MouseButton.PRIMARY).clickCount(1).build();
      MouseMoved var6 = MouseMoved.newBuilder(var7).build();
      this.browser.browser().dispatch(var6);
      this.browser.browser().dispatch(var8);
      this.sleep(ThreadLocalRandom.current().nextInt(1, 5));
      this.browser.browser().dispatch(var9);
   }

   public static String captchaPageV3() {
      return "<html>\n<body style=\"background-color:#002240;\">\n<header>\n    <h1 style=\"color:#FFFFFF;\">Trickle V3 ~ %d</span> </h1>\n</header>\n<main>\n    <script src=\"https://www.recaptcha.net/recaptcha/api.js?onload=storefrontContactFormsRecaptchaCallback&render=%s&hl=en\"></script>\n    <script>\n        grecaptcha.ready(function() {\n            grecaptcha.execute('%s', {action: '%s'}).then(function(token) {\n                console.log(token);\n            });\n        });\n    </script>\n</main>\n</body>\n</html>";
   }

   public void configureCheckpointDetails() {
      if (this.tokenHolder.get() != null && ((CaptchaToken)this.tokenHolder.get()).isHcaptcha) {
         CaptchaToken var1 = this.solveFuture.getEmptyCaptchaToken();
         this.setProxy(Proxy.fromArray(var1.getProxyStr().split(":")));
      } else {
         this.engine.proxy().config(DirectProxyConfig.newInstance());
      }

   }

   public void startSolver() {
      this.executor.submit(this::lambda$startSolver$10);
   }
}

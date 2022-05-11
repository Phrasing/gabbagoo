package io.trickle.harvester.pooled;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.ProprietaryFeature;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.engine.event.EngineCrashed;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.UrlRequestJob.Options;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback.Response;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import com.teamdev.jxbrowser.net.proxy.DirectProxyConfig;
import io.trickle.core.Controller;
import io.trickle.harvester.WindowedBrowser;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.ProxyController;
import io.trickle.util.Storage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrowserSharedHarvester extends AbstractVerticle implements SharedHarvester {
   public static Logger logger = LogManager.getLogger(BrowserSharedHarvester.class);
   public Promise solvePromise;
   public Proxy proxy;
   public WindowedBrowser browser;
   public List deferredReplies;
   public String sitekey;
   public String currentSiteURL;
   public HashMap referenceMap;
   public LongAdder passCounter;
   public long timerId = -1L;
   public LinkedHashMap requests;
   public SharedCaptchaToken currentToken;
   public int reloads;
   public ExecutorService executor;
   public int indexedId;
   public String harvesterId;
   public boolean ready;
   public String action;
   public Engine browserEngine;

   public void captchaRequestHandler(Message var1) {
      String var2 = (String)var1.body();
      if (var2 != null && !var2.isEmpty()) {
         SharedCaptchaToken var3 = (SharedCaptchaToken)this.referenceMap.get(var2);
         if (var3 != null && !var3.isExpired()) {
            var1.reply(var3);
         } else {
            this.requests.putIfAbsent(var2, new ArrayList());
            this.requests.computeIfPresent(var2, BrowserSharedHarvester::lambda$captchaRequestHandler$4);
         }
      }

   }

   public static void lambda$stop$10(Promise var0, AsyncResult var1) {
      var0.complete();
   }

   public InterceptUrlRequestCallback.Response lambda$initialiseBrowser$0(InterceptUrlRequestCallback.Params var1) {
      String var2 = var1.urlRequest().url();
      boolean var3 = var2.contains(".ico");
      if (var2.contains("https://www.google.com")) {
         return Response.proceed();
      } else if (!var3 && this.isSupportedV3Site(var2)) {
         UrlRequestJob var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).build());
         var4.write(String.format(captchaPageV3(), this.reloads, this.sitekey, this.sitekey, this.action).getBytes(StandardCharsets.UTF_8));
         var4.complete();
         return Response.intercept(var4);
      } else {
         return Response.proceed();
      }
   }

   public void initialiseBrowser(Promise var1) {
      logger.info("Initialising harvester instance...");

      try {
         this.browserEngine = Engine.newInstance(baseOpts().userDataDir(Paths.get(Storage.CONFIG_PATH + "/harvester-ys-" + this.indexedId)).addSwitch("--disable-site-isolation-trials").enableProprietaryFeature(ProprietaryFeature.AAC).enableProprietaryFeature(ProprietaryFeature.H_264).enableProprietaryFeature(ProprietaryFeature.WIDEVINE).addScheme(Scheme.HTTPS, this::lambda$initialiseBrowser$0).build());
      } catch (Throwable var3) {
         logger.warn("Error loading cached data: {}", var3.getMessage());
         if (this.browserEngine != null) {
            this.browserEngine.close();
            this.browserEngine = null;
         }

         var1.tryFail(var3);
      }

      this.browserEngine.on(EngineCrashed.class, this::lambda$initialiseBrowser$2);
      this.browser = new WindowedBrowser(this.browserEngine);
      this.browser.createWindow();
      this.setInterceptors();
      this.setProxy();
      this.waitForLogin(var1);
   }

   public void setProxy(String[] var1) {
      this.browserEngine.network().set(AuthenticateCallback.class, BrowserSharedHarvester::lambda$setProxy$11);
      this.browserEngine.proxy().config(CustomProxyConfig.newInstance(String.format("http=%s:%s;https=%s:%s", var1[0], var1[1], var1[0], var1[1])));
      logger.info("Using proxy: {}", Arrays.toString(var1));
   }

   public static EngineOptions.Builder baseOpts() {
      return EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ");
   }

   public void setInterceptors() {
      this.browser.browser().on(ConsoleMessageReceived.class, this::lambda$setInterceptors$9);
   }

   public void lambda$startSolveLoop$5(Long var1) {
      this.checkAndSolve();
   }

   public void waitForLogin(Promise var1) {
      this.executor.submit(this::lambda$waitForLogin$8);
      this.browser.browser().navigation().loadUrl("https://accounts.google.com/ServiceLogin?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1");
   }

   public void useLocal() {
      this.browserEngine.proxy().config(DirectProxyConfig.newInstance());
      this.browser.setTitle(this.indexedId, "local-ip");
   }

   public void handleSolved(SharedCaptchaToken var1) {
      try {
         Iterator var2 = ((List)this.requests.get(var1.getDomain())).iterator();
         this.requests.remove(var1.getDomain());
         this.referenceMap.put(var1.getDomain(), var1);

         for(; var2.hasNext(); var2.remove()) {
            String var3 = (String)var2.next();
            if (var3 != null && !var3.isEmpty()) {
               super.vertx.eventBus().send(var3, var1);
            }
         }
      } catch (Throwable var4) {
         logger.error("Error occurred handing solves: {}", var4.getMessage());
      }

   }

   public void stop(Promise var1) {
      super.vertx.executeBlocking(this::closeBrowsers).onComplete(BrowserSharedHarvester::lambda$stop$10);
   }

   public static List lambda$captchaRequestHandler$4(Message var0, String var1, List var2) {
      var2.add(var0.replyAddress());
      return var2;
   }

   public void lambda$initialiseBrowser$1() {
      this.closeBrowsers(Promise.promise());
      this.initialiseBrowser(Promise.promise());
   }

   public void lambda$waitForLogin$8(Promise var1) {
      boolean var2 = false;

      while(!Thread.currentThread().isInterrupted() && !var2) {
         try {
            Optional var3 = this.browser.browser().mainFrame();
            if (var3.isPresent()) {
               var2 = ((Frame)var3.get()).html().contains("compose");
            }

            Thread.sleep(2222L);
         } catch (Throwable var5) {
            if (!this.browserEngine.isClosed() && !this.browser.browser().isClosed()) {
               var5.printStackTrace();
            } else {
               this.closeBrowsers(Promise.promise());
            }
         }
      }

      this.browser.browser().navigation().loadUrl("https://www.google.com/search?q=funny+youtube+videos");

      try {
         Thread.sleep(3000L);
         this.browser.browser().mainFrame().ifPresent(BrowserSharedHarvester::lambda$waitForLogin$7);
         Thread.sleep(2000L);
         this.ready = true;
         logger.warn("Starting captcha poll");
         this.startSolveLoop();
         var1.tryComplete();
      } catch (Exception var4) {
         logger.error("Failed to start harvester-ys-{}: {}", this.indexedId, var4.getMessage());
         var1.tryFail(var4);
      }

   }

   public BrowserSharedHarvester(int var1) {
      this.timerId = -1L;
      this.currentToken = null;
      this.ready = false;
      this.reloads = 0;
      this.currentSiteURL = null;
      this.indexedId = var1;
      this.harvesterId = UUID.randomUUID().toString();
      this.deferredReplies = new ArrayList();
      this.executor = Executors.newSingleThreadExecutor();
      this.requests = new LinkedHashMap();
      this.referenceMap = new HashMap();
      this.passCounter = new LongAdder();
   }

   public void lambda$setInterceptors$9(ConsoleMessageReceived var1) {
      ConsoleMessage var2 = var1.consoleMessage();
      String var3 = var2.message();
      if (var3.indexOf("03") == 0) {
         try {
            SharedCaptchaToken var4 = new SharedCaptchaToken(this.currentSiteURL);
            var4.setSolved(var3, this.passCounter);
            if (this.solvePromise != null) {
               this.solvePromise.complete(var4);
            }

            logger.info("Received token [V3]: {}", var3);
         } catch (Throwable var5) {
            var5.printStackTrace();
         }
      }

   }

   public void setProxy() {
      try {
         this.proxy = ((ProxyController)io.trickle.core.Engine.get().getModule(Controller.PROXY_CAPTCHA)).getProxyCyclic();
         if (this.proxy != null && !this.proxy.isLocal()) {
            this.setProxy(this.proxy.toParams());
            this.browser.setTitle(this.indexedId, this.proxy.string());
         } else {
            logger.info("Running harvester[{}] locally", this.indexedId);
            this.useLocal();
         }
      } catch (Throwable var2) {
         this.useLocal();
      }

   }

   public void checkAndSolve() {
      if (this.solvePromise == null || this.solvePromise.future().isComplete()) {
         Iterator var1 = this.requests.keySet().iterator();
         if (var1.hasNext()) {
            this.currentSiteURL = (String)var1.next();
            this.solvePromise = Promise.promise();
            super.vertx.executeBlocking(this::solve).onSuccess(BrowserSharedHarvester::lambda$checkAndSolve$6);
            this.solvePromise.future().onSuccess(this::handleSolved);
         }
      }

   }

   public void loadSitekey() {
      this.sitekey = io.trickle.core.Engine.get().getClientConfiguration().getString("sitekeyV3", "6Lf34M8ZAAAAANgE72rhfideXH21Lab333mdd2d-");
      this.action = io.trickle.core.Engine.get().getClientConfiguration().getString("actionV3", "yzysply_wr_pageview");
      logger.info("Loaded captcha config: sitekey:'{}'; action:'{}'", this.sitekey, this.action);
   }

   public void solve(Promise var1) {
      try {
         if (this.reloads != 0 && this.reloads % 200 == 0) {
            this.setProxy();
            Thread.sleep(3000L);
         }

         ++this.reloads;
         Optional var2 = this.browser.browser().mainFrame();
         var2.ifPresent(this::lambda$solve$3);
      } catch (Throwable var3) {
         logger.warn("Error occurred solving: {}", var3.getMessage());
         var1.tryFail(var3);
      }

   }

   public static void lambda$checkAndSolve$6(Void var0) {
      logger.info("Solving requested successfully!");
   }

   public boolean isSupportedV3Site(String var1) {
      if (var1.contains("https://www.google.com") && var1.endsWith(".js")) {
         return true;
      } else {
         return var1.contains("yeezysupply") || var1.contains("jdsports") || var1.contains("finishline") || var1.endsWith("/account/register");
      }
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

   public static void lambda$waitForLogin$7(Frame var0) {
      var0.executeJavaScript("location.href = \"https://www.google.com/\"");
   }

   public void start(Promise var1) {
      this.vertx.eventBus().localConsumer(this.harvesterId, this::captchaRequestHandler);
      this.loadSitekey();
      Future var10000 = this.vertx.executeBlocking(this::initialiseBrowser);
      Objects.requireNonNull(var1);
      var10000 = var10000.onFailure(var1::tryFail);
      Objects.requireNonNull(var1);
      var10000.onSuccess(var1::tryComplete);
   }

   public void closeBrowsers(Promise var1) {
      try {
         if (this.browserEngine != null && !this.browserEngine.isClosed()) {
            if (this.browser != null) {
               this.browser.close();
            }

            Iterator var2 = this.browserEngine.browsers().iterator();

            while(var2.hasNext()) {
               Browser var3 = (Browser)var2.next();
               if (var3 != null && !var3.isClosed()) {
                  var3.close();
               }
            }

            this.browserEngine.close();
         }
      } catch (Throwable var4) {
         logger.error("Error shutting down browser window: {}", var4.getMessage());
      }

      this.executor.shutdownNow();
      var1.tryComplete();
   }

   public String id() {
      return this.harvesterId;
   }

   public void startSolveLoop() {
      super.vertx.setPeriodic(150L, this::lambda$startSolveLoop$5);
   }

   public void lambda$solve$3(Frame var1) {
      var1.executeJavaScript("location.href = \"" + this.currentSiteURL + "\"");
   }

   public int passCount() {
      return this.passCounter.intValue();
   }

   public static String captchaPageV3() {
      return "<html>\n<body style=\"background-color:#002240;\">\n<header>\n    <h1 style=\"color:#FFFFFF;\">Trickle V3 ~ %d</span> </h1>\n</header>\n<main>\n    <script src=\"https://www.google.com/recaptcha/enterprise.js?render=%s\"></script>\n    <script>\n        grecaptcha.enterprise.ready(function() {\n            grecaptcha.enterprise.execute('%s', {action: '%s'}).then(function(token) {\n                console.log(token);\n            });\n        });\n    </script>\n</main>\n</body>\n</html>";
   }

   public void lambda$initialiseBrowser$2(EngineCrashed var1) {
      int var2 = var1.exitCode();
      logger.warn("Harvester crashed with code: {}. Restarting...", var2);
      this.ready = false;
      CompletableFuture.runAsync(this::lambda$initialiseBrowser$1);
   }
}

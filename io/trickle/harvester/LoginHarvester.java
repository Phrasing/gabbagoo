package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.cookie.Cookie;
import com.teamdev.jxbrowser.cookie.CookieStore;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.UrlRequestJob.Options;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback.Response;
import com.teamdev.jxbrowser.net.event.RequestCompleted;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import io.trickle.core.Controller;
import io.trickle.core.VertxSingleton;
import io.trickle.util.Storage;
import io.trickle.webclient.CookieJar;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.shareddata.Lock;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginHarvester {
   public static LoginHarvester[] LOGIN_HARVESTERS;
   public AtomicReference latch = new AtomicReference();
   public AtomicBoolean started = new AtomicBoolean(false);
   public ExecutorService executor = Executors.newSingleThreadExecutor();
   public WindowedBrowser browser;
   public Engine engine;
   public static AtomicInteger harvesterCount = new AtomicInteger(0);
   public static String[] LOGIN_EPS = new String[]{"https://www.amazon.com/ap/signin?openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.com%2F%3Fref_%3Dnav_signin&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&", "https://www.bestbuy.com/identity/signin?token="};
   public LoginFuture loginFuture;
   public ConcurrentHashSet urlsIncompleted;
   public static CountDownLatch isInstantiated = new CountDownLatch(1);
   public AtomicInteger referenceCount = new AtomicInteger(0);
   public static Logger logger = LogManager.getLogger(LoginHarvester.class);

   public void setInterceptor() {
      this.engine.network().set(BeforeUrlRequestCallback.class, this::lambda$setInterceptor$3);
      this.engine.network().on(RequestCompleted.class, this::lambda$setInterceptor$5);
   }

   public static void lambda$startSolver$1(Frame var0) {
      var0.executeJavaScript("document.querySelector(\"html\").innerHTML = `<h2>Waiting for token</h2>`");
   }

   public void init() {
      int var1 = harvesterCount.incrementAndGet();

      while(this.engine == null) {
         try {
            this.engine = Engine.newInstance(this.baseOpts().build());
            isInstantiated.countDown();
         } catch (Throwable var7) {
            logger.warn("Updating harvester. Please wait: {}", var7.getMessage());

            try {
               if (this.engine != null) {
                  this.engine.close();
               }

               if (var1 != 1) {
                  isInstantiated.await();
               }
            } catch (Throwable var6) {
               logger.warn("Error loading cached data backup: {}", var7.getMessage());

               try {
                  Thread.sleep(5000L);
               } catch (InterruptedException var5) {
                  var5.printStackTrace();
               }
            }
         }
      }

      this.browser = new WindowedBrowser(this.engine);
      this.browser.createWindow();
      this.setInterceptor();
   }

   public void startSolver() {
      this.executor.submit(this::lambda$startSolver$2);
   }

   public void lambda$startSolver$2() {
      while(!Thread.currentThread().isInterrupted()) {
         try {
            this.urlsIncompleted = new ConcurrentHashSet();
            this.latch.set(new CountDownLatch(1));
            this.loginFuture = ((LoginController)io.trickle.core.Engine.get().getModule(Controller.LOGIN)).pollWaitingList();
            logger.debug("[startSolver()] Got token {}", this.loginFuture);
            this.configureLoginDetails();
            this.browser.browser().navigation().loadUrlAndWait("https://www.bestbuy.com/favicon.ico");
            LoginToken var1 = this.loginFuture.getEmptyLoginToken();
            this.browser.browser().navigation().loadUrl(var1.getDomain());
            this.browser.enlarge();
            ((CountDownLatch)this.latch.get()).await();
            this.exportCookiesFromHarvester();
            this.browser.browser().mainFrame().ifPresent(LoginHarvester::lambda$startSolver$1);
            this.loginFuture.complete(this.loginFuture.getEmptyLoginToken());
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

   }

   public static void lambda$setProxy$6(String[] var0, AuthenticateCallback.Params var1, AuthenticateCallback.Action var2) {
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

   public void lambda$setInterceptor$4(String var1, Long var2) {
      this.urlsIncompleted.remove(var1);
      if (this.urlsIncompleted.isEmpty()) {
         ((CountDownLatch)this.latch.get()).countDown();
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

   public void setProxy(String[] var1) {
      this.engine.network().set(AuthenticateCallback.class, LoginHarvester::lambda$setProxy$6);
      this.engine.proxy().config(CustomProxyConfig.newInstance(String.format("http=%s:%s;https=%s:%s", var1[0], var1[1], var1[0], var1[1])));
   }

   public boolean checkIfBrowserHasCookie(String var1) {
      CookieStore var2 = this.engine.cookieStore();
      Iterator var3 = var2.cookies().iterator();

      Cookie var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (Cookie)var3.next();
      } while(var4.name().isEmpty() || !var4.name().equals(var1));

      return true;
   }

   public EngineOptions.Builder baseOpts() {
      return EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, this::lambda$baseOpts$0);
   }

   public BeforeUrlRequestCallback.Response lambda$setInterceptor$3(BeforeUrlRequestCallback.Params var1) {
      String var2 = var1.urlRequest().url();
      if (var2.contains("addchv=true")) {
         ((CountDownLatch)this.latch.get()).countDown();
         return Response.cancel();
      } else {
         if (var2.contains("tmx.bestbuy.com")) {
            this.urlsIncompleted.add(var2);
         }

         return Response.proceed();
      }
   }

   public void lambda$setInterceptor$5(RequestCompleted var1) {
      String var2 = var1.urlRequest().url();
      if (var1.urlRequest().url().contains("https://www.amazon.com/?ref_=nav_signin&")) {
         logger.info("Login complete!");
         ((CountDownLatch)this.latch.get()).countDown();
      } else if (var2.contains("tmx.bestbuy.com")) {
         VertxSingleton.INSTANCE.get().setTimer(5000L, this::lambda$setInterceptor$4);
      }

   }

   public static CompletableFuture async$start(LoginHarvester param0, CompletableFuture param1, int param2, Object param3) {
      // $FF: Couldn't be decompiled
   }

   public boolean isLoginPage(String var1) {
      String[] var2 = LOGIN_EPS;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var1.startsWith(var5)) {
            return true;
         }
      }

      return false;
   }

   public void exportCookiesFromHarvester() {
      CookieJar var1 = this.loginFuture.getEmptyLoginToken().getCookieJar();
      CookieStore var2 = this.engine.cookieStore();
      Iterator var3 = var2.cookies().iterator();

      while(var3.hasNext()) {
         Cookie var4 = (Cookie)var3.next();
         if (!var4.name().isEmpty() && !var4.name().equals("vt")) {
            var1.put(var4.name(), var4.value(), var4.domain());
         }
      }

   }

   public InterceptUrlRequestCallback.Response lambda$baseOpts$0(InterceptUrlRequestCallback.Params var1) {
      String var2 = var1.urlRequest().url();
      boolean var3 = var2.contains(".ico");
      if (!var3 && this.isLoginPage(var2) && this.loginFuture.getEmptyLoginToken().getHtml() != null) {
         UrlRequestJob var4 = var1.newUrlRequestJob(Options.newBuilder(HttpStatus.OK).build());
         var4.write(this.loginFuture.getEmptyLoginToken().getHtml().getBytes(StandardCharsets.UTF_8));
         var4.complete();
         return com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback.Response.intercept(var4);
      } else {
         return com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback.Response.proceed();
      }
   }

   public CompletableFuture start() {
      this.referenceCount.incrementAndGet();

      try {
         CompletableFuture var10000 = Vertx.currentContext().owner().sharedData().getLocalLockWithTimeout(String.valueOf(this.executor.hashCode()), TimeUnit.HOURS.toMillis(1L)).toCompletionStage().toCompletableFuture();
         if (!var10000.isDone()) {
            CompletableFuture var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(LoginHarvester::async$start);
         }

         Lock var1 = (Lock)var10000.join();

         try {
            if (!this.started.get()) {
               this.executor.submit(this::init);
               this.executor.submit(this::startSolver);
               this.started.set(true);
               logger.debug("Started!");
            }
         } catch (Throwable var8) {
            logger.error("Start error on harvester {}", var8.getMessage());
         } finally {
            var1.release();
         }
      } catch (Throwable var10) {
         logger.error("Lock error on harvester {}", var10.getMessage());
      }

      return CompletableFuture.completedFuture(true);
   }

   public void configureLoginDetails() {
      LoginToken var1 = this.loginFuture.getEmptyLoginToken();
      CookieStore var2 = this.engine.cookieStore();
      var2.deleteAll();
      var2.persist();
      Iterator var3 = var1.getCookies().iterator();

      while(var3.hasNext()) {
         io.netty.handler.codec.http.cookie.Cookie var4 = (io.netty.handler.codec.http.cookie.Cookie)var3.next();
         var2.set(Cookie.newBuilder(var4.domain()).name(var4.name()).value(var4.value()).path(var4.path()).httpOnly(var4.isHttpOnly()).secure(var4.isSecure()).build());
      }

      var2.persist();
      this.setProxy(var1.getProxyStr().split(":"));
   }

   static {
      LOGIN_HARVESTERS = new LoginHarvester[Storage.HARVESTER_COUNT_YS];

      for(int var0 = 0; var0 < LOGIN_HARVESTERS.length; ++var0) {
         LOGIN_HARVESTERS[var0] = new LoginHarvester();
      }

   }
}

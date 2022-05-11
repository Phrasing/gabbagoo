package io.trickle.harvester.pooled;

import com.fuzzy.aycd.autosolve.AbstractAutoSolveManager;
import com.fuzzy.aycd.autosolve.exception.AutoSolveException;
import com.fuzzy.aycd.autosolve.model.AutoSolveProxyConfig;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaToken;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaTokenRequest;
import io.trickle.core.Engine;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.LongAdder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutosolveHarvester implements SharedHarvester, Handler {
   public LongAdder passes;
   public Vertx vertx;
   public AbstractAutoSolveManager solver;
   public HashMap referenceMap;
   public String action;
   public String id;
   public LinkedHashMap requests;
   public String sitekey;
   public boolean started;
   public static Logger logger = LogManager.getLogger(AutosolveHarvester.class);
   public Promise solvePromise;
   public String currentSiteURL;

   public void loadSitekey() {
      this.sitekey = Engine.get().getClientConfiguration().getString("sitekeyV3", "6Lf34M8ZAAAAANgE72rhfideXH21Lab333mdd2d-");
      this.action = Engine.get().getClientConfiguration().getString("actionV3", "yzysply_wr_pageview");
   }

   public void handle(Object var1) {
      this.handle((Message)var1);
   }

   public AutosolveHarvester(Vertx var1, AbstractAutoSolveManager var2, LongAdder var3) {
      this.currentSiteURL = null;
      this.started = false;
      this.vertx = var1;
      this.id = UUID.randomUUID().toString();
      this.solver = var2;
      this.passes = var3;
      this.requests = new LinkedHashMap();
      this.referenceMap = new HashMap();
      this.loadSitekey();
   }

   public void startSolveLoop() {
      if (!this.started) {
         this.vertx.setPeriodic(150L, this::lambda$startSolveLoop$3);
         this.started = true;
      }

   }

   public void checkSolve() {
      if (this.solvePromise == null || this.solvePromise.future().isComplete()) {
         Iterator var1 = this.requests.keySet().iterator();
         if (var1.hasNext()) {
            this.currentSiteURL = (String)var1.next();
            this.solvePromise = Promise.promise();
            int var2 = 0;

            while(var2 < 5) {
               CaptchaTokenRequest var3 = CaptchaTokenRequest.ofReCaptchaV3(UUID.randomUUID().toString(), this.currentSiteURL, this.sitekey, this.action, Double.longBitsToDouble(4604480259023595110L), AutoSolveProxyConfig.none(), false);

               try {
                  this.solver.requestAndConsumeCaptchaToken(var3, this::lambda$checkSolve$1);
                  break;
               } catch (AutoSolveException var5) {
                  logger.warn("Retrying after error occurred on autoSolve: {}", var5.getMessage());
                  ++var2;
               }
            }

            this.solvePromise.future().onSuccess(this::handleSolved);
         }
      }

   }

   public String id() {
      return this.id;
   }

   public int passCount() {
      return this.passes == null ? 0 : this.passes.intValue();
   }

   public void lambda$startSolveLoop$3(Long var1) {
      this.checkSolve();
   }

   public void lambda$checkSolve$1(CaptchaToken var1) {
      if (var1.isValid()) {
         SharedCaptchaToken var2 = new SharedCaptchaToken(this.currentSiteURL);
         logger.info("Received AutoSolve Captcha: {}", var1);
         var2.setSolved(var1.getToken(), (LongAdder)null);
         this.vertx.runOnContext(this::lambda$checkSolve$0);
      } else {
         this.solvePromise.tryFail("Invalid Captcha Solve");
      }

   }

   public void handle(Message var1) {
      this.startSolveLoop();
      String var2 = (String)var1.body();
      if (var2 != null && !var2.isEmpty()) {
         SharedCaptchaToken var3 = (SharedCaptchaToken)this.referenceMap.get(var2);
         if (var3 != null && !var3.isExpired()) {
            var1.reply(var3);
         } else {
            this.requests.putIfAbsent(var2, new ArrayList());
            this.requests.computeIfPresent(var2, AutosolveHarvester::lambda$handle$2);
         }
      }

   }

   public static List lambda$handle$2(Message var0, String var1, List var2) {
      var2.add(var0.replyAddress());
      return var2;
   }

   public void lambda$checkSolve$0(SharedCaptchaToken var1, Void var2) {
      this.solvePromise.tryComplete(var1);
   }

   public AutosolveHarvester(Vertx var1, AbstractAutoSolveManager var2) {
      this(var1, var2, (LongAdder)null);
   }

   public void handleSolved(SharedCaptchaToken var1) {
      try {
         Iterator var2 = ((List)this.requests.get(var1.getDomain())).iterator();
         this.requests.remove(var1.getDomain());
         this.referenceMap.put(var1.getDomain(), var1);

         for(; var2.hasNext(); var2.remove()) {
            String var3 = (String)var2.next();
            if (var3 != null && !var3.isEmpty()) {
               this.vertx.eventBus().send(var3, var1);
            }
         }
      } catch (Throwable var4) {
         logger.error("Error occurred handing solves: {}", var4.getMessage());
      }

   }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fuzzy.aycd.autosolve.AbstractAutoSolveManager
 *  com.fuzzy.aycd.autosolve.exception.AutoSolveException
 *  com.fuzzy.aycd.autosolve.model.AutoSolveProxyConfig
 *  com.fuzzy.aycd.autosolve.model.task.impl.CaptchaToken
 *  com.fuzzy.aycd.autosolve.model.task.impl.CaptchaTokenRequest
 *  io.trickle.core.Engine
 *  io.trickle.harvester.pooled.SharedCaptchaToken
 *  io.trickle.harvester.pooled.SharedHarvester
 *  io.vertx.core.Handler
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  io.vertx.core.eventbus.Message
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester.pooled;

import com.fuzzy.aycd.autosolve.AbstractAutoSolveManager;
import com.fuzzy.aycd.autosolve.exception.AutoSolveException;
import com.fuzzy.aycd.autosolve.model.AutoSolveProxyConfig;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaToken;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaTokenRequest;
import io.trickle.core.Engine;
import io.trickle.harvester.pooled.SharedCaptchaToken;
import io.trickle.harvester.pooled.SharedHarvester;
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

public class AutosolveHarvester
implements SharedHarvester,
Handler {
    public LongAdder passes;
    public Vertx vertx;
    public AbstractAutoSolveManager solver;
    public HashMap<String, SharedCaptchaToken> referenceMap;
    public String action;
    public String id;
    public LinkedHashMap<String, List<String>> requests;
    public String sitekey;
    public boolean started = false;
    public static Logger logger = LogManager.getLogger(AutosolveHarvester.class);
    public Promise<SharedCaptchaToken> solvePromise;
    public String currentSiteURL = null;

    public void loadSitekey() {
        this.sitekey = Engine.get().getClientConfiguration().getString("sitekeyV3", "6Lf34M8ZAAAAANgE72rhfideXH21Lab333mdd2d-");
        this.action = Engine.get().getClientConfiguration().getString("actionV3", "yzysply_wr_pageview");
    }

    public void handle(Object object) {
        this.handle((Message)object);
    }

    public AutosolveHarvester(Vertx vertx, AbstractAutoSolveManager abstractAutoSolveManager, LongAdder longAdder) {
        this.vertx = vertx;
        this.id = UUID.randomUUID().toString();
        this.solver = abstractAutoSolveManager;
        this.passes = longAdder;
        this.requests = new LinkedHashMap();
        this.referenceMap = new HashMap();
        this.loadSitekey();
    }

    public void startSolveLoop() {
        if (this.started) return;
        this.vertx.setPeriodic(150L, this::lambda$startSolveLoop$3);
        this.started = true;
    }

    public void checkSolve() {
        Iterator<String> iterator;
        if (this.solvePromise != null) {
            if (!this.solvePromise.future().isComplete()) return;
        }
        if (!(iterator = this.requests.keySet().iterator()).hasNext()) return;
        this.currentSiteURL = iterator.next();
        this.solvePromise = Promise.promise();
        for (int i = 0; i < 5; ++i) {
            CaptchaTokenRequest captchaTokenRequest = CaptchaTokenRequest.ofReCaptchaV3((String)UUID.randomUUID().toString(), (String)this.currentSiteURL, (String)this.sitekey, (String)this.action, (double)Double.longBitsToDouble(4604480259023595110L), (AutoSolveProxyConfig)AutoSolveProxyConfig.none(), (boolean)false);
            try {
                this.solver.requestAndConsumeCaptchaToken(captchaTokenRequest, this::lambda$checkSolve$1);
                break;
            }
            catch (AutoSolveException autoSolveException) {
                logger.warn("Retrying after error occurred on autoSolve: {}", (Object)autoSolveException.getMessage());
                continue;
            }
        }
        this.solvePromise.future().onSuccess(this::handleSolved);
    }

    public String id() {
        return this.id;
    }

    public int passCount() {
        if (this.passes != null) return this.passes.intValue();
        return 0;
    }

    public void lambda$startSolveLoop$3(Long l) {
        this.checkSolve();
    }

    public void lambda$checkSolve$1(CaptchaToken captchaToken) {
        if (captchaToken.isValid()) {
            SharedCaptchaToken sharedCaptchaToken = new SharedCaptchaToken(this.currentSiteURL);
            logger.info("Received AutoSolve Captcha: {}", (Object)captchaToken);
            sharedCaptchaToken.setSolved(captchaToken.getToken(), null);
            this.vertx.runOnContext(arg_0 -> this.lambda$checkSolve$0(sharedCaptchaToken, arg_0));
        } else {
            this.solvePromise.tryFail("Invalid Captcha Solve");
        }
    }

    public void handle(Message message) {
        this.startSolveLoop();
        String string = (String)message.body();
        if (string == null) return;
        if (string.isEmpty()) return;
        SharedCaptchaToken sharedCaptchaToken = this.referenceMap.get(string);
        if (sharedCaptchaToken == null || sharedCaptchaToken.isExpired()) {
            this.requests.putIfAbsent(string, new ArrayList());
            this.requests.computeIfPresent(string, (arg_0, arg_1) -> AutosolveHarvester.lambda$handle$2(message, arg_0, arg_1));
        } else {
            message.reply((Object)sharedCaptchaToken);
        }
    }

    public static List lambda$handle$2(Message message, String string, List list) {
        list.add(message.replyAddress());
        return list;
    }

    public void lambda$checkSolve$0(SharedCaptchaToken sharedCaptchaToken, Void void_) {
        this.solvePromise.tryComplete((Object)sharedCaptchaToken);
    }

    public AutosolveHarvester(Vertx vertx, AbstractAutoSolveManager abstractAutoSolveManager) {
        this(vertx, abstractAutoSolveManager, null);
    }

    public void handleSolved(SharedCaptchaToken sharedCaptchaToken) {
        try {
            Iterator<String> iterator = this.requests.get(sharedCaptchaToken.getDomain()).iterator();
            this.requests.remove(sharedCaptchaToken.getDomain());
            this.referenceMap.put(sharedCaptchaToken.getDomain(), sharedCaptchaToken);
            while (iterator.hasNext()) {
                String string = iterator.next();
                if (string != null && !string.isEmpty()) {
                    this.vertx.eventBus().send(string, (Object)sharedCaptchaToken);
                }
                iterator.remove();
            }
            return;
        }
        catch (Throwable throwable) {
            logger.error("Error occurred handing solves: {}", (Object)throwable.getMessage());
        }
    }
}

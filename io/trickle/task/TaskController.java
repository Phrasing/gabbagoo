/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.core.api.LoadableAsync
 *  io.trickle.core.api.Module
 *  io.trickle.task.ColumnIndex
 *  io.trickle.task.Task
 *  io.trickle.task.TaskController$1
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.bestbuy.Bestbuy
 *  io.trickle.task.sites.hibbett.Hibbett
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.task.sites.shopify.ShopifyBackend
 *  io.trickle.task.sites.shopify.ShopifyHybrid
 *  io.trickle.task.sites.shopify.ShopifySafe
 *  io.trickle.task.sites.shopify.util.SiteParser
 *  io.trickle.task.sites.walmart.canada.WalmartCA
 *  io.trickle.task.sites.walmart.graphql.WalmartNew
 *  io.trickle.task.sites.yeezy.Yeezy
 *  io.trickle.util.FileUtils
 *  io.trickle.util.Storage
 *  io.trickle.util.concurrent.VertxUtil
 *  io.vertx.core.Future
 *  io.vertx.core.Verticle
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.FileSystem
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task;

import io.trickle.core.actor.TaskActor;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.task.ColumnIndex;
import io.trickle.task.Task;
import io.trickle.task.TaskController;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.bestbuy.Bestbuy;
import io.trickle.task.sites.hibbett.Hibbett;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.ShopifyBackend;
import io.trickle.task.sites.shopify.ShopifyHybrid;
import io.trickle.task.sites.shopify.ShopifySafe;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.walmart.canada.WalmartCA;
import io.trickle.task.sites.walmart.graphql.WalmartNew;
import io.trickle.task.sites.yeezy.Yeezy;
import io.trickle.util.FileUtils;
import io.trickle.util.Storage;
import io.trickle.util.concurrent.VertxUtil;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TaskController
implements Module,
LoadableAsync {
    private static final Logger logger = LogManager.getLogger(TaskController.class);
    public static int TASK_COUNT = 0;
    private final List<TaskActor<?>> actors;
    private final List<Task> tasks;
    private final Vertx vertx;
    private final AtomicInteger count = new AtomicInteger(1);
    private boolean cleaner = false;
    private int profileCount = 0;

    public TaskController(Vertx vertx) {
        this.vertx = vertx;
        this.tasks = new ArrayList<Task>();
        this.actors = new ArrayList();
    }

    public void initialise() {
        logger.debug("Initialized.");
    }

    public synchronized void startTasks() {
        Task.profileRotator.finish();
        Iterator<Task> iterator = this.tasks.iterator();
        block15: while (iterator.hasNext()) {
            Task task = iterator.next();
            try {
                WalmartNew actor;
                block1 : switch (1.$SwitchMap$io$trickle$task$sites$Site[task.getSite().ordinal()]) {
                    case 1: {
                        actor = new WalmartNew(task, this.count.getAndIncrement());
                        break;
                    }
                    case 2: {
                        actor = new WalmartCA(task, this.count.getAndIncrement());
                        break;
                    }
                    case 3: {
                        actor = new Yeezy(task, this.count.getAndIncrement());
                        break;
                    }
                    case 4: {
                        actor = new Bestbuy(task, this.count.getAndIncrement());
                        break;
                    }
                    case 5: {
                        actor = new Hibbett(task, this.count.getAndIncrement());
                        break;
                    }
                    default: {
                        switch (1.$SwitchMap$io$trickle$task$sites$shopify$Mode[SiteParser.getMode((String)task.getMode()).ordinal()]) {
                            case 1: {
                                actor = new ShopifySafe(task, this.count.getAndIncrement());
                                break block1;
                            }
                            case 2: {
                                actor = new ShopifyHybrid(task, this.count.getAndIncrement());
                                break block1;
                            }
                            case 3: {
                                actor = new Shopify(task, this.count.getAndIncrement());
                                break block1;
                            }
                            case 4: {
                                actor = new ShopifyBackend(task, this.count.getAndIncrement());
                                break block1;
                            }
                        }
                        continue block15;
                    }
                }
                this.actors.add((TaskActor<?>)actor);
                this.vertx.deployVerticle((Verticle)actor);
                if (this.cleaner) continue;
                this.cleaner = true;
                this.afterStartCleanup();
            }
            catch (Throwable throwable) {}
        }
    }

    private void afterStartCleanup() {
        this.vertx.setTimer(TimeUnit.MINUTES.toMillis(5L), l -> {
            this.cleanUp();
            this.periodicCleanup();
        });
    }

    private void periodicCleanup() {
        this.vertx.setPeriodic(TimeUnit.MINUTES.toMillis(30L), l -> this.cleanUp());
    }

    public void stopTasks() {
        Iterator<TaskActor<?>> iterator = this.actors.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.cleanUp();
                return;
            }
            TaskActor<?> actor = iterator.next();
            try {
                this.vertx.undeploy(actor.deploymentID()).result();
            }
            catch (Exception exception) {
            }
        }
    }

    private void cleanUp() {
        System.gc();
    }

    public void terminate() {
        Iterator<TaskActor<?>> iterator = this.actors.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.actors.clear();
                this.tasks.clear();
                return;
            }
            TaskActor<?> actor = iterator.next();
            try {
                this.vertx.undeploy(actor.deploymentID()).result();
            }
            catch (Exception exception) {
            }
        }
    }

    public int profileCount() {
        return this.profileCount;
    }

    public int countTasks() {
        return this.tasks.size();
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public Future<Void> load() {
        FileSystem fs = this.vertx.fileSystem();
        return fs.readFile(Storage.CONFIG_PATH + "/profiles.csv").otherwise(ex -> {
            logger.warn("Failed to find profiles.csv. Creating template...");
            FileUtils.createAndWrite((FileSystem)fs, (String)(Storage.CONFIG_PATH + "/profiles.csv"), (Buffer)Buffer.buffer((String)"Keyword,SIZE/QUANTITY,FIRST NAME,LAST NAME,EMAIL,PHONE NUMBER,ADDRESS 1,ADDRESS 2,STATE,CITY,ZIP,COUNTRY,CC NUMBER,MONTH,YEAR,CVC,Task Quantity,Retry Delay,Monitor Delay,Site,Mode,2CaptchaKEY\n"));
            return Buffer.buffer((String)"");
        }).map(f -> f.toString(StandardCharsets.UTF_8)).map(this::parseFile).map(c -> {
            this.profileCount = c.size();
            Iterator iterator = c.iterator();
            block0: while (true) {
                if (!iterator.hasNext()) {
                    TASK_COUNT = this.tasks.size();
                    return true;
                }
                Task task = (Task)iterator.next();
                int i = 0;
                while (true) {
                    if (i >= task.getTaskQuantity()) continue block0;
                    this.tasks.add(task.copy());
                    ++i;
                }
                break;
            }
        }).compose(r -> Future.succeededFuture());
    }

    private List<Task> parseFile(String rows) {
        List rawTasks = Arrays.stream(rows.split("\n")).filter(Objects::nonNull).filter(s -> !s.isEmpty()).filter(l -> !l.toLowerCase().contains("keyword")).map(r -> r.split(",")).collect(Collectors.toList());
        ArrayList<Task> tasks = new ArrayList<Task>();
        Iterator iterator = rawTasks.iterator();
        while (iterator.hasNext()) {
            String[] rawTask = (String[])iterator.next();
            try {
                tasks.add(this.validateAndCreate(rawTask));
            }
            catch (Throwable e) {
                System.out.println("Entry error for row '" + rawTasks.indexOf(rawTask) + "': " + e.getMessage());
            }
        }
        return tasks;
    }

    private Task validateAndCreate(String[] row) throws Exception {
        if (row.length < 22) {
            int c = ColumnIndex.values().length - row.length;
            StringBuilder r = new StringBuilder();
            int i2 = ColumnIndex.values().length - c;
            while (i2 < ColumnIndex.values().length - 1) {
                if (r.length() != 0) {
                    r.append(", ");
                }
                r.append(ColumnIndex.values()[i2]);
                ++i2;
            }
            throw new Exception("Missing collum entries: " + r.toString().trim());
        }
        int i = 0;
        while (i < row.length) {
            String t = row[i];
            Objects.requireNonNull(t, "Field '" + ColumnIndex.values()[i] + "' is empty or missing");
            if (i >= 16 && i <= 18) {
                try {
                    int i2 = Integer.parseInt(t);
                }
                catch (NumberFormatException e) {
                    throw new Exception("Field '" + ColumnIndex.values()[i] + "' is expected to be a number but is: " + t);
                }
            }
            ++i;
        }
        return new Task(row);
    }

    public void massPassword(String rawInput) {
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setPassword(rawInput);
        }
    }

    public void massEditLinkOrKeyword(String rawInput) {
        boolean isYs = false;
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (isYs) return;
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setKeywords(rawInput.toUpperCase(Locale.ROOT).replace("  ", " ").split(" "));
            if (task.getSite() != Site.YEEZY) continue;
            isYs = true;
        }
    }

    public void massEditDelay(long newDelay) {
        if (newDelay > Integer.MAX_VALUE) {
            newDelay = Integer.MAX_VALUE;
        }
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setMonitorDelay(Integer.valueOf((int)newDelay));
        }
    }

    public void switchToDefaultDelay() {
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setMonitorDelay(null);
        }
    }
}

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
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
import io.trickle.task.TaskController$1;
import io.trickle.task.sites.bestbuy.Bestbuy;
import io.trickle.task.sites.hibbett.Hibbett;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.ShopifyBackend;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskController
implements Module,
LoadableAsync {
    public int profileCount = 0;
    public List<Task> tasks;
    public boolean cleaner = false;
    public AtomicInteger count = new AtomicInteger(1);
    public List<TaskActor<?>> actors;
    public Vertx vertx;
    public static Logger logger = LogManager.getLogger(TaskController.class);

    @Override
    public void initialise() {
        logger.debug("Initialized.");
    }

    public void startTasks() {
        Iterator<Task> iterator = this.tasks.iterator();
        block14: while (iterator.hasNext()) {
            Task task = iterator.next();
            try {
                TaskActor taskActor;
                block1 : switch (task.getSite()) {
                    case WALMART_NEW: {
                        taskActor = new WalmartNew(task, this.count.getAndIncrement());
                        break;
                    }
                    case WALMART_CA: {
                        taskActor = new WalmartCA(task, this.count.getAndIncrement());
                        break;
                    }
                    case YEEZY: {
                        taskActor = new Yeezy(task, this.count.getAndIncrement());
                        break;
                    }
                    case BESTBUY: {
                        taskActor = new Bestbuy(task, this.count.getAndIncrement());
                        break;
                    }
                    case HIBBETT: {
                        taskActor = new Hibbett(task, this.count.getAndIncrement());
                        break;
                    }
                    default: {
                        switch (TaskController$1.$SwitchMap$io$trickle$task$sites$shopify$Mode[SiteParser.getMode(task.getMode()).ordinal()]) {
                            case 1: {
                                taskActor = new ShopifySafe(task, this.count.getAndIncrement());
                                break block1;
                            }
                            case 2: {
                                taskActor = new Shopify(task, this.count.getAndIncrement());
                                break block1;
                            }
                            case 3: {
                                taskActor = new ShopifyBackend(task, this.count.getAndIncrement());
                                break block1;
                            }
                        }
                        continue block14;
                    }
                }
                this.actors.add(taskActor);
                this.vertx.deployVerticle((Verticle)taskActor);
                if (this.cleaner) continue;
                this.cleaner = true;
                this.afterStartCleanup();
            }
            catch (Throwable throwable) {}
        }
    }

    public int countTasks() {
        return this.tasks.size();
    }

    public List parseFile(String string) {
        List list = Arrays.stream(string.split("\n")).filter(Objects::nonNull).filter(TaskController::lambda$parseFile$6).filter(TaskController::lambda$parseFile$7).map(TaskController::lambda$parseFile$8).collect(Collectors.toList());
        ArrayList<Task> arrayList = new ArrayList<Task>();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String[] stringArray = (String[])iterator.next();
            try {
                arrayList.add(this.validateAndCreate(stringArray));
            }
            catch (Throwable throwable) {
                System.out.println("Entry error for row '" + list.indexOf(stringArray) + "': " + throwable.getMessage());
            }
        }
        return arrayList;
    }

    public static Buffer lambda$load$2(FileSystem fileSystem, Throwable throwable) {
        logger.warn("Failed to find profiles.csv. Creating template...");
        FileUtils.createAndWrite(fileSystem, Storage.CONFIG_PATH + "/profiles.csv", Buffer.buffer((String)"Keyword,SIZE/QUANTITY,FIRST NAME,LAST NAME,EMAIL,PHONE NUMBER,ADDRESS 1,ADDRESS 2,STATE,CITY,ZIP,COUNTRY,CC NUMBER,MONTH,YEAR,CVC,Task Quantity,Retry Delay,Monitor Delay,Site,Mode,2CaptchaKEY\n"));
        return Buffer.buffer((String)"");
    }

    public static Future lambda$load$5(Boolean bl) {
        return Future.succeededFuture();
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

    public void massEditLinkOrKeyword(String string) {
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setKeywords(string.toLowerCase().replace("  ", " ").split(" "));
        }
    }

    public void lambda$periodicCleanup$1(Long l) {
        this.cleanUp();
    }

    public int profileCount() {
        return this.profileCount;
    }

    public static String[] lambda$parseFile$8(String string) {
        return string.split(",");
    }

    public void massEditDelay(long l) {
        if (l > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setMonitorDelay((int)l);
        }
    }

    public static boolean lambda$parseFile$7(String string) {
        if (string.toLowerCase().contains("keyword")) return false;
        return true;
    }

    public void afterStartCleanup() {
        this.vertx.setTimer(TimeUnit.MINUTES.toMillis(5L), this::lambda$afterStartCleanup$0);
    }

    public List getTasks() {
        return this.tasks;
    }

    public void lambda$afterStartCleanup$0(Long l) {
        this.cleanUp();
        this.periodicCleanup();
    }

    public Boolean lambda$load$4(List list) {
        this.profileCount = list.size();
        Iterator iterator = list.iterator();
        block0: while (iterator.hasNext()) {
            Task task = (Task)iterator.next();
            int n = 0;
            while (true) {
                if (n >= task.getTaskQuantity()) continue block0;
                this.tasks.add(task.copy());
                ++n;
            }
            break;
        }
        return true;
    }

    public static boolean lambda$parseFile$6(String string) {
        if (string.isEmpty()) return false;
        return true;
    }

    public Task validateAndCreate(String[] stringArray) {
        if (stringArray.length < 22) {
            int n = ColumnIndex.values().length - stringArray.length;
            StringBuilder stringBuilder = new StringBuilder();
            int n2 = ColumnIndex.values().length - n;
            while (n2 < ColumnIndex.values().length - 1) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append((Object)ColumnIndex.values()[n2]);
                ++n2;
            }
            throw new Exception("Missing collum entries: " + stringBuilder.toString().trim());
        }
        int n = 0;
        while (n < stringArray.length) {
            String string = stringArray[n];
            Objects.requireNonNull(string, "Field '" + ColumnIndex.values()[n] + "' is empty or missing");
            if (n >= 16 && n <= 18) {
                try {
                    int n3 = Integer.parseInt(string);
                }
                catch (NumberFormatException numberFormatException) {
                    throw new Exception("Field '" + ColumnIndex.values()[n] + "' is expected to be a number but is: " + string);
                }
            }
            ++n;
        }
        return new Task(stringArray);
    }

    public void periodicCleanup() {
        this.vertx.setPeriodic(TimeUnit.MINUTES.toMillis(30L), this::lambda$periodicCleanup$1);
    }

    public void stopTasks() {
        Iterator<TaskActor<?>> iterator = this.actors.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.cleanUp();
                return;
            }
            TaskActor<?> taskActor = iterator.next();
            try {
                this.vertx.undeploy(taskActor.deploymentID()).result();
            }
            catch (Exception exception) {
            }
        }
    }

    public void cleanUp() {
        System.gc();
    }

    @Override
    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + "/profiles.csv").otherwise(arg_0 -> TaskController.lambda$load$2(fileSystem, arg_0)).map(TaskController::lambda$load$3).map(this::parseFile).map(this::lambda$load$4).compose(TaskController::lambda$load$5);
    }

    @Override
    public void terminate() {
        Iterator<TaskActor<?>> iterator = this.actors.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.actors.clear();
                this.tasks.clear();
                return;
            }
            TaskActor<?> taskActor = iterator.next();
            try {
                this.vertx.undeploy(taskActor.deploymentID()).result();
            }
            catch (Exception exception) {
            }
        }
    }

    public TaskController(Vertx vertx) {
        this.vertx = vertx;
        this.tasks = new ArrayList<Task>();
        this.actors = new ArrayList();
    }

    public void massPassword(String string) {
        Iterator<Task> iterator = this.tasks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                VertxUtil.sendSignal();
                return;
            }
            Task task = iterator.next();
            task.setPassword(string);
        }
    }

    public static String lambda$load$3(Buffer buffer) {
        return buffer.toString(StandardCharsets.UTF_8);
    }
}


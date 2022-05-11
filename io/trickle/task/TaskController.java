package io.trickle.task;

import io.trickle.core.actor.TaskActor;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
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
import java.io.PrintStream;
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

public final class TaskController implements Module, LoadableAsync {
   private static final Logger logger = LogManager.getLogger(TaskController.class);
   public static int TASK_COUNT = 0;
   private final List actors;
   private final List tasks;
   private final Vertx vertx;
   private final AtomicInteger count = new AtomicInteger(1);
   private boolean cleaner = false;
   private int profileCount = 0;

   public TaskController(Vertx vertx) {
      this.vertx = vertx;
      this.tasks = new ArrayList();
      this.actors = new ArrayList();
   }

   public void initialise() {
      logger.debug("Initialized.");
   }

   public synchronized void startTasks() {
      Task.profileRotator.finish();
      Iterator var1 = this.tasks.iterator();

      while(var1.hasNext()) {
         Task task = (Task)var1.next();

         try {
            Object actor;
            switch (task.getSite()) {
               case WALMART_NEW:
                  actor = new WalmartNew(task, this.count.getAndIncrement());
                  break;
               case WALMART_CA:
                  actor = new WalmartCA(task, this.count.getAndIncrement());
                  break;
               case YEEZY:
                  actor = new Yeezy(task, this.count.getAndIncrement());
                  break;
               case BESTBUY:
                  actor = new Bestbuy(task, this.count.getAndIncrement());
                  break;
               case HIBBETT:
                  actor = new Hibbett(task, this.count.getAndIncrement());
                  break;
               default:
                  switch (SiteParser.getMode(task.getMode())) {
                     case HUMAN:
                        actor = new ShopifySafe(task, this.count.getAndIncrement());
                        break;
                     case HYBRID:
                        actor = new ShopifyHybrid(task, this.count.getAndIncrement());
                        break;
                     case NORMAL:
                        actor = new Shopify(task, this.count.getAndIncrement());
                        break;
                     case FAST:
                        actor = new ShopifyBackend(task, this.count.getAndIncrement());
                        break;
                     default:
                        continue;
                  }
            }

            this.actors.add(actor);
            this.vertx.deployVerticle((Verticle)actor);
            if (!this.cleaner) {
               this.cleaner = true;
               this.afterStartCleanup();
            }
         } catch (Throwable var4) {
         }
      }

   }

   private void afterStartCleanup() {
      this.vertx.setTimer(TimeUnit.MINUTES.toMillis(5L), (l) -> {
         this.cleanUp();
         this.periodicCleanup();
      });
   }

   private void periodicCleanup() {
      this.vertx.setPeriodic(TimeUnit.MINUTES.toMillis(30L), (l) -> {
         this.cleanUp();
      });
   }

   public void stopTasks() {
      Iterator var1 = this.actors.iterator();

      while(var1.hasNext()) {
         TaskActor actor = (TaskActor)var1.next();

         try {
            this.vertx.undeploy(actor.deploymentID()).result();
         } catch (Exception var4) {
         }
      }

      this.cleanUp();
   }

   private void cleanUp() {
      System.gc();
   }

   public void terminate() {
      Iterator var1 = this.actors.iterator();

      while(var1.hasNext()) {
         TaskActor actor = (TaskActor)var1.next();

         try {
            this.vertx.undeploy(actor.deploymentID()).result();
         } catch (Exception var4) {
         }
      }

      this.actors.clear();
      this.tasks.clear();
   }

   public int profileCount() {
      return this.profileCount;
   }

   public int countTasks() {
      return this.tasks.size();
   }

   public List getTasks() {
      return this.tasks;
   }

   public Future load() {
      FileSystem fs = this.vertx.fileSystem();
      return fs.readFile(Storage.CONFIG_PATH + "/profiles.csv").otherwise((ex) -> {
         logger.warn("Failed to find profiles.csv. Creating template...");
         FileUtils.createAndWrite(fs, Storage.CONFIG_PATH + "/profiles.csv", Buffer.buffer("Keyword,SIZE/QUANTITY,FIRST NAME,LAST NAME,EMAIL,PHONE NUMBER,ADDRESS 1,ADDRESS 2,STATE,CITY,ZIP,COUNTRY,CC NUMBER,MONTH,YEAR,CVC,Task Quantity,Retry Delay,Monitor Delay,Site,Mode,2CaptchaKEY\n"));
         return Buffer.buffer("");
      }).map((f) -> {
         return f.toString(StandardCharsets.UTF_8);
      }).map(this::parseFile).map((c) -> {
         this.profileCount = c.size();
         Iterator var2 = c.iterator();

         while(var2.hasNext()) {
            Task task = (Task)var2.next();

            for(int i = 0; i < task.getTaskQuantity(); ++i) {
               this.tasks.add(task.copy());
            }
         }

         TASK_COUNT = this.tasks.size();
         return true;
      }).compose((r) -> {
         return Future.succeededFuture();
      });
   }

   private List parseFile(String rows) {
      List rawTasks = (List)Arrays.stream(rows.split("\n")).filter(Objects::nonNull).filter((s) -> {
         return !s.isEmpty();
      }).filter((l) -> {
         return !l.toLowerCase().contains("keyword");
      }).map((r) -> {
         return r.split(",");
      }).collect(Collectors.toList());
      List tasks = new ArrayList();
      Iterator var4 = rawTasks.iterator();

      while(var4.hasNext()) {
         String[] rawTask = (String[])var4.next();

         try {
            tasks.add(this.validateAndCreate(rawTask));
         } catch (Throwable var7) {
            PrintStream var10000 = System.out;
            int var10001 = rawTasks.indexOf(rawTask);
            var10000.println("Entry error for row '" + var10001 + "': " + var7.getMessage());
         }
      }

      return tasks;
   }

   private Task validateAndCreate(String[] row) throws Exception {
      int i;
      int i;
      if (row.length < 22) {
         i = ColumnIndex.values().length - row.length;
         StringBuilder r = new StringBuilder();

         for(i = ColumnIndex.values().length - i; i < ColumnIndex.values().length - 1; ++i) {
            if (r.length() != 0) {
               r.append(", ");
            }

            r.append(ColumnIndex.values()[i]);
         }

         throw new Exception("Missing collum entries: " + r.toString().trim());
      } else {
         for(i = 0; i < row.length; ++i) {
            String t = row[i];
            Objects.requireNonNull(t, "Field '" + ColumnIndex.values()[i] + "' is empty or missing");
            if (i >= 16 && i <= 18) {
               try {
                  i = Integer.parseInt(t);
               } catch (NumberFormatException var5) {
                  ColumnIndex var10002 = ColumnIndex.values()[i];
                  throw new Exception("Field '" + var10002 + "' is expected to be a number but is: " + t);
               }
            }
         }

         return new Task(row);
      }
   }

   public void massPassword(String rawInput) {
      Iterator var2 = this.tasks.iterator();

      while(var2.hasNext()) {
         Task task = (Task)var2.next();
         task.setPassword(rawInput);
      }

      VertxUtil.sendSignal();
   }

   public void massEditLinkOrKeyword(String rawInput) {
      boolean isYs = false;
      Iterator var3 = this.tasks.iterator();

      while(var3.hasNext()) {
         Task task = (Task)var3.next();
         task.setKeywords(rawInput.toUpperCase(Locale.ROOT).replace("  ", " ").split(" "));
         if (task.getSite() == Site.YEEZY) {
            isYs = true;
         }
      }

      if (!isYs) {
         VertxUtil.sendSignal();
      }

   }

   public void massEditDelay(long newDelay) {
      if (newDelay > 2147483647L) {
         newDelay = 2147483647L;
      }

      Iterator var3 = this.tasks.iterator();

      while(var3.hasNext()) {
         Task task = (Task)var3.next();
         task.setMonitorDelay((int)newDelay);
      }

      VertxUtil.sendSignal();
   }

   public void switchToDefaultDelay() {
      Iterator var1 = this.tasks.iterator();

      while(var1.hasNext()) {
         Task task = (Task)var1.next();
         task.setMonitorDelay((Integer)null);
      }

      VertxUtil.sendSignal();
   }
}

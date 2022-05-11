package io.trickle.account;

import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.util.FileUtils;
import io.trickle.util.Storage;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.shareddata.Lock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountController implements Module, LoadableAsync {
   public static Preferences ACCOUNT_STORE = Preferences.userRoot().node("c31145d854972f36a643e09d3e861c9a");
   public Vertx vertx;
   public String fileName;
   public List accounts = new ArrayList();
   public String LOCK_IDENTITY;
   public static Logger logger = LogManager.getLogger(AccountController.class);
   public AtomicInteger c = new AtomicInteger(0);

   public Account getAccountCyclic() {
      if (this.accounts.isEmpty()) {
         return null;
      } else {
         int var1 = this.accounts.size();
         return var1 == 1 ? (Account)this.accounts.get(0) : (Account)this.accounts.get(this.c.getAndUpdate(AccountController::lambda$getAccountCyclic$5));
      }
   }

   public static String[] lambda$parseFile$8(String var0) {
      return var0.split(":");
   }

   public static void main(String[] var0) {
      System.out.println(Arrays.toString(ACCOUNT_STORE.keys()));
      System.out.println(ACCOUNT_STORE.get("-4895702603361687169", ""));
   }

   public Buffer lambda$load$2(FileSystem var1, Throwable var2) {
      logger.warn("Failed to find '{}'. Proceeding without...", this.fileName.replace("/", ""));
      FileUtils.createAndWrite(var1, Storage.CONFIG_PATH + this.fileName, Buffer.buffer());
      return Buffer.buffer("");
   }

   public void lambda$findAccount$7(String var1, Promise var2, boolean var3, AsyncResult var4) {
      if (var4.succeeded()) {
         Lock var5 = (Lock)var4.result();

         try {
            List var6 = (List)this.accounts.stream().filter(AccountController::lambda$findAccount$6).collect(Collectors.toList());
            if (var6.isEmpty()) {
               var2.complete((Object)null);
            } else {
               Account var7 = (Account)var6.get(ThreadLocalRandom.current().nextInt(var6.size()));
               if (!var3) {
                  this.accounts.remove(var7);
               }

               var2.complete(var7);
            }
         } catch (Throwable var11) {
            logger.warn("Error occurred searching for account: {}", var11.getMessage());
         } finally {
            var5.release();
         }
      } else {
         var2.complete((Object)null);
      }

   }

   public void putAccountSession(Message var1) {
      Account var2 = (Account)var1.body();
      if (var2 != null && var2.getSessionString() != null && !var2.getSessionString().isBlank()) {
         try {
            ACCOUNT_STORE.put(var2.sessionCacheKey(), var2.getSessionString());
            logger.info("Saved account OK!");
         } catch (Throwable var4) {
            var4.printStackTrace();
         }
      }

   }

   public int loadedAccounts() {
      return this.accounts.size();
   }

   public void terminate() {
      try {
         ACCOUNT_STORE.sync();
      } catch (BackingStoreException var2) {
         var2.printStackTrace();
      }

      this.accounts.clear();
   }

   public static String lambda$load$3(Buffer var0) {
      return var0.toString(StandardCharsets.UTF_8);
   }

   public static boolean lambda$findAccount$6(String var0, Account var1) {
      String var2 = var1.getUser().split("@")[1];
      String var3 = var1.getUser().split("@")[0].replaceAll("(?<=\\+)[^@]*", "").replace("+", "");
      return var0.equalsIgnoreCase(var3 + "@" + var2);
   }

   public AccountController(Vertx var1) {
      this.vertx = var1;
      this.fileName = "/accounts.txt";
      this.LOCK_IDENTITY = UUID.randomUUID().toString();
   }

   public void initialise() {
      this.vertx.eventBus().localConsumer("accounts.writer", this::putAccount);
      this.vertx.eventBus().localConsumer("accounts.writer.session", this::putAccountSession);
      logger.debug("Initialised.");
   }

   public void putAccount(Message var1) {
      String var2 = ((Account)var1.body()).toString();
      this.vertx.fileSystem().open(Storage.CONFIG_PATH + this.fileName, (new OpenOptions()).setAppend(true)).onComplete(AccountController::lambda$putAccount$1);
   }

   public List parseFile(String var1) {
      return (List)Arrays.stream(var1.split("\n")).filter(Objects::nonNull).map(String::trim).map(AccountController::lambda$parseFile$8).map(Account::fromArray).filter(Objects::nonNull).collect(Collectors.toList());
   }

   public static void lambda$putAccount$0(String var0, AsyncResult var1) {
      if (var1.failed()) {
         logger.warn("Failed to save account to storage");
      } else {
         logger.info("Saved account: {}", var0);
      }

   }

   public Future findAccount(String var1, boolean var2) {
      if (this.accounts.isEmpty()) {
         return Future.succeededFuture((Object)null);
      } else {
         Promise var3 = Promise.promise();
         this.vertx.sharedData().getLocalLockWithTimeout(this.LOCK_IDENTITY, 30000L).onComplete(this::lambda$findAccount$7);
         return var3.future();
      }
   }

   public AccountController(Vertx var1, String var2) {
      this.vertx = var1;
      this.fileName = var2;
      this.LOCK_IDENTITY = UUID.randomUUID().toString();
   }

   public static Future lambda$load$4(Boolean var0) {
      return Future.succeededFuture();
   }

   public static int lambda$getAccountCyclic$5(int var0, int var1) {
      ++var1;
      return var1 < var0 ? var1 : 0;
   }

   public static void lambda$putAccount$1(String var0, AsyncResult var1) {
      if (var1.succeeded()) {
         Buffer var2 = Buffer.buffer(var0 + "\n");
         AsyncFile var3 = (AsyncFile)var1.result();
         var3.write(var2, AccountController::lambda$putAccount$0);
      } else {
         logger.error("Failed to find accounts storage");
      }

   }

   public Future load() {
      FileSystem var1 = this.vertx.fileSystem();
      Future var10000 = var1.readFile(Storage.CONFIG_PATH + this.fileName).otherwise(this::lambda$load$2).map(AccountController::lambda$load$3).map(this::parseFile);
      List var10001 = this.accounts;
      Objects.requireNonNull(var10001);
      return var10000.map(var10001::addAll).compose(AccountController::lambda$load$4);
   }
}

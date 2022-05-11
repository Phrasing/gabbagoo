/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.account.Account
 *  io.trickle.core.api.LoadableAsync
 *  io.trickle.core.api.Module
 *  io.trickle.util.FileUtils
 *  io.trickle.util.Storage
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.eventbus.Message
 *  io.vertx.core.file.AsyncFile
 *  io.vertx.core.file.FileSystem
 *  io.vertx.core.file.OpenOptions
 *  io.vertx.core.shareddata.Lock
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.account;

import io.trickle.account.Account;
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

public class AccountController
implements Module,
LoadableAsync {
    public static Preferences ACCOUNT_STORE = Preferences.userRoot().node("c31145d854972f36a643e09d3e861c9a");
    public Vertx vertx;
    public String fileName;
    public List<Account> accounts;
    public String LOCK_IDENTITY;
    public static Logger logger = LogManager.getLogger(AccountController.class);
    public AtomicInteger c = new AtomicInteger(0);

    public Account getAccountCyclic() {
        if (!this.accounts.isEmpty()) int n;
        return (n = this.accounts.size()) == 1 ? this.accounts.get(0) : this.accounts.get(this.c.getAndUpdate(arg_0 -> AccountController.lambda$getAccountCyclic$5(n, arg_0)));
        return null;
    }

    public static String[] lambda$parseFile$8(String string) {
        return string.split(":");
    }

    public static void main(String[] stringArray) {
        System.out.println(Arrays.toString(ACCOUNT_STORE.keys()));
        System.out.println(ACCOUNT_STORE.get("-4895702603361687169", ""));
    }

    public Buffer lambda$load$2(FileSystem fileSystem, Throwable throwable) {
        logger.warn("Failed to find '{}'. Proceeding without...", (Object)this.fileName.replace("/", ""));
        FileUtils.createAndWrite((FileSystem)fileSystem, (String)(Storage.CONFIG_PATH + this.fileName), (Buffer)Buffer.buffer());
        return Buffer.buffer((String)"");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void lambda$findAccount$7(String string, Promise promise, boolean bl, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            Lock lock = (Lock)asyncResult.result();
            try {
                List list = this.accounts.stream().filter(arg_0 -> AccountController.lambda$findAccount$6(string, arg_0)).collect(Collectors.toList());
                if (list.isEmpty()) {
                    promise.complete(null);
                }
                Account account = (Account)list.get(ThreadLocalRandom.current().nextInt(list.size()));
                if (!bl) {
                    this.accounts.remove(account);
                }
                promise.complete((Object)account);
            }
            catch (Throwable throwable) {
                logger.warn("Error occurred searching for account: {}", (Object)throwable.getMessage());
            }
            finally {
                lock.release();
            }
        } else {
            promise.complete(null);
        }
    }

    public void putAccountSession(Message message) {
        Account account = (Account)message.body();
        if (account == null) return;
        if (account.getSessionString() == null) return;
        if (account.getSessionString().isBlank()) return;
        try {
            ACCOUNT_STORE.put(account.sessionCacheKey(), account.getSessionString());
            logger.info("Saved account OK!");
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public int loadedAccounts() {
        return this.accounts.size();
    }

    public void terminate() {
        try {
            ACCOUNT_STORE.sync();
        }
        catch (BackingStoreException backingStoreException) {
            backingStoreException.printStackTrace();
        }
        this.accounts.clear();
    }

    public static String lambda$load$3(Buffer buffer) {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public static boolean lambda$findAccount$6(String string, Account account) {
        String string2 = account.getUser().split("@")[1];
        String string3 = account.getUser().split("@")[0].replaceAll("(?<=\\+)[^@]*", "").replace("+", "");
        return string.equalsIgnoreCase(string3 + "@" + string2);
    }

    public AccountController(Vertx vertx) {
        this.accounts = new ArrayList<Account>();
        this.vertx = vertx;
        this.fileName = "/accounts.txt";
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    public void initialise() {
        this.vertx.eventBus().localConsumer("accounts.writer", this::putAccount);
        this.vertx.eventBus().localConsumer("accounts.writer.session", this::putAccountSession);
        logger.debug("Initialised.");
    }

    public void putAccount(Message message) {
        String string = ((Account)message.body()).toString();
        this.vertx.fileSystem().open(Storage.CONFIG_PATH + this.fileName, new OpenOptions().setAppend(true)).onComplete(arg_0 -> AccountController.lambda$putAccount$1(string, arg_0));
    }

    public List parseFile(String string) {
        return Arrays.stream(string.split("\n")).filter(Objects::nonNull).map(String::trim).map(AccountController::lambda$parseFile$8).map(Account::fromArray).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static void lambda$putAccount$0(String string, AsyncResult asyncResult) {
        if (asyncResult.failed()) {
            logger.warn("Failed to save account to storage");
        } else {
            logger.info("Saved account: {}", (Object)string);
        }
    }

    public Future findAccount(String string, boolean bl) {
        if (this.accounts.isEmpty()) {
            return Future.succeededFuture(null);
        }
        Promise promise = Promise.promise();
        this.vertx.sharedData().getLocalLockWithTimeout(this.LOCK_IDENTITY, 30000L).onComplete(arg_0 -> this.lambda$findAccount$7(string, promise, bl, arg_0));
        return promise.future();
    }

    public AccountController(Vertx vertx, String string) {
        this.accounts = new ArrayList<Account>();
        this.vertx = vertx;
        this.fileName = string;
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    public static Future lambda$load$4(Boolean bl) {
        return Future.succeededFuture();
    }

    public static int lambda$getAccountCyclic$5(int n, int n2) {
        return ++n2 < n ? n2 : 0;
    }

    public static void lambda$putAccount$1(String string, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            Buffer buffer = Buffer.buffer((String)(string + "\n"));
            AsyncFile asyncFile = (AsyncFile)asyncResult.result();
            asyncFile.write((Object)buffer, arg_0 -> AccountController.lambda$putAccount$0(string, arg_0));
        } else {
            logger.error("Failed to find accounts storage");
        }
    }

    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + this.fileName).otherwise(arg_0 -> this.lambda$load$2(fileSystem, arg_0)).map(AccountController::lambda$load$3).map(this::parseFile).map(this.accounts::addAll).compose(AccountController::lambda$load$4);
    }
}

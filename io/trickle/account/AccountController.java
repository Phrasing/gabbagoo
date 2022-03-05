/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
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
    public static Logger logger;
    public Vertx vertx;
    public AtomicInteger c = new AtomicInteger(0);
    public List<Account> accounts = new ArrayList<Account>();
    public String LOCK_IDENTITY;
    public static Preferences ACCOUNT_STORE;
    public String fileName;

    public AccountController(Vertx vertx) {
        this.vertx = vertx;
        this.fileName = "/accounts.txt";
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    @Override
    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + this.fileName).otherwise(arg_0 -> this.lambda$load$2(fileSystem, arg_0)).map(AccountController::lambda$load$3).map(this::parseFile).map(this.accounts::addAll).compose(AccountController::lambda$load$4);
    }

    public static int lambda$getAccountCyclic$5(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void lambda$findAccount$7(String string, Promise promise, boolean bl, AsyncResult asyncResult) {
        if (!asyncResult.succeeded()) {
            promise.complete(null);
            return;
        }
        Lock lock = (Lock)asyncResult.result();
        try {
            List list = this.accounts.stream().filter(arg_0 -> AccountController.lambda$findAccount$6(string, arg_0)).collect(Collectors.toList());
            if (list.isEmpty()) {
                promise.complete(null);
                return;
            }
            Account account = (Account)list.get(ThreadLocalRandom.current().nextInt(list.size()));
            if (!bl) {
                this.accounts.remove(account);
            }
            promise.complete((Object)account);
            return;
        }
        catch (Throwable throwable) {
            logger.warn("Error occurred searching for account: {}", (Object)throwable.getMessage());
            return;
        }
        finally {
            lock.release();
        }
    }

    public Account getAccountCyclic() {
        Account account;
        if (this.accounts.isEmpty()) {
            return null;
        }
        int n = this.accounts.size();
        if (n == 1) {
            account = this.accounts.get(0);
            return account;
        }
        account = this.accounts.get(this.c.getAndUpdate(arg_0 -> AccountController.lambda$getAccountCyclic$5(n, arg_0)));
        return account;
    }

    public static String lambda$load$3(Buffer buffer) {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public List parseFile(String string) {
        return Arrays.stream(string.split("\n")).filter(Objects::nonNull).map(String::trim).map(AccountController::lambda$parseFile$8).map(Account::fromArray).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void initialise() {
        this.vertx.eventBus().localConsumer("accounts.writer", this::putAccount);
        this.vertx.eventBus().localConsumer("accounts.writer.session", this::putAccountSession);
        logger.debug("Initialised.");
    }

    public static void main(String[] stringArray) {
        System.out.println(Arrays.toString(ACCOUNT_STORE.keys()));
        System.out.println(ACCOUNT_STORE.get("-4895702603361687169", ""));
    }

    static {
        ACCOUNT_STORE = Preferences.userRoot().node("c31145d854972f36a643e09d3e861c9a");
        logger = LogManager.getLogger(AccountController.class);
    }

    public Future findAccount(String string, boolean bl) {
        if (this.accounts.isEmpty()) {
            return Future.succeededFuture(null);
        }
        Promise promise = Promise.promise();
        this.vertx.sharedData().getLocalLockWithTimeout(this.LOCK_IDENTITY, 30000L).onComplete(arg_0 -> this.lambda$findAccount$7(string, promise, bl, arg_0));
        return promise.future();
    }

    @Override
    public void terminate() {
        try {
            ACCOUNT_STORE.sync();
        }
        catch (BackingStoreException backingStoreException) {
            backingStoreException.printStackTrace();
        }
        this.accounts.clear();
    }

    public static void lambda$putAccount$0(String string, AsyncResult asyncResult) {
        if (asyncResult.failed()) {
            logger.warn("Failed to save account to storage");
            return;
        }
        logger.info("Saved account: {}", (Object)string);
    }

    public Buffer lambda$load$2(FileSystem fileSystem, Throwable throwable) {
        logger.warn("Failed to find '{}'. Proceeding without...", (Object)this.fileName.replace("/", ""));
        FileUtils.createAndWrite(fileSystem, Storage.CONFIG_PATH + this.fileName, Buffer.buffer());
        return Buffer.buffer((String)"");
    }

    public AccountController(Vertx vertx, String string) {
        this.vertx = vertx;
        this.fileName = string;
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    public static void lambda$putAccount$1(String string, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            Buffer buffer = Buffer.buffer((String)(string + "\n"));
            AsyncFile asyncFile = (AsyncFile)asyncResult.result();
            asyncFile.write((Object)buffer, arg_0 -> AccountController.lambda$putAccount$0(string, arg_0));
            return;
        }
        logger.error("Failed to find accounts storage");
    }

    public static boolean lambda$findAccount$6(String string, Account account) {
        String string2 = account.getUser().split("@")[1];
        String string3 = account.getUser().split("@")[0].replaceAll("(?<=\\+)[^@]*", "").replace("+", "");
        return string.equalsIgnoreCase(string3 + "@" + string2);
    }

    public void putAccountSession(Message message) {
        Account account = (Account)message.body();
        if (account == null) return;
        if (account.getSessionString() == null) return;
        if (account.getSessionString().isBlank()) return;
        try {
            ACCOUNT_STORE.put(account.sessionCacheKey(), account.getSessionString());
            logger.info("Saved account OK!");
            return;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public int loadedProxies() {
        return this.accounts.size();
    }

    public void putAccount(Message message) {
        String string = ((Account)message.body()).toString();
        this.vertx.fileSystem().open(Storage.CONFIG_PATH + this.fileName, new OpenOptions().setAppend(true)).onComplete(arg_0 -> AccountController.lambda$putAccount$1(string, arg_0));
    }

    public static Future lambda$load$4(Boolean bl) {
        return Future.succeededFuture();
    }

    public static String[] lambda$parseFile$8(String string) {
        return string.split(":");
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.basicgui.BasicGUI
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.util.CommandLineHandler
 *  io.trickle.util.ScriptEngineHelper
 *  io.trickle.util.Utils
 *  io.vertx.core.AsyncResult
 *  org.apache.logging.log4j.LogManager
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.conscrypt.Conscrypt
 */
package io.trickle;

import io.trickle.basicgui.BasicGUI;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.util.CommandLineHandler;
import io.trickle.util.ScriptEngineHelper;
import io.trickle.util.Utils;
import io.vertx.core.AsyncResult;
import java.security.Provider;
import java.security.Security;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.conscrypt.Conscrypt;

public class App {
    public static int MINOR;
    public static BasicGUI gui;
    public static int MAJOR;
    public static int PATCH;
    public static String SESSION_HASH;
    public static Engine engine;

    public static void init() {
        try {
            CommandLineHandler.requestKey();
            Runtime.getRuntime().addShutdownHook(new Thread(App::lambda$init$1));
            try {
                engine.initialisePromise().get(1L, TimeUnit.MINUTES);
            }
            catch (TimeoutException timeoutException) {
                // empty catch block
            }
            CommandLineHandler.greet();
        }
        catch (Throwable throwable) {
            System.exit(1);
        }
    }

    public static void lambda$init$1() {
        System.out.println("Terminating...");
        engine.terminate();
        VertxSingleton.INSTANCE.get().close().onFailure(App::lambda$init$0);
        LogManager.shutdown();
        System.gc();
    }

    public static void initRichPresence() {
        String string = System.getProperty("os.arch");
        if (string == null) return;
        if (!string.contains("aarch")) return;
    }

    public static void waitForExit() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            try {
                String string = scanner.next().toLowerCase(Locale.ROOT).trim();
                if (string.contains("9") || string.contains("exit") || string.contains("q")) {
                    System.exit(1);
                    continue;
                }
                if (string.contains("5") || string.contains("gui")) {
                    App.initGui();
                    continue;
                }
                if (string.contains("task") || string.contains("t")) {
                    CommandLineHandler.stopTasks();
                    System.out.println("Stopped task");
                    continue;
                }
                if (!string.contains("engine") && !string.contains("e")) continue;
                VertxSingleton.INSTANCE.get().close(App::lambda$waitForExit$2);
            }
            catch (Throwable throwable) {}
        }
    }

    public static void main(String[] stringArray) {
        System.out.printf("Starting Trickle v%d.%d.%d%n", 1, 0, 278);
        App.initRichPresence();
        ScriptEngineHelper.test();
        Utils.ensureBrotli();
        App.init();
        App.waitForExit();
    }

    static {
        PATCH = 278;
        MAJOR = 1;
        MINOR = 0;
        engine = Engine.get();
        gui = null;
        SESSION_HASH = UUID.randomUUID().toString();
        System.setProperty("vertx.disableHttpHeadersValidation", "true");
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        Security.addProvider((Provider)new BouncyCastleProvider());
        System.err.close();
        System.setErr(System.out);
    }

    public static void lambda$waitForExit$2(AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            System.out.println("Stopped engine!");
        } else if (asyncResult.cause() != null) {
            asyncResult.cause().printStackTrace();
        } else {
            System.out.println("Failed to close");
        }
    }

    public static void initGui() {
        if (gui != null) {
            if (!gui.isClosed()) return;
        }
        gui = new BasicGUI();
    }

    public static void lambda$init$0(Throwable throwable) {
    }
}

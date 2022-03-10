/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.aayushatharva.brotli4j.Brotli4jLoader
 *  com.aayushatharva.brotli4j.decoder.Decoder
 *  com.aayushatharva.brotli4j.decoder.DecoderJNI$Status
 *  com.aayushatharva.brotli4j.decoder.DirectDecompress
 *  com.aayushatharva.brotli4j.encoder.Encoder
 *  com.fuzzy.aycd.autosolve.model.AutoSolveAccount
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.util;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoder;
import com.aayushatharva.brotli4j.decoder.DecoderJNI;
import com.aayushatharva.brotli4j.decoder.DirectDecompress;
import com.aayushatharva.brotli4j.encoder.Encoder;
import com.fuzzy.aycd.autosolve.model.AutoSolveAccount;
import io.trickle.App;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.proxy.ProxyController;
import io.trickle.task.TaskController;
import io.trickle.task.sites.yeezy.util.CodeScreen;
import io.trickle.util.ScriptEngineHelper;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CommandLineHandler {
    public static void checkWebhook() {
        String string = Storage.DISCORD_WEBHOOK;
        String string2 = string.isEmpty() ? "Webhook not set! Would you like to set one? Y/N:" : String.format("Webhook set as '%s'. Would you like to change it? Y/N:", string);
        Scanner scanner = new Scanner(System.in);
        System.out.println(string2);
        while (scanner.hasNext()) {
            String string3 = scanner.next().toLowerCase();
            if (string3.contains("y")) {
                CommandLineHandler.askForWebhook();
                return;
            }
            if (string3.contains("n")) {
                return;
            }
            System.out.println(string2);
        }
    }

    public static void setupAutoSolve() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("AutoSolve Settings");
        System.out.println("API_KEY " + (Storage.AYCD_API_KEY.isBlank() ? "None" : Storage.AYCD_API_KEY));
        System.out.println("ACCESS_TOKEN " + (Storage.AYCD_ACCESS_TOKEN.isBlank() ? "None" : Storage.AYCD_ACCESS_TOKEN));
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        Supplier<String> supplier = () -> CommandLineHandler.lambda$setupAutoSolve$2(atomicBoolean);
        System.out.println(supplier.get());
        while (scanner.hasNext()) {
            try {
                String string = scanner.next().trim();
                if (atomicBoolean.get()) {
                    Storage.setAycdApiKey(string);
                    System.out.println("API_KEY set: " + string);
                    atomicBoolean.set(false);
                } else {
                    Storage.setAycdAccessToken(string);
                    System.out.println("ACCESS_TOKEN set: " + string);
                    AutoSolveAccount autoSolveAccount = AutoSolveAccount.of((String)Storage.AYCD_ACCESS_TOKEN, (String)Storage.AYCD_API_KEY);
                    if (autoSolveAccount.isValid()) {
                        return;
                    }
                    System.out.println("Credentials Invalid. Try again:");
                    atomicBoolean.set(true);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            System.out.println(supplier.get());
        }
    }

    public static void requestKey() {
        if (!Storage.ACCESS_KEY.isEmpty()) return;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your key: ");
        CommandLineHandler.handleScan(scanner);
    }

    public static void askForHarvesterCount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the desired harvester count (current: " + Storage.HARVESTER_COUNT_YS + "): ");
        while (scanner.hasNext()) {
            String string = scanner.next().trim();
            try {
                int n = Integer.parseInt(string);
                if (n >= 0) {
                    Storage.setHarvesterCountYs(n);
                    System.out.printf("Harvester count set: '%s'%n", string);
                    return;
                }
                System.out.println("Harvester count should be at least 0");
            }
            catch (Throwable throwable) {
                System.out.println("Please enter a valid number!");
            }
            System.out.println("Enter the desired harvester count (current: " + Storage.HARVESTER_COUNT_YS + "): ");
        }
    }

    public static void startTasks() {
        CompletableFuture.runAsync(CommandLineHandler::lambda$startTasks$3);
    }

    public static void stop() {
        CommandLineHandler.stopTasks();
        System.exit(1);
    }

    public static void askForWebhook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the webhook uri: ");
        while (scanner.hasNext()) {
            String string = scanner.next().trim();
            if (string.contains("discord") || string.contains("webhooks.aycd") || string.contains("webhooks.tidalmarket.com") || string.contains("api.soleware.io/webhooks")) {
                Storage.setDiscordWebhook(string);
                System.out.printf("Discord webhook set: '%s'%n", string);
                return;
            }
            System.out.println("Enter the webhook uri: ");
        }
    }

    public static void lambda$waitMenu$0(String string) {
        System.out.println("received: " + string);
    }

    public static void askForAutoSolveCreds(Scanner scanner) {
        boolean bl = true;
        while (scanner.hasNext()) {
            System.out.println("???");
            String string = bl ? "Enter your API_KEY" : "Enter your ACCESS_TOKEN";
            System.out.println(string);
            String string2 = scanner.next().trim();
            if (!string2.isBlank()) {
                if (bl) {
                    Storage.setAycdApiKey(string2);
                    System.out.println("API_KEY set: " + string2);
                    bl = false;
                    continue;
                }
                Storage.setAycdAccessToken(string2);
                System.out.println("ACCESS_TOKEN set: " + string2);
                AutoSolveAccount autoSolveAccount = AutoSolveAccount.of((String)Storage.AYCD_ACCESS_TOKEN, (String)Storage.AYCD_API_KEY);
                if (autoSolveAccount.isValid()) {
                    System.out.println("Valid");
                    return;
                }
                System.out.println("Credentials Invalid. Try again:");
                bl = true;
                continue;
            }
            System.out.println(string);
        }
    }

    public static void waitMenu() {
        System.out.println();
        String string = "1. Start tasks\n3. Configure Harvester Count\n4. Configure AutoSolve\n5. Open Settings UI\n8. Set Webhook\n9. Exit\n";
        System.out.println(string.trim());
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            try {
                int n = scanner.nextInt();
                switch (n) {
                    case 1: {
                        CommandLineHandler.start();
                        return;
                    }
                    case 3: {
                        CommandLineHandler.askForHarvesterCount();
                        break;
                    }
                    case 4: {
                        CommandLineHandler.setupAutoSolve();
                        break;
                    }
                    case 5: {
                        App.initGui();
                        break;
                    }
                    case 8: {
                        CommandLineHandler.askForWebhook();
                        break;
                    }
                    case 9: {
                        CommandLineHandler.stop();
                        return;
                    }
                    case 7777: {
                        Object object;
                        try {
                            Brotli4jLoader.ensureAvailability();
                            object = Encoder.compress((byte[])"Meow".getBytes());
                            DirectDecompress directDecompress = Decoder.decompress((byte[])object);
                            if (directDecompress.getResultStatus() == DecoderJNI.Status.DONE) {
                                System.out.println("Decompression Successful: " + new String(directDecompress.getDecompressedData()));
                            } else {
                                System.out.println("Some Error Occurred While Decompressing");
                            }
                        }
                        catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    case 1337: {
                        int n2 = 0;
                        while (n2 < 10) {
                            CodeScreen.request(1337, "test-test-test").thenAccept(CommandLineHandler::lambda$waitMenu$0);
                            ++n2;
                        }
                        return;
                    }
                    case 420420: {
                        Object object;
                        try {
                            object = ScriptEngineHelper.calculateTime("c62913f82818e7f8d800486099230cdfbbaa3a2b849f9f72d5644351ebb20a267934cab127cc2f2a123eead2f0056c4aebd46e4773d85cbb53a214cba4dc612c\udb40\udd38\udb40\udd33\udb40\udd39");
                            System.out.println((String)object);
                        }
                        catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    case 54545: {
                        VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://headers.cf/tcp/?format=raw").as(BodyCodec.string()).send().onSuccess(CommandLineHandler::lambda$waitMenu$1);
                        break;
                    }
                }
            }
            catch (Throwable throwable) {
                CommandLineHandler.waitMenu();
                return;
            }
        }
    }

    public static void waitForStart() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Start tasks? Y/N:");
        while (scanner.hasNext()) {
            String string = scanner.next().toLowerCase();
            if (string.contains("y")) {
                CommandLineHandler.startTasks();
                return;
            }
            if (string.contains("n")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            System.out.println("Start tasks? Y/N:");
        }
    }

    public static void lambda$startTasks$3() {
        ((TaskController)Engine.get().getModule(Controller.TASK)).startTasks();
    }

    public static void greet() {
        Engine engine = Engine.get();
        int n = ((ProxyController)engine.getModule(Controller.PROXY)).loadedProxies();
        int n2 = ((AccountController)engine.getModule(Controller.ACCOUNT)).loadedProxies();
        int n3 = ((TaskController)engine.getModule(Controller.TASK)).profileCount();
        int n4 = ((TaskController)engine.getModule(Controller.TASK)).countTasks();
        System.out.printf("%s - %s%n", Utils.centerString(7, "TASKS"), Utils.centerStringBraced(7, String.valueOf(n4)));
        System.out.printf("%s - %s%n", Utils.centerString(7, "PROXY"), Utils.centerStringBraced(7, String.valueOf(n)));
        System.out.printf("%s - %s%n", Utils.centerString(7, "ACCOUNT"), Utils.centerStringBraced(7, String.valueOf(n2)));
        System.out.printf("%s - %s%n", Utils.centerString(7, "PROFILE"), Utils.centerStringBraced(7, String.valueOf(n3)));
        System.out.printf("%s - %s%n", Utils.centerString(7, "WEBHOOK"), Utils.centerStringBraced(7, Storage.DISCORD_WEBHOOK.isEmpty() ? "NOT_SET" : "SET"));
        System.out.printf("%s - %s%n", Utils.centerString(7, "HARVESTERS"), Utils.centerStringBraced(7, String.valueOf(Storage.HARVESTER_COUNT_YS)));
        CommandLineHandler.waitMenu();
    }

    public static void stopTasks() {
        CompletableFuture.runAsync(CommandLineHandler::lambda$stopTasks$4);
    }

    public static void handleScan(Scanner scanner) {
        while (scanner.hasNext()) {
            try {
                String string = scanner.next().trim();
                if (!string.isEmpty()) {
                    Storage.setAccessKey(string);
                    return;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            System.out.println("Please enter your access key to proceed:");
        }
    }

    public static void start() {
        System.out.println("Starting tasks...");
        CommandLineHandler.startTasks();
    }

    public static void lambda$stopTasks$4() {
        ((TaskController)Engine.get().getModule(Controller.TASK)).stopTasks();
    }

    public static String lambda$setupAutoSolve$2(AtomicBoolean atomicBoolean) {
        if (!atomicBoolean.get()) return "Enter your ACCESS_TOKEN:";
        return "Enter your API_KEY:";
    }

    public static void lambda$waitMenu$1(HttpResponse httpResponse) {
        System.out.println((String)httpResponse.body());
    }
}


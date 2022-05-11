package io.trickle.util;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoder;
import com.aayushatharva.brotli4j.decoder.DirectDecompress;
import com.aayushatharva.brotli4j.decoder.DecoderJNI.Status;
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
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CommandLineHandler {
   public static void greet() {
      Engine var0 = Engine.get();
      int var1 = ((ProxyController)var0.getModule(Controller.PROXY)).loadedProxies();
      int var2 = ((AccountController)var0.getModule(Controller.ACCOUNT)).loadedAccounts();
      int var3 = ((TaskController)var0.getModule(Controller.TASK)).profileCount();
      int var4 = ((TaskController)var0.getModule(Controller.TASK)).countTasks();
      System.out.printf("%s - %s%n", Utils.centerString(7, "TASKS"), Utils.centerStringBraced(7, String.valueOf(var4)));
      System.out.printf("%s - %s%n", Utils.centerString(7, "PROXY"), Utils.centerStringBraced(7, String.valueOf(var1)));
      System.out.printf("%s - %s%n", Utils.centerString(7, "ACCOUNT"), Utils.centerStringBraced(7, String.valueOf(var2)));
      System.out.printf("%s - %s%n", Utils.centerString(7, "PROFILE"), Utils.centerStringBraced(7, String.valueOf(var3)));
      System.out.printf("%s - %s%n", Utils.centerString(7, "WEBHOOK"), Utils.centerStringBraced(7, Storage.DISCORD_WEBHOOK.isEmpty() ? "NOT_SET" : "SET"));
      System.out.printf("%s - %s%n", Utils.centerString(7, "HARVESTERS"), Utils.centerStringBraced(7, String.valueOf(Storage.HARVESTER_COUNT_YS)));
      waitMenu();
   }

   public static void start() {
      System.out.println("Starting tasks...");
      startTasks();
   }

   public static void handleScan(Scanner var0) {
      while(true) {
         label20: {
            if (var0.hasNext()) {
               try {
                  String var1 = var0.next().trim();
                  if (var1.isEmpty()) {
                     break label20;
                  }

                  Storage.setAccessKey(var1);
               } catch (Exception var2) {
                  break label20;
               }
            }

            return;
         }

         System.out.println("Please enter your access key to proceed:");
      }
   }

   public static String lambda$setupAutoSolve$2(AtomicBoolean var0) {
      return var0.get() ? "Enter your API_KEY:" : "Enter your ACCESS_TOKEN:";
   }

   public static void checkWebhook() {
      String var0 = Storage.DISCORD_WEBHOOK;
      String var1;
      if (var0.isEmpty()) {
         var1 = "Webhook not set! Would you like to set one? Y/N:";
      } else {
         var1 = String.format("Webhook set as '%s'. Would you like to change it? Y/N:", var0);
      }

      Scanner var2 = new Scanner(System.in);
      System.out.println(var1);

      while(var2.hasNext()) {
         String var3 = var2.next().toLowerCase();
         if (var3.contains("y")) {
            askForWebhook();
            break;
         }

         if (var3.contains("n")) {
            break;
         }

         System.out.println(var1);
      }

   }

   public static void lambda$waitMenu$1(HttpResponse var0) {
      System.out.println((String)var0.body());
   }

   public static void lambda$waitMenu$0(String var0) {
      System.out.println("received: " + var0);
   }

   public static void waitForStart() {
      Scanner var0 = new Scanner(System.in);
      System.out.println("Start tasks? Y/N:");

      for(; var0.hasNext(); System.out.println("Start tasks? Y/N:")) {
         String var1 = var0.next().toLowerCase();
         if (var1.contains("y")) {
            startTasks();
            break;
         }

         if (var1.contains("n")) {
            System.out.println("Goodbye!");
            System.exit(0);
         }
      }

   }

   public static void requestKey() {
      if (Storage.ACCESS_KEY.isEmpty()) {
         Scanner var0 = new Scanner(System.in);
         System.out.println("Enter your key: ");
         handleScan(var0);
      }

   }

   public static void askForHarvesterCount() {
      Scanner var0 = new Scanner(System.in);
      System.out.println("Enter the desired harvester count (current: " + Storage.HARVESTER_COUNT_YS + "): ");

      for(; var0.hasNext(); System.out.println("Enter the desired harvester count (current: " + Storage.HARVESTER_COUNT_YS + "): ")) {
         String var1 = var0.next().trim();

         try {
            int var2 = Integer.parseInt(var1);
            if (var2 >= 0) {
               Storage.setHarvesterCountYs(var2);
               System.out.printf("Harvester count set: '%s'%n", var1);
               break;
            }

            System.out.println("Harvester count should be at least 0");
         } catch (Throwable var3) {
            System.out.println("Please enter a valid number!");
         }
      }

   }

   public static void askForAutoSolveCreds(Scanner var0) {
      boolean var1 = true;

      while(var0.hasNext()) {
         System.out.println("???");
         String var2 = var1 ? "Enter your API_KEY" : "Enter your ACCESS_TOKEN";
         System.out.println(var2);
         String var3 = var0.next().trim();
         if (!var3.isBlank()) {
            if (var1) {
               Storage.setAycdApiKey(var3);
               System.out.println("API_KEY set: " + var3);
               var1 = false;
            } else {
               Storage.setAycdAccessToken(var3);
               System.out.println("ACCESS_TOKEN set: " + var3);
               AutoSolveAccount var4 = AutoSolveAccount.of(Storage.AYCD_ACCESS_TOKEN, Storage.AYCD_API_KEY);
               if (var4.isValid()) {
                  System.out.println("Valid");
                  break;
               }

               System.out.println("Credentials Invalid. Try again:");
               var1 = true;
            }
         } else {
            System.out.println(var2);
         }
      }

   }

   public static void stopTasks() {
      CompletableFuture.runAsync(CommandLineHandler::lambda$stopTasks$4);
   }

   public static void startTasks() {
      CompletableFuture.runAsync(CommandLineHandler::lambda$startTasks$3);
   }

   public static void setupAutoSolve() {
      Scanner var0 = new Scanner(System.in);
      System.out.println("AutoSolve Settings");
      PrintStream var10000 = System.out;
      String var10001 = Storage.AYCD_API_KEY.isBlank() ? "None" : Storage.AYCD_API_KEY;
      var10000.println("API_KEY " + var10001);
      System.out.println("ACCESS_TOKEN " + (Storage.AYCD_ACCESS_TOKEN.isBlank() ? "None" : Storage.AYCD_ACCESS_TOKEN));
      AtomicBoolean var1 = new AtomicBoolean(true);
      Supplier var2 = CommandLineHandler::lambda$setupAutoSolve$2;
      System.out.println((String)var2.get());

      for(; var0.hasNext(); System.out.println((String)var2.get())) {
         try {
            String var3 = var0.next().trim();
            if (var1.get()) {
               Storage.setAycdApiKey(var3);
               System.out.println("API_KEY set: " + var3);
               var1.set(false);
            } else {
               Storage.setAycdAccessToken(var3);
               System.out.println("ACCESS_TOKEN set: " + var3);
               AutoSolveAccount var4 = AutoSolveAccount.of(Storage.AYCD_ACCESS_TOKEN, Storage.AYCD_API_KEY);
               if (var4.isValid()) {
                  break;
               }

               System.out.println("Credentials Invalid. Try again:");
               var1.set(true);
            }
         } catch (Throwable var5) {
         }
      }

   }

   public static void lambda$stopTasks$4() {
      ((TaskController)Engine.get().getModule(Controller.TASK)).stopTasks();
   }

   public static void lambda$startTasks$3() {
      ((TaskController)Engine.get().getModule(Controller.TASK)).startTasks();
   }

   public static void waitMenu() {
      System.out.println();
      String var0 = "1. Start tasks\n3. Configure Harvester Count\n4. Configure AutoSolve\n5. Open Settings UI\n8. Set Webhook\n9. Exit\n";
      System.out.println(var0.trim());
      Scanner var1 = new Scanner(System.in);

      while(var1.hasNext()) {
         try {
            int var2 = var1.nextInt();
            switch (var2) {
               case 1:
                  start();
                  return;
               case 3:
                  askForHarvesterCount();
                  break;
               case 4:
                  setupAutoSolve();
                  break;
               case 5:
                  App.initGui();
                  break;
               case 8:
                  askForWebhook();
                  break;
               case 9:
                  stop();
                  return;
               case 7777:
                  try {
                     Brotli4jLoader.ensureAvailability();
                     byte[] var8 = Encoder.compress("Meow".getBytes());
                     DirectDecompress var4 = Decoder.decompress(var8);
                     if (var4.getResultStatus() == Status.DONE) {
                        PrintStream var10000 = System.out;
                        String var10001 = new String(var4.getDecompressedData());
                        var10000.println("Decompression Successful: " + var10001);
                     } else {
                        System.out.println("Some Error Occurred While Decompressing");
                     }
                  } catch (Throwable var5) {
                     var5.printStackTrace();
                  }
               case 1337:
                  for(int var9 = 0; var9 < 10; ++var9) {
                     CodeScreen.request(1337, "test-test-test").thenAccept(CommandLineHandler::lambda$waitMenu$0);
                  }

                  return;
               case 420420:
                  try {
                     String var3 = ScriptEngineHelper.calculateTime("c62913f82818e7f8d800486099230cdfbbaa3a2b849f9f72d5644351ebb20a267934cab127cc2f2a123eead2f0056c4aebd46e4773d85cbb53a214cba4dc612c\udb40\udd38\udb40\udd33\udb40\udd39");
                     System.out.println(var3);
                  } catch (Throwable var6) {
                     var6.printStackTrace();
                  }
               case 54545:
                  VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://headers.cf/tcp/?format=raw").as(BodyCodec.string()).send().onSuccess(CommandLineHandler::lambda$waitMenu$1);
            }
         } catch (Throwable var7) {
            waitMenu();
            return;
         }
      }

   }

   public static void stop() {
      stopTasks();
      System.exit(1);
   }

   public static void askForWebhook() {
      Scanner var0 = new Scanner(System.in);
      System.out.println("Enter the webhook uri: ");

      while(var0.hasNext()) {
         String var1 = var0.next().trim();
         if (var1.contains("discord") || var1.contains("webhooks.aycd") || var1.contains("webhooks.tidalmarket.com") || var1.contains("api.soleware.io/webhooks")) {
            Storage.setDiscordWebhook(var1);
            System.out.printf("Discord webhook set: '%s'%n", var1);
            break;
         }

         System.out.println("Enter the webhook uri: ");
      }

   }
}

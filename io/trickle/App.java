package io.trickle;

import io.trickle.basicgui.BasicGUI;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.util.CommandLineHandler;
import io.trickle.util.ScriptEngineHelper;
import io.trickle.util.Utils;
import io.vertx.core.AsyncResult;
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
   public static int MINOR = 0;
   public static BasicGUI gui = null;
   public static int MAJOR = 1;
   public static int PATCH = 278;
   public static String SESSION_HASH = UUID.randomUUID().toString();
   public static Engine engine = Engine.get();

   public static void init() {
      try {
         CommandLineHandler.requestKey();
         Runtime.getRuntime().addShutdownHook(new Thread(App::lambda$init$1));

         try {
            engine.initialisePromise().get(1L, TimeUnit.MINUTES);
         } catch (TimeoutException var1) {
         }

         CommandLineHandler.greet();
      } catch (Throwable var2) {
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
      String var0 = System.getProperty("os.arch");
      if (var0 == null || !var0.contains("aarch")) {
         ;
      }
   }

   public static void waitForExit() {
      Scanner var0 = new Scanner(System.in);

      while(var0.hasNext()) {
         try {
            String var1 = var0.next().toLowerCase(Locale.ROOT).trim();
            if (!var1.contains("9") && !var1.contains("exit") && !var1.contains("q")) {
               if (!var1.contains("5") && !var1.contains("gui")) {
                  if (!var1.contains("task") && !var1.contains("t")) {
                     if (var1.contains("engine") || var1.contains("e")) {
                        VertxSingleton.INSTANCE.get().close(App::lambda$waitForExit$2);
                     }
                  } else {
                     CommandLineHandler.stopTasks();
                     System.out.println("Stopped task");
                  }
               } else {
                  initGui();
               }
            } else {
               System.exit(1);
            }
         } catch (Throwable var2) {
         }
      }

   }

   public static void main(String[] var0) {
      System.out.printf("Starting Trickle v%d.%d.%d%n", 1, 0, 278);
      initRichPresence();
      ScriptEngineHelper.test();
      Utils.ensureBrotli();
      init();
      waitForExit();
   }

   static {
      System.setProperty("vertx.disableHttpHeadersValidation", "true");
      System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
      Security.insertProviderAt(Conscrypt.newProvider(), 1);
      Security.addProvider(new BouncyCastleProvider());
      System.err.close();
      System.setErr(System.out);
   }

   public static void lambda$waitForExit$2(AsyncResult var0) {
      if (var0.succeeded()) {
         System.out.println("Stopped engine!");
      } else if (var0.cause() != null) {
         var0.cause().printStackTrace();
      } else {
         System.out.println("Failed to close");
      }

   }

   public static void initGui() {
      if (gui == null || gui.isClosed()) {
         gui = new BasicGUI();
      }

   }

   public static void lambda$init$0(Throwable var0) {
   }
}

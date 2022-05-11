package io.trickle.task.sites.yeezy.util;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.event.BrowserClosed;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.engine.event.EngineClosed;
import com.teamdev.jxbrowser.net.FormData;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.callback.BeforeSendUploadDataCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.BeforeSendUploadDataCallback.Response;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import java.awt.event.WindowEvent;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import javax.swing.JFrame;

public class Window3DS2 {
   public Predicate termUrlCheck;
   public ContextCompletableFuture callback;
   public String acsMethod;
   public String encodedData;
   public Browser browser;
   public String loadUrl;
   public String userAgent;
   public JFrame frame;
   public HashMap uploadValues;
   public MultiMap acsForm;
   public ContextCompletableFuture urlCallbackShopify;
   public String termURI;
   public String acsURI;

   public HashMap getUploadValues() {
      return this.uploadValues;
   }

   public CompletableFuture invokeShopify() {
      CompletableFuture.runAsync(this::invoke2);
      return this.urlCallbackShopify;
   }

   public void lambda$invoke2$8(String var1) {
      try {
         this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
      } catch (Throwable var3) {
      }

   }

   public BeforeSendUploadDataCallback.Response lambda$invoke0$3(BeforeSendUploadDataCallback.Params var1) {
      if (this.termUrlCheck.test(var1.urlRequest().url()) && var1.uploadData() instanceof FormData) {
         FormData var2 = (FormData)var1.uploadData();
         Iterator var3 = var2.data().iterator();

         while(var3.hasNext()) {
            FormData.Pair var4 = (FormData.Pair)var3.next();
            this.uploadValues.put(var4.key(), var4.value());
         }
      }

      return Response.proceed();
   }

   public String getEncodedData() {
      return this.encodedData;
   }

   public static String getFormPage(String var0, String var1, MultiMap var2) {
      return "        <html>\n            <body>\n                <form method=\"" + var0 + "\" action=\"" + var1 + "\" id=\"Cardinal-CCA-Form\">\n                    <input type=\"hidden\" name=\"PaReq\" value=\"" + var2.get("PaReq") + "\" />\n                    <input type=\"hidden\" name=\"MD\" value=\"" + var2.get("MD") + "\" />\n                    <input type=\"hidden\" name=\"TermUrl\" value=\"" + var2.get("TermUrl") + "\" />\n                </form>\n                <script>\n                    setTimeout(() => document.querySelector('#Cardinal-CCA-Form').submit(), 500);\n                </script>\n            </body>\n        </html>";
   }

   public void invoke0() {
      Engine var1 = Engine.newInstance(this.engineOptions());
      var1.on(EngineClosed.class, this::lambda$invoke0$2);
      var1.network().set(BeforeSendUploadDataCallback.class, this::lambda$invoke0$3);
      this.browser = var1.newBrowser();
      this.browser.on(BrowserClosed.class, this::lambda$invoke0$4);
      this.frame = new JFrame("Confirm 3DS");
      this.frame.setDefaultCloseOperation(2);
      this.frame.addWindowListener(new Window3DS2$1(this, var1));
      BrowserView var2 = BrowserView.newInstance(this.browser);
      this.frame.add(var2, "Center");
      this.frame.setSize(360, 550);
      this.frame.setVisible(true);
      this.callback.thenAcceptAsync(this::lambda$invoke0$5);
      String var3 = getFormPage(this.acsMethod, this.acsURI, this.acsForm);
      String var4 = Base64.getEncoder().encodeToString(var3.getBytes(StandardCharsets.UTF_8));
      String var5 = "data:text/html;base64," + var4;
      this.browser.navigation().loadUrl(var5);
   }

   public String getTermURI() {
      return this.termURI;
   }

   public static Window3DS2 getTest() {
      JsonObject var0 = new JsonObject("{\n\t\"orderId\": \"AIT05632810\",\n\t\"resourceState\": \"c1fe5eb51cefbeb9c8a5b8ff0f3373d9e24cacbc255b2eb55f04229ab6ccbd97\",\n\t\"paymentStatus\": \"not_paid\",\n\t\"status\": \"created\",\n\t\"authorizationType\": \"3ds\",\n\t\"paRedirectForm\": {\n\t\t\"formMethod\": \"POST\",\n\t\t\"formAction\": \"https://authentication.cardinalcommerce.com/ThreeDSecure/V1_0_2/PayerAuthentication?issuerId=5bf2b9086196010e1638af29&transactionId=MQrPWDwBzSDbJq6T4zIFoDpwLyi0https://authentication.cardinalcommerce.com/ThreeDSecure/V1_0_2/PayerAuthentication?issuerId=5bf2b9086196010e1638af29&transactionId=MQrPWDwBzSDbJq6T4zIFoDpwLyi0\",\n\t\t\"formFields\": {\n\t\t\t\"PaReq\": \"eJxUkm9vsjAUxb/KsvdSWlpg5q4JSpa5zMVNn+x1LVchmYAF5p9Pv1ZhPuPV/d0eTm9PC6vcIKZL1J1BCXNsGrXFuyJ7vMe12KBg4Wijw2jEhYpH6yjGEc9Y8BBSoYKM30tYJB+4l/CNpimqUlLP9xiQAa2j0bkqWwlK7yezNyk4jVkApEfYoZmlMop8wX374xWhVDuUKisy1QC5AOiqK1tzkiwKgQwAnfmSedvWzZiQq97T1Q6I6wO57b7oXNVYn2ORyW6biHpe0A863XRU/atj1Pk2cd8jEKeATLUomc+oHzL/jsbjgI65HfDSB7VzA0gaeDyyZ7kS1G6TZFhyK/93wGZssNQnGXPfHmEgwGNdlWgV1v63BnIbefrs4tOtTWb+bhaf6WFyXqbrl3244ufZU5XWh9dTYT17kXMsXFSC2mYPQJwN6e/LRnO5alv9eQI/BcCBAAAAAACQ/2sA3Yitmg==\",\n\t\t\t\"EncodedData\": \"abe6f5a162944e75a5457590a1c4e0ca0aaf5f16db30d71d3553a9ee8efeb2deb5126baf181ae52ff2e8a759cd9c20e0a61feacc58426fa06452fcf467473bb7c787ac7aeb9de5fceadcce941c80a8068237f7d39148d8dcdc2e59eca816790a068ad387e429887b82505137a16d762d968cfa143bdceaa035d0e72d4eb2009abeb8c8dde36d651f24fcf0475b50f842\",\n\t\t\t\"MD\": \"8ac9a4a77a13d11c017a2ab152271bb8\"\n\t\t}\n\t}\n}");
      String var1 = var0.getString("orderId");
      JsonObject var2 = var0.getJsonObject("paRedirectForm");
      String var3 = var2.getString("formMethod", "POST");
      String var4 = var2.getString("formAction");
      JsonObject var5 = var2.getJsonObject("formFields");
      String var6 = var5.getString("EncodedData");
      var5.remove("EncodedData");
      String var7 = "https://www.yeezysupply.com/payment/callback/CREDIT_CARD/6f5e33ab3ccb5d30cbadee8258/adyen?orderId=" + var1 + "&encodedData=" + var6 + "&result=AUTHORISED";
      MultiMap var8 = MultiMap.caseInsensitiveMultiMap();
      Iterator var9 = var5.fieldNames().iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         var8.add(var10, var5.getString(var10));
      }

      var8.add("TermUrl", var7);
      System.out.println(var8);
      Window3DS2 var11 = new Window3DS2("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36", var4, var7, var6, var3, var8);
      return var11;
   }

   public void invoke2() {
      Engine var1 = Engine.newInstance(this.engineOptions());
      var1.on(EngineClosed.class, this::lambda$invoke2$6);
      this.browser = var1.newBrowser();
      this.browser.on(BrowserClosed.class, this::lambda$invoke2$7);
      this.frame = new JFrame("Confirm 3DS");
      this.frame.setDefaultCloseOperation(2);
      this.frame.addWindowListener(new Window3DS2$2(this, var1));
      BrowserView var2 = BrowserView.newInstance(this.browser);
      this.frame.add(var2, "Center");
      this.frame.setSize(360, 550);
      this.frame.setVisible(true);
      this.urlCallbackShopify.thenAcceptAsync(this::lambda$invoke2$8);
      this.browser.navigation().loadUrl(this.loadUrl);
   }

   public void lambda$invoke2$7(BrowserClosed var1) {
      this.urlCallbackShopify.complete((Object)null);
   }

   public boolean lambda$new$1(String var1) {
      return var1.contains(this.termURI);
   }

   public CompletableFuture invoke() {
      CompletableFuture.runAsync(this::invoke0);
      return this.callback;
   }

   public void lambda$invoke0$5(Boolean var1) {
      try {
         this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
      } catch (Throwable var3) {
      }

   }

   public Window3DS2(String var1, String var2, String var3) {
      this.urlCallbackShopify = new ContextCompletableFuture();
      this.userAgent = var1;
      this.termURI = var2;
      this.loadUrl = var3;
      this.termUrlCheck = this::lambda$new$1;
   }

   public boolean lambda$new$0(String var1) {
      return var1.contains("callback/CREDIT_CARD") || var1.equalsIgnoreCase(this.termURI);
   }

   public Window3DS2(String var1, String var2, String var3, String var4, String var5, MultiMap var6) {
      this.callback = new ContextCompletableFuture();
      this.userAgent = var1;
      this.acsURI = var2;
      this.termURI = var3;
      this.encodedData = var4;
      this.acsMethod = var5;
      this.acsForm = var6;
      this.uploadValues = new LinkedHashMap();
      this.termUrlCheck = this::lambda$new$0;
   }

   public EngineOptions engineOptions() {
      return EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, this::lambda$engineOptions$9).userAgent(this.userAgent).build();
   }

   public void lambda$invoke2$6(EngineClosed var1) {
      this.urlCallbackShopify.complete((Object)null);
   }

   public InterceptUrlRequestCallback.Response lambda$engineOptions$9(InterceptUrlRequestCallback.Params var1) {
      if (this.termUrlCheck.test(var1.urlRequest().url())) {
         if (this.urlCallbackShopify != null) {
            this.urlCallbackShopify.complete(var1.urlRequest().url());
         } else {
            this.callback.complete(true);
         }
      }

      return com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback.Response.proceed();
   }

   public void lambda$invoke0$2(EngineClosed var1) {
      this.callback.complete(true);
   }

   public static void main(String[] var0) {
      Window3DS2 var1 = getTest();
      var1.invoke().get();
      System.out.println(var1.uploadValues);
      System.out.println("EXIT");
   }

   public void lambda$invoke0$4(BrowserClosed var1) {
      this.callback.complete(true);
   }
}

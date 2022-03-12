/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.browser.event.BrowserClosed
 *  com.teamdev.jxbrowser.callback.Callback
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.engine.EngineOptions
 *  com.teamdev.jxbrowser.engine.RenderingMode
 *  com.teamdev.jxbrowser.engine.event.EngineClosed
 *  com.teamdev.jxbrowser.net.FormData
 *  com.teamdev.jxbrowser.net.FormData$Pair
 *  com.teamdev.jxbrowser.net.Scheme
 *  com.teamdev.jxbrowser.net.callback.BeforeSendUploadDataCallback
 *  com.teamdev.jxbrowser.net.callback.BeforeSendUploadDataCallback$Params
 *  com.teamdev.jxbrowser.net.callback.BeforeSendUploadDataCallback$Response
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Response
 *  com.teamdev.jxbrowser.view.swing.BrowserView
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.yeezy.util;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.event.BrowserClosed;
import com.teamdev.jxbrowser.callback.Callback;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.engine.event.EngineClosed;
import com.teamdev.jxbrowser.net.FormData;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.callback.BeforeSendUploadDataCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import io.trickle.task.sites.yeezy.util.Window3DS2$1;
import io.trickle.task.sites.yeezy.util.Window3DS2$2;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import java.awt.Component;
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
    public ContextCompletableFuture<String> urlCallbackShopify;
    public String acsMethod;
    public Browser browser;
    public String termURI;
    public String acsURI;
    public Predicate<String> termUrlCheck;
    public String loadUrl;
    public MultiMap acsForm;
    public String userAgent;
    public JFrame frame;
    public String encodedData;
    public ContextCompletableFuture<Boolean> callback;
    public HashMap<String, String> uploadValues;

    public void lambda$invoke0$4(BrowserClosed browserClosed) {
        this.callback.complete((Object)true);
    }

    public void lambda$invoke2$8(String string) {
        try {
            this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public String getTermURI() {
        return this.termURI;
    }

    public void lambda$invoke0$5(Boolean bl) {
        try {
            this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static Window3DS2 getTest() {
        JsonObject jsonObject = new JsonObject("{\n\t\"orderId\": \"AIT05632810\",\n\t\"resourceState\": \"c1fe5eb51cefbeb9c8a5b8ff0f3373d9e24cacbc255b2eb55f04229ab6ccbd97\",\n\t\"paymentStatus\": \"not_paid\",\n\t\"status\": \"created\",\n\t\"authorizationType\": \"3ds\",\n\t\"paRedirectForm\": {\n\t\t\"formMethod\": \"POST\",\n\t\t\"formAction\": \"https://authentication.cardinalcommerce.com/ThreeDSecure/V1_0_2/PayerAuthentication?issuerId=5bf2b9086196010e1638af29&transactionId=MQrPWDwBzSDbJq6T4zIFoDpwLyi0https://authentication.cardinalcommerce.com/ThreeDSecure/V1_0_2/PayerAuthentication?issuerId=5bf2b9086196010e1638af29&transactionId=MQrPWDwBzSDbJq6T4zIFoDpwLyi0\",\n\t\t\"formFields\": {\n\t\t\t\"PaReq\": \"eJxUkm9vsjAUxb/KsvdSWlpg5q4JSpa5zMVNn+x1LVchmYAF5p9Pv1ZhPuPV/d0eTm9PC6vcIKZL1J1BCXNsGrXFuyJ7vMe12KBg4Wijw2jEhYpH6yjGEc9Y8BBSoYKM30tYJB+4l/CNpimqUlLP9xiQAa2j0bkqWwlK7yezNyk4jVkApEfYoZmlMop8wX374xWhVDuUKisy1QC5AOiqK1tzkiwKgQwAnfmSedvWzZiQq97T1Q6I6wO57b7oXNVYn2ORyW6biHpe0A863XRU/atj1Pk2cd8jEKeATLUomc+oHzL/jsbjgI65HfDSB7VzA0gaeDyyZ7kS1G6TZFhyK/93wGZssNQnGXPfHmEgwGNdlWgV1v63BnIbefrs4tOtTWb+bhaf6WFyXqbrl3244ufZU5XWh9dTYT17kXMsXFSC2mYPQJwN6e/LRnO5alv9eQI/BcCBAAAAAACQ/2sA3Yitmg==\",\n\t\t\t\"EncodedData\": \"abe6f5a162944e75a5457590a1c4e0ca0aaf5f16db30d71d3553a9ee8efeb2deb5126baf181ae52ff2e8a759cd9c20e0a61feacc58426fa06452fcf467473bb7c787ac7aeb9de5fceadcce941c80a8068237f7d39148d8dcdc2e59eca816790a068ad387e429887b82505137a16d762d968cfa143bdceaa035d0e72d4eb2009abeb8c8dde36d651f24fcf0475b50f842\",\n\t\t\t\"MD\": \"8ac9a4a77a13d11c017a2ab152271bb8\"\n\t\t}\n\t}\n}");
        String string = jsonObject.getString("orderId");
        JsonObject jsonObject2 = jsonObject.getJsonObject("paRedirectForm");
        String string2 = jsonObject2.getString("formMethod", "POST");
        String string3 = jsonObject2.getString("formAction");
        JsonObject jsonObject3 = jsonObject2.getJsonObject("formFields");
        String string4 = jsonObject3.getString("EncodedData");
        jsonObject3.remove("EncodedData");
        String string5 = "https://www.yeezysupply.com/payment/callback/CREDIT_CARD/6f5e33ab3ccb5d30cbadee8258/adyen?orderId=" + string + "&encodedData=" + string4 + "&result=AUTHORISED";
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        Iterator iterator = jsonObject3.fieldNames().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                multiMap.add("TermUrl", string5);
                System.out.println(multiMap);
                return new Window3DS2("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36", string3, string5, string4, string2, multiMap);
            }
            String string6 = (String)iterator.next();
            multiMap.add(string6, jsonObject3.getString(string6));
        }
    }

    public CompletableFuture invoke() {
        CompletableFuture.runAsync(this::invoke0);
        return this.callback;
    }

    public InterceptUrlRequestCallback.Response lambda$engineOptions$9(InterceptUrlRequestCallback.Params params) {
        if (!this.termUrlCheck.test(params.urlRequest().url())) return InterceptUrlRequestCallback.Response.proceed();
        if (this.urlCallbackShopify != null) {
            this.urlCallbackShopify.complete((Object)params.urlRequest().url());
            return InterceptUrlRequestCallback.Response.proceed();
        }
        this.callback.complete((Object)true);
        return InterceptUrlRequestCallback.Response.proceed();
    }

    public static void main(String[] stringArray) {
        Window3DS2 window3DS2 = Window3DS2.getTest();
        window3DS2.invoke().get();
        System.out.println(window3DS2.uploadValues);
        System.out.println("EXIT");
    }

    public void invoke0() {
        Engine engine = Engine.newInstance((EngineOptions)this.engineOptions());
        engine.on(EngineClosed.class, this::lambda$invoke0$2);
        engine.network().set(BeforeSendUploadDataCallback.class, (Callback)((BeforeSendUploadDataCallback)this::lambda$invoke0$3));
        this.browser = engine.newBrowser();
        this.browser.on(BrowserClosed.class, this::lambda$invoke0$4);
        this.frame = new JFrame("Confirm 3DS");
        this.frame.setDefaultCloseOperation(2);
        this.frame.addWindowListener(new Window3DS2$1(this, engine));
        BrowserView browserView = BrowserView.newInstance((Browser)this.browser);
        this.frame.add((Component)browserView, "Center");
        this.frame.setSize(360, 550);
        this.frame.setVisible(true);
        this.callback.thenAcceptAsync(this::lambda$invoke0$5);
        String string = Window3DS2.getFormPage(this.acsMethod, this.acsURI, this.acsForm);
        String string2 = Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
        String string3 = "data:text/html;base64," + string2;
        this.browser.navigation().loadUrl(string3);
    }

    public EngineOptions engineOptions() {
        return EngineOptions.newBuilder((RenderingMode)RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, this::lambda$engineOptions$9).userAgent(this.userAgent).build();
    }

    public void lambda$invoke2$7(BrowserClosed browserClosed) {
        this.urlCallbackShopify.complete(null);
    }

    public void invoke2() {
        Engine engine = Engine.newInstance((EngineOptions)this.engineOptions());
        engine.on(EngineClosed.class, this::lambda$invoke2$6);
        this.browser = engine.newBrowser();
        this.browser.on(BrowserClosed.class, this::lambda$invoke2$7);
        this.frame = new JFrame("Confirm 3DS");
        this.frame.setDefaultCloseOperation(2);
        this.frame.addWindowListener(new Window3DS2$2(this, engine));
        BrowserView browserView = BrowserView.newInstance((Browser)this.browser);
        this.frame.add((Component)browserView, "Center");
        this.frame.setSize(360, 550);
        this.frame.setVisible(true);
        this.urlCallbackShopify.thenAcceptAsync(this::lambda$invoke2$8);
        this.browser.navigation().loadUrl(this.loadUrl);
    }

    public CompletableFuture invokeShopify() {
        CompletableFuture.runAsync(this::invoke2);
        return this.urlCallbackShopify;
    }

    public HashMap getUploadValues() {
        return this.uploadValues;
    }

    public BeforeSendUploadDataCallback.Response lambda$invoke0$3(BeforeSendUploadDataCallback.Params params) {
        if (!this.termUrlCheck.test(params.urlRequest().url())) return BeforeSendUploadDataCallback.Response.proceed();
        if (!(params.uploadData() instanceof FormData)) return BeforeSendUploadDataCallback.Response.proceed();
        FormData formData = (FormData)params.uploadData();
        Iterator iterator = formData.data().iterator();
        while (iterator.hasNext()) {
            FormData.Pair pair = (FormData.Pair)iterator.next();
            this.uploadValues.put(pair.key(), pair.value());
        }
        return BeforeSendUploadDataCallback.Response.proceed();
    }

    public boolean lambda$new$1(String string) {
        return string.contains(this.termURI);
    }

    public static String getFormPage(String string, String string2, MultiMap multiMap) {
        return "        <html>\n            <body>\n                <form method=\"" + string + "\" action=\"" + string2 + "\" id=\"Cardinal-CCA-Form\">\n                    <input type=\"hidden\" name=\"PaReq\" value=\"" + multiMap.get("PaReq") + "\" />\n                    <input type=\"hidden\" name=\"MD\" value=\"" + multiMap.get("MD") + "\" />\n                    <input type=\"hidden\" name=\"TermUrl\" value=\"" + multiMap.get("TermUrl") + "\" />\n                </form>\n                <script>\n                    setTimeout(() => document.querySelector('#Cardinal-CCA-Form').submit(), 500);\n                </script>\n            </body>\n        </html>";
    }

    public String getEncodedData() {
        return this.encodedData;
    }

    public Window3DS2(String string, String string2, String string3) {
        this.urlCallbackShopify = new ContextCompletableFuture();
        this.userAgent = string;
        this.termURI = string2;
        this.loadUrl = string3;
        this.termUrlCheck = this::lambda$new$1;
    }

    public boolean lambda$new$0(String string) {
        if (string.contains("callback/CREDIT_CARD")) return true;
        if (string.equalsIgnoreCase(this.termURI)) return true;
        return false;
    }

    public void lambda$invoke0$2(EngineClosed engineClosed) {
        this.callback.complete((Object)true);
    }

    public Window3DS2(String string, String string2, String string3, String string4, String string5, MultiMap multiMap) {
        this.callback = new ContextCompletableFuture();
        this.userAgent = string;
        this.acsURI = string2;
        this.termURI = string3;
        this.encodedData = string4;
        this.acsMethod = string5;
        this.acsForm = multiMap;
        this.uploadValues = new LinkedHashMap<String, String>();
        this.termUrlCheck = this::lambda$new$0;
    }

    public void lambda$invoke2$6(EngineClosed engineClosed) {
        this.urlCallbackShopify.complete(null);
    }
}


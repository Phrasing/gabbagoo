package io.trickle.harvester;

import io.trickle.task.sites.shopify.Shopify;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaptchaToken {
   public String proxyStr;
   public long creationTime;
   public String domain;
   public CookieJar cookieJar;
   public Shopify shopify;
   public boolean isHcaptcha = false;
   public static Pattern HCAP_REPLACE_PATTERN = Pattern.compile("else if \\(hcaptchaElement && !hcaptchaElement.value\\).*?}", 32);
   public String token;
   public static Pattern CAP_PATTERN = Pattern.compile("<div class=\"checkpoint__wrapper content.*?</form>.*?</div>.*?</div>", 32);
   public RealClient client;
   public boolean used = false;
   public boolean checkpoint;
   public String html;

   public String getHtml() {
      return this.html;
   }

   public void setTokenValues(String var1) {
      this.token = var1;
   }

   public boolean isUsed() {
      return this.used;
   }

   public String modifyHcaptchaCallback(String var1) {
      Matcher var2 = HCAP_REPLACE_PATTERN.matcher(var1);
      if (var2.find()) {
         this.isHcaptcha = true;
         return var2.replaceAll(var2.group(0) + " else {\n                                            window.dispatchEvent(new Event('captchaSuccess', {bubbles: true, cancelable: true}));\n                                        }");
      } else {
         return var1;
      }
   }

   public String getDomain() {
      return this.domain;
   }

   public CaptchaToken(String var1, boolean var2, String var3, String var4, Shopify var5) {
      this.domain = var1;
      this.checkpoint = var2;
      this.token = null;
      this.proxyStr = var3;
      this.html = this.v2Html(var4);
      this.creationTime = System.currentTimeMillis();
      this.shopify = var5;
      if (var5 != null) {
         this.client = var5.api.getWebClient();
         this.cookieJar = var5.api.getCookies();
      }

   }

   public CookieJar getCookieJar() {
      return this.cookieJar;
   }

   public String getToken() {
      return this.token;
   }

   public boolean isCheckpoint() {
      return this.checkpoint;
   }

   public void expire() {
      this.used = true;
   }

   public String getProxyStr() {
      return this.proxyStr;
   }

   public String v2Html(String var1) {
      if (var1 == null) {
         return var1;
      } else {
         var1 = var1.replace("Captchaを解決して続行します", "Solve Captcha");
         var1 = var1.replaceAll("(?s)<button type=\"submit\" class=\"btn\">.*?</button>", "");
         var1 = var1.replace("recaptcha.net", "google.com");
         Matcher var2 = CAP_PATTERN.matcher(var1);
         if (var2.find()) {
            String var3 = var2.group(0);
            var3 = this.modifyHcaptchaCallback(var3);
            return "<!doctype html>\n<html class=\"no-js\" lang=\"en\">\n" + var3 + "\n<script>\nconst doesTokenExist = () => {\n    if((document.querySelector('[name=\"g-recaptcha-response\"]') && document.querySelector('[name=\"g-recaptcha-response\"]').value && document.querySelector('[name=\"g-recaptcha-response\"]').value.length > 0) || (document.querySelector('[name=\"h-captcha-response\"]') && document.querySelector('[name=\"h-captcha-response\"]').value && document.querySelector('[name=\"h-captcha-response\"]').value.length > 0)) {\n        window.completion.completed(JSON.stringify(Object.fromEntries(Array.from(new FormData(document.querySelector('form[action=\"/checkpoint\"]'))))));\n        return true;\n    }\n\n    return false;\n};\n\nnew Promise(resolve => {\n    window.addEventListener('captchaSuccess', () => {\n        const isValid = doesTokenExist();\n        if(!isValid) {\n            const intervalVal = setInterval(() => {\n                const isValid = doesTokenExist();\n                if(isValid) clearInterval(intervalVal);\n            }, 50);\n        }\n        setTimeout(() => document.querySelector(\"html\").innerHTML = `<h2>Waiting for captcha</h2>`, 25000);\n    }, false);\n});\nconst grecp = document.getElementById(\"g-recaptcha\")\nconst hcrap = document.getElementById(\"h-captcha\")\nconst observer = new ResizeObserver(entries => {\n    var boundRect = grecp.getBoundingClientRect();\n    if (boundRect.height > 0)\n        console.log(JSON.stringify(boundRect));\n})\nconst hObserver = new ResizeObserver(entries => {\n    var boundRect = hcrap.getBoundingClientRect();\n    if (boundRect.height > 0)\n        console.log(JSON.stringify(boundRect));\n})\nif (!!grecp)\n    observer.observe(grecp);\nelse if (!!hcrap)\n    hObserver.observe(hcrap);\n</script>\n</html>";
         } else {
            return var1;
         }
      }
   }

   public CaptchaToken(String var1) {
      this.domain = var1;
      this.checkpoint = false;
      this.token = null;
   }
}

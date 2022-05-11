/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.webclient.CookieJar
 *  io.trickle.webclient.RealClient
 */
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
    public static Pattern HCAP_REPLACE_PATTERN;
    public String token;
    public static Pattern CAP_PATTERN;
    public RealClient client;
    public boolean used = false;
    public boolean checkpoint;
    public String html;

    public String getHtml() {
        return this.html;
    }

    public void setTokenValues(String string) {
        this.token = string;
    }

    public boolean isUsed() {
        return this.used;
    }

    static {
        CAP_PATTERN = Pattern.compile("<div class=\"checkpoint__wrapper content.*?</form>.*?</div>.*?</div>", 32);
        HCAP_REPLACE_PATTERN = Pattern.compile("else if \\(hcaptchaElement && !hcaptchaElement.value\\).*?}", 32);
    }

    public String modifyHcaptchaCallback(String string) {
        Matcher matcher = HCAP_REPLACE_PATTERN.matcher(string);
        if (!matcher.find()) return string;
        this.isHcaptcha = true;
        return matcher.replaceAll(matcher.group(0) + " else {\n                                            window.dispatchEvent(new Event('captchaSuccess', {bubbles: true, cancelable: true}));\n                                        }");
    }

    public String getDomain() {
        return this.domain;
    }

    public CaptchaToken(String string, boolean bl, String string2, String string3, Shopify shopify) {
        this.domain = string;
        this.checkpoint = bl;
        this.token = null;
        this.proxyStr = string2;
        this.html = this.v2Html(string3);
        this.creationTime = System.currentTimeMillis();
        this.shopify = shopify;
        if (shopify == null) return;
        this.client = shopify.api.getWebClient();
        this.cookieJar = shopify.api.getCookies();
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

    public String v2Html(String string) {
        if (string == null) {
            return string;
        }
        string = string.replace("Captcha\u3092\u89e3\u6c7a\u3057\u3066\u7d9a\u884c\u3057\u307e\u3059", "Solve Captcha");
        string = string.replaceAll("(?s)<button type=\"submit\" class=\"btn\">.*?</button>", "");
        Matcher matcher = CAP_PATTERN.matcher(string = string.replace("recaptcha.net", "google.com"));
        if (!matcher.find()) return string;
        String string2 = matcher.group(0);
        string2 = this.modifyHcaptchaCallback(string2);
        return "<!doctype html>\n<html class=\"no-js\" lang=\"en\">\n" + string2 + "\n<script>\nconst doesTokenExist = () => {\n    if((document.querySelector('[name=\"g-recaptcha-response\"]') && document.querySelector('[name=\"g-recaptcha-response\"]').value && document.querySelector('[name=\"g-recaptcha-response\"]').value.length > 0) || (document.querySelector('[name=\"h-captcha-response\"]') && document.querySelector('[name=\"h-captcha-response\"]').value && document.querySelector('[name=\"h-captcha-response\"]').value.length > 0)) {\n        window.completion.completed(JSON.stringify(Object.fromEntries(Array.from(new FormData(document.querySelector('form[action=\"/checkpoint\"]'))))));\n        return true;\n    }\n\n    return false;\n};\n\nnew Promise(resolve => {\n    window.addEventListener('captchaSuccess', () => {\n        const isValid = doesTokenExist();\n        if(!isValid) {\n            const intervalVal = setInterval(() => {\n                const isValid = doesTokenExist();\n                if(isValid) clearInterval(intervalVal);\n            }, 50);\n        }\n        setTimeout(() => document.querySelector(\"html\").innerHTML = `<h2>Waiting for captcha</h2>`, 25000);\n    }, false);\n});\nconst grecp = document.getElementById(\"g-recaptcha\")\nconst hcrap = document.getElementById(\"h-captcha\")\nconst observer = new ResizeObserver(entries => {\n    var boundRect = grecp.getBoundingClientRect();\n    if (boundRect.height > 0)\n        console.log(JSON.stringify(boundRect));\n})\nconst hObserver = new ResizeObserver(entries => {\n    var boundRect = hcrap.getBoundingClientRect();\n    if (boundRect.height > 0)\n        console.log(JSON.stringify(boundRect));\n})\nif (!!grecp)\n    observer.observe(grecp);\nelse if (!!hcrap)\n    hObserver.observe(hcrap);\n</script>\n</html>";
    }

    public CaptchaToken(String string) {
        this.domain = string;
        this.checkpoint = false;
        this.token = null;
    }
}

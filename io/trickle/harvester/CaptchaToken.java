/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 */
package io.trickle.harvester;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.task.antibot.impl.px.tools.Deobfuscator;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaptchaToken {
    public boolean checkpoint;
    public RealClient client;
    public static Pattern CAP_PATTERN = Pattern.compile("<div class=\"checkpoint__wrapper content.*?</form>.*?</div>.*?</div>", 32);
    public String token;
    public String proxyStr;
    public boolean used = false;
    public static Pattern HCAP_REPLACE_PATTERN = Pattern.compile("else if \\(hcaptchaElement && !hcaptchaElement.value\\).*?}", 32);
    public CookieJar cookieJar;
    public String domain;
    public String html;
    public String originalHtml;
    public Iterable<Cookie> cookies;

    public static String v2Html(String string) {
        if (string == null) {
            return string;
        }
        string = string.replace("Captcha\u3092\u89e3\u6c7a\u3057\u3066\u7d9a\u884c\u3057\u307e\u3059", "Solve Captcha");
        string = string.replaceAll("(?s)<button type=\"submit\" class=\"btn\">.*?</button>", "");
        Matcher matcher = CAP_PATTERN.matcher(string = string.replace("hl=ja", "hl=en"));
        if (!matcher.find()) return string;
        String string2 = matcher.group(0);
        string2 = CaptchaToken.modifyHcaptchaCallback(string2);
        return "<!doctype html>\n<html class=\"no-js\" lang=\"en\">\n" + string2 + "\n<script>\nconst doesTokenExist = () => {\n    if((document.querySelector('[name=\"g-recaptcha-response\"]') && document.querySelector('[name=\"g-recaptcha-response\"]').value && document.querySelector('[name=\"g-recaptcha-response\"]').value.length > 0) || (document.querySelector('[name=\"h-captcha-response\"]') && document.querySelector('[name=\"h-captcha-response\"]').value && document.querySelector('[name=\"h-captcha-response\"]').value.length > 0)) {\n        window.completion.completed(JSON.stringify(Object.fromEntries(Array.from(new FormData(document.querySelector('form[action=\"/checkpoint\"]'))))));\n        return true;\n    }\n\n    return false;\n};\n\nnew Promise(resolve => {\n    window.addEventListener('captchaSuccess', () => {\n        const isValid = doesTokenExist();\n        if(!isValid) {\n            const intervalVal = setInterval(() => {\n                const isValid = doesTokenExist();\n                if(isValid) clearInterval(intervalVal);\n            }, 50);\n        }\n        setTimeout(() => document.querySelector(\"html\").innerHTML = `<h2>Waiting for captcha</h2>`, 25000);\n    }, false);\n});\nconst grecp = document.getElementById(\"g-recaptcha\")\nconst observer = new ResizeObserver(entries => {\n  var boundRect = grecp.getBoundingClientRect();  \n  if (boundRect.height > 0)  \n      console.log(JSON.stringify(boundRect));\n})\nobserver.observe(grecp);\n</script>\n</html>";
    }

    public CaptchaToken(String string) {
        this.domain = string;
        this.checkpoint = false;
        this.token = null;
    }

    public String getToken() {
        return this.token;
    }

    public void setTokenValues(String string) {
        this.token = string;
    }

    public String getProxyStr() {
        return this.proxyStr;
    }

    public void expire() {
        this.used = true;
    }

    public boolean isUsed() {
        return this.used;
    }

    public static void main(String[] stringArray) {
        System.out.println(CaptchaToken.v2Html(Deobfuscator.readJsFile("hicc.html")));
    }

    public static String modifyHcaptchaCallback(String string) {
        Matcher matcher = HCAP_REPLACE_PATTERN.matcher(string);
        if (!matcher.find()) return string;
        return matcher.replaceAll(matcher.group(0) + " else {\n                                            window.dispatchEvent(new Event('captchaSuccess', {bubbles: true, cancelable: true}));\n                                        }");
    }

    public Iterable getCookies() {
        return this.cookies;
    }

    public String getHtml() {
        return this.html;
    }

    public CaptchaToken(String string, boolean bl, Iterable iterable, String string2, CookieJar cookieJar, String string3, RealClient realClient) {
        this.domain = string;
        this.checkpoint = bl;
        this.token = null;
        this.cookies = iterable;
        this.proxyStr = string2;
        this.cookieJar = cookieJar;
        this.originalHtml = string3;
        this.html = CaptchaToken.v2Html(this.originalHtml);
        this.client = realClient;
    }

    public CookieJar getCookieJar() {
        return this.cookieJar;
    }

    public String getDomain() {
        return this.domain;
    }

    public boolean isCheckpoint() {
        return this.checkpoint;
    }
}


/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.akamai.pixel.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deobfuscator {
    public static Pattern WINDOW_REFERENCE_PATTERN;
    public static Pattern _STRINGS;
    public static Pattern NAVIGATOR_REFERENCE_PATTERN;
    public static String[] underscore;
    public static Pattern DOCUMENT_REFERENCE_PATTERN;
    public static Pattern SCREEN_REFERENCE_PATTERN;

    public static String readJsFile() {
        String string = null;
        try (FileInputStream fileInputStream = new FileInputStream("/Users/tricklebot/Documents/GitHub/TrickleV1.0/src/main/java/io/trickle/task/antibot/impl/akamai/pixel/tools/pixel.js");){
            string = new String(((InputStream)fileInputStream).readAllBytes());
            return string;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return string;
    }

    public static String deobV2(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.group(1).length() <= 0) continue;
            try {
                String string2 = Deobfuscator.cleanUnderscores(Integer.parseInt(matcher.group(1)));
                string2 = string2.replace("\"", "mehai");
                string2 = string2.replace("\\", "franco");
                String string3 = string2.replace("franco", "\\\\");
                string3 = string3.replace("mehai", "\\\"");
                string = string.replace(matcher.group(0), "\"" + string3 + "\"");
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed on: " + matcher.group(1));
            }
        }
        return string;
    }

    public static String deobWindowReference(String string, Pattern pattern, String string2) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.group(1).length() <= 0) continue;
            try {
                string = string.replace(matcher.group(0), matcher.group(0).replace(matcher.group(1), "window." + string2));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed on: " + matcher.group(1));
            }
        }
        return string;
    }

    public static String cleanUnderscores(int n) {
        return underscore[n];
    }

    public static String deobWindows(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.group(1).length() <= 0) continue;
            try {
                string = string.replace(matcher.group(0), matcher.group(0).replace(matcher.group(1), "window"));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed on: " + matcher.group(1));
            }
        }
        return string;
    }

    static {
        underscore = new String[]{"languages", "cookieEnabled", "Andale Mono", "a", "screenY", "detachEvent", "permissions", "\\\\\\\\", "Content-Type", "contextMenu", "dp", "persistent-storage", "Palatino Linotype", "images", ",", "open", "name", "selenium", "JSON", "ShockwaveFlash.ShockwaveFlash.6", "prototype", "ieps", "Edge", "save", "Sinhala Sangam MN", "San Francisco UI", "#E0E0E0", "Leelawadee UI", "XPathResult", "Gadugi", "Academy Engraved LET", "ActiveXObject", "getTimezoneOffset", "PDF.PdfCtrl.1", "sessionStorage", "querySelector", "Chalkboard", "removeChild", "z", "data", ".", "Yu Gothic UI Light", "clipboard-write", "POST", "substr", "function", "state", "device-info", "split", "getAttribute", "outerWidth", "getImageData", "Corbel", "location", "4", "Chrome IOS", "mimeTypes", "innerWidth", "length", "screenX", "2d", "1.3", "removeAttribute", "language", "FreeMono", "img", "rgba(255,153,153, 0.5)", "1.2", "[", "fillStyle", "fsfp", "Nimbus Roman No 9 L", "[object Array]", "utf8Decode", "drawImage", "__akfp_storage_test__", "David", "document", "1.4", "opera", "hash", "availWidth", "font", "IsVersionSupported", "accelerometer", "toDataURL", "onload", "Trattatello", "replace", "\"", "screen", "f", "514", "f024ea41ae1d5b46a7553040b60ca4be", "hostname", "MONO", "removeEventListener", "appendChild", "createPopup", "1.5", "documentElement", "Luxi", "map", "#FF3333", "oXMLStore", "1.7", "Ubuntu", "denied", "encodeURIComponent", "toString", "appCodeName", "match", "Segoe UI", "Nimbus Mono L", "position: relative; left: -9999px; visibility: hidden; display: block !important", "text", "attachEvent", "u", "Opera", "cssText", "src", "canvas", "charAt", "7", "outerHeight", "port", "1.0", "Franklin Gothic", "Int8Array", "]", "AgControl.AgControl", "script", ":", "msDoNotTrack", "send", "/akam/11/pixel_", ", ", "fillText", "3.0", "profile", "documentMode", "vendorSub", "setRequestHeader", "Al Nile", "true1", "div", "18pt Tahoma", "br", "AcroPDF.PDF.1", "\\\\r", "now", "HTMLElement", "dispatchEvent", "timing", "boolean", "nap", "doNotTrack", "vendor", "top", "availHeight", "Nimbus Sans L", "maxTouchPoints", "innerHTML", "buildID", "Utsaah", "!H71JCaj)]# 1@#", "pow", "XDomainRequest", "pageYOffset", "Leelawadee UI Bold", "\\\\f", "#0000FF", "Shockwave Flash", "firstChild", "plugins", "StyleMedia", "1.9", "childNodes", "MutationObserver", "Vijaya", "San Francisco Mono", "ShockwaveFlash.ShockwaveFlash", "Liberation Mono", "fp", "notifications", "style", "Monotype LingWai Medium", "crc", "URW Gothic L", "PDF.PdfCtrl.", "\\\\u", "chrome", "Firefox", "American Typewriter", "'btoa' failed: The string to be encoded contains characters outside of the Latin1 range.", "\\\t", "product", "Safari", "application/x-www-form-urlencoded", "message", "getContext", "speaker", "MV Boli", "Soft Ruddy Foothold 2", "indexOf", "URW Chancery L", "origin", "width", "floor", "4.0", "+", "Apple Color Emoji", "createElement", "sans-serif", "FreeSerif", "Segoe Pseudo", "postMessage", "Microsoft Sans Serif", "fireEvent", "body", "MS Gothic", "insertBefore", "indexedDB", "\\\n", "nav", "PingFang", "GetVariable", "pageXOffset", "Segoe UI Light", "Bitstream Vera Sans", "object", "magnetometer", "RYelrZVIUa", "openDatabase", " OPR/", "Cambria", "Party LET", "bt", "textBaseline", "1.1", "//", "Shockwave Flash 2.0", "\\\\\"", "$version", "background-sync", "hardwareConcurrency", "filename", "colorDepth", "Bahnschrift", "sp", "userAgent", "concat", "ps", "Oriya Sangam MN", "av", "jsv", "toHexStr", "Bitstream Charter", "getElementsByClassName", "=\"", "main", "clipboard", "t", "Nirmala UI", "Khmer UI", "userLanguage", "Silverlight Plug-In", "setTimeout", "fc", "microphone", "/*@cc_on!@*/false", "parseInt", "prompt", "enabledPlugin", "Roboto", "{", "sr", "valueOf", "application/x-shockwave-flash", "utf8Encode", "815b5b47c74851ad8094bb807516616c", "", "bazadebezolkohpepadr", "getElementsByTagName", "query", "Shonar Bangla", "midi", "head", "monospace", "Skia", "camera", "Muna", "addEventListener", "\\\\b", "ROTL", "pixelDepth", "charCodeAt", "systemLanguage", "fillRect", "type", "driver", "2.0.0.11", "}", "payment-handler", "appName", "Sequentum", "catch", "stringify", "0", "bp", "accessibility-events", "vibrate", "appVersion", "is not a valid enum value of type PermissionName", "bluetooth", "granted", " !important\">wi wi</b>", "Liberation Sans", "fonts", "toJSON", "webdriver", "protocol", "number", "</div>", "hasOwnProperty", "lt", "push", "ceil", "Sitka Subheading Italic", "ambient-light-sensor", "Microsoft.XMLHTTP", "ShockwaveFlash.ShockwaveFlash.7", "Aparajita", "btoa", "description", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAACGFjVEwAAAABAAAAAcMq2TYAAAANSURBVAiZY2BgYPgPAAEEAQB9ssjfAAAAGmZjVEwAAAAAAAAAAQAAAAEAAAAAAAAAAAD6A+gBAbNU+2sAAAARZmRBVAAAAAEImWNgYGBgAAAABQAB6MzFdgAAAABJRU5ErkJggg==", "<div>", "fromCharCode", "Constructor", "url(#default#userData)", "Image", "pop", "pushNotification", "isArray", "JavaScript", "clientWidth", "ap", "XMLHttpRequest", "fh", "localStorage", "HTMLMenuItemElement", "exitEarly", "platform", ": ", "Malgun Gothic", "sort", "productSub", "Noto", "Infinity", "DB LCD Temp", "-not-existent", "null", "FreeSans", "urhehlevkedkilrobacf", "join", "external", "DejaVu Sans", "removeItem", "clientHeight", "offsetWidth", "URW Bookman L", "[object SafariRemoteNotification]", "retry", "load", "=", "compatMode", "Sitka Banner", "setAttribute", "navigator", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", "Liberation Serif", "all", "Century Schoolbook L", "2992f96a1fb65726bf972d6c1ea672ac", "[object Object]", "compute", "Chrome", "IE", "addBehavior", "1.8", "gyroscope", "globalStorage", "suffixes", "DOMContentLoaded", "setItem", "responseStart", "ApplePaySession", "innerHeight", "undefined", "cv", "performance", "6.0.21", "behavior", "1.6", "<b style=\"position: absolute; display:inline !important; width:auto !important; font:normal 10px/1 ", "Segoe UI Emoji", "appMinorVersion", "oscpu", "2.0", "5.0", "Rockwell", "Nimbus Mono", "geolocation", "&", "getBattery", "Date", "clipboard-read", "call", "runtime", "height", "safari", "CriOS", "zh", "then"};
        _STRINGS = Pattern.compile("[_]\\[([0-9]*?)]");
        WINDOW_REFERENCE_PATTERN = Pattern.compile("(f?)\\[\"");
        DOCUMENT_REFERENCE_PATTERN = Pattern.compile("(u?)\\[\"");
        NAVIGATOR_REFERENCE_PATTERN = Pattern.compile("(d?)\\[\"");
        SCREEN_REFERENCE_PATTERN = Pattern.compile("(b?)\\[\"");
    }

    public static void main(String[] stringArray) {
        String string = Deobfuscator.readJsFile();
        string = Deobfuscator.deobV2(string, _STRINGS);
        string = Deobfuscator.deobWindows(string, WINDOW_REFERENCE_PATTERN);
        string = Deobfuscator.deobWindowReference(string, DOCUMENT_REFERENCE_PATTERN, "document");
        string = Deobfuscator.deobWindowReference(string, NAVIGATOR_REFERENCE_PATTERN, "navigator");
        string = Deobfuscator.deobWindowReference(string, SCREEN_REFERENCE_PATTERN, "screen");
        System.out.println(string);
    }
}


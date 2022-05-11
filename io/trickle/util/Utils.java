/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.aayushatharva.brotli4j.Brotli4jLoader
 *  com.aayushatharva.brotli4j.decoder.Decoder
 *  com.aayushatharva.brotli4j.decoder.DecoderJNI$Status
 *  com.aayushatharva.brotli4j.decoder.DirectDecompress
 *  com.aayushatharva.brotli4j.encoder.Encoder
 *  io.netty.handler.codec.http.HttpHeaderNames
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.netty.util.AsciiString
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.spi.CookieStore
 *  org.bouncycastle.util.encoders.Hex
 */
package io.trickle.util;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoder;
import com.aayushatharva.brotli4j.decoder.DecoderJNI;
import com.aayushatharva.brotli4j.decoder.DirectDecompress;
import com.aayushatharva.brotli4j.encoder.Encoder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AsciiString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.spi.CookieStore;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bouncycastle.util.encoders.Hex;

public class Utils {
    public static Pattern VER_PATTERN;
    public static DateTimeFormatter ISO_8901_US;
    public static CharSequence[] headers;
    public static DateTimeFormatter ISO_8901_JS;

    public static String leftpad(String string, int n) {
        return String.format("%" + n + "." + n + "s", string);
    }

    public static boolean containsAllWords(String string, String ... stringArray) {
        if (string == null) {
            return false;
        }
        if (stringArray.length == 0) {
            return true;
        }
        String[] stringArray2 = stringArray;
        int n = stringArray2.length;
        int n2 = 0;
        while (n2 < n) {
            String string2 = stringArray2[n2];
            if (!Utils.containsIgnoreCase(string, string2)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public static double round(double d, int n) {
        double d2 = Math.pow(Double.longBitsToDouble(0x4024000000000000L), n);
        return (double)Math.round(d * d2) / d2;
    }

    public static boolean hasPattern(String string, Pattern pattern) {
        return pattern.matcher(string).find();
    }

    public static String getRandomString() {
        int n = ThreadLocalRandom.current().nextInt(1, 32);
        return Utils.getString(n);
    }

    public static String parseSafe(String string, String string2, Supplier supplier) {
        try {
            JsonObject jsonObject = new JsonObject(string);
            if (jsonObject.containsKey(string2)) {
                return jsonObject.getString(string2, (String)supplier.get());
            }
        }
        finally {
            return (String)supplier.get();
        }
    }

    public static String getRandomHeader() {
        return headers[ThreadLocalRandom.current().nextInt(headers.length)].toString();
    }

    public static String readFileAsString(String string) {
        return new String(Files.readAllBytes(Paths.get(string, new String[0])));
    }

    public static String getString(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < n) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                stringBuilder.append(Utils.getRandomNumber());
            } else {
                stringBuilder.append(Utils.getRandomChar());
            }
            ++n2;
        }
        return stringBuilder.toString();
    }

    public static String getMacAddress() {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            if (networkInterface == null) {
                return "NaN";
            }
            byte[] byArray = networkInterface.getHardwareAddress();
            if (byArray == null) {
                return "NaN";
            }
            StringBuilder stringBuilder = new StringBuilder(18);
            byte[] byArray2 = byArray;
            int n = byArray2.length;
            int n2 = 0;
            while (n2 < n) {
                byte by = byArray2[n2];
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(':');
                }
                stringBuilder.append(String.format("%02x", by));
                ++n2;
            }
            return stringBuilder.toString();
        }
        catch (Throwable throwable) {
            return "NaN";
        }
    }

    public static String getLocalAddress() {
        try {
            return InetAddress.getLocalHost().toString();
        }
        catch (UnknownHostException unknownHostException) {
            return "NaN";
        }
    }

    public static List quickParseAll(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        ArrayList<String> arrayList = null;
        while (matcher.find()) {
            if (arrayList == null) {
                arrayList = new ArrayList<String>();
            }
            arrayList.add(matcher.group(1));
        }
        return arrayList;
    }

    public static String fixedLenString(String string, int n) {
        return String.format("%1$" + n + "s", string);
    }

    public static boolean containsAnyWords(String string, String ... stringArray) {
        if (string == null) {
            return false;
        }
        if (stringArray.length == 0) {
            return true;
        }
        String[] stringArray2 = stringArray;
        int n = stringArray2.length;
        int n2 = 0;
        while (n2 < n) {
            String string2 = stringArray2[n2];
            if (Utils.containsIgnoreCase(string, string2)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static String rightpad(String string, int n) {
        return String.format("%-" + n + "." + n + "s", string);
    }

    public static boolean containsIgnoreCase(String string, String string2) {
        if (string == null) return false;
        if (string2 == null) {
            return false;
        }
        int n = string2.length();
        if (n == 0) {
            return true;
        }
        int n2 = string.length() - n;
        while (n2 >= 0) {
            if (string.regionMatches(true, n2, string2, 0, n)) {
                return true;
            }
            --n2;
        }
        return false;
    }

    public static char getRandomNumber() {
        return "1234567890".charAt(ThreadLocalRandom.current().nextInt("1234567890".length()));
    }

    public static void threadSleep(long l) {
        try {
            Thread.sleep(l);
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public static String readFile(String string) {
        String string2 = null;
        try (FileInputStream fileInputStream = new FileInputStream(string);){
            string2 = new String(((InputStream)fileInputStream).readAllBytes());
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return string2;
    }

    public static String getRandomNumber(int n) {
        String string = "1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < n) {
            stringBuilder.append("1234567890".charAt(ThreadLocalRandom.current().nextInt("1234567890".length())));
            ++n2;
        }
        return stringBuilder.toString();
    }

    static {
        ISO_8901_US = DateTimeFormatter.ISO_OFFSET_DATE_TIME.localizedBy(Locale.US);
        ISO_8901_JS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));
        headers = new CharSequence[]{HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderNames.ACCEPT_RANGES, HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, HttpHeaderNames.ACCESS_CONTROL_REQUEST_HEADERS, HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD, HttpHeaderNames.AGE, HttpHeaderNames.ALLOW, HttpHeaderNames.CACHE_CONTROL, HttpHeaderNames.CONTENT_BASE, HttpHeaderNames.CONTENT_DISPOSITION, HttpHeaderNames.CONTENT_LANGUAGE, HttpHeaderNames.CONTENT_LOCATION, HttpHeaderNames.CONTENT_MD5, HttpHeaderNames.CONTENT_RANGE, HttpHeaderNames.DATE, HttpHeaderNames.ETAG, HttpHeaderNames.EXPECT, HttpHeaderNames.EXPIRES, HttpHeaderNames.IF_MATCH, HttpHeaderNames.IF_MODIFIED_SINCE, HttpHeaderNames.IF_NONE_MATCH, HttpHeaderNames.LAST_MODIFIED, HttpHeaderNames.LOCATION, HttpHeaderNames.ORIGIN, AsciiString.cached((String)"vary")};
        VER_PATTERN = Pattern.compile("Chrome/([0-9][0-9])");
    }

    @SafeVarargs
    public static Object randomFrom(Object ... objectArray) {
        return objectArray[ThreadLocalRandom.current().nextInt(objectArray.length)];
    }

    public static List quickParseAllGroups(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        ArrayList<String> arrayList = null;
        block0: while (matcher.find()) {
            if (arrayList == null) {
                arrayList = new ArrayList<String>();
            }
            int n = 0;
            while (true) {
                if (n >= matcher.groupCount()) continue block0;
                arrayList.add(matcher.group(n + 1));
                ++n;
            }
            break;
        }
        return arrayList;
    }

    public static int calculateMSLeftUntilHour() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime2 = localDateTime.plusHours(1L).truncatedTo(ChronoUnit.HOURS);
        Duration duration = Duration.between(localDateTime, localDateTime2);
        return (int)duration.toMillis();
    }

    public static String encodedDateISO(Instant instant) {
        return ISO_8901_JS.format(instant).replace(":", "%3A");
    }

    public static String centerString(int n, String string) {
        return String.format("%-" + n + "s", String.format("%" + (string.length() + (n - string.length()) / 2) + "s", string));
    }

    public static String quickParseFirst(String string, Pattern ... patternArray) {
        Pattern[] patternArray2 = patternArray;
        int n = patternArray2.length;
        int n2 = 0;
        while (n2 < n) {
            Pattern pattern = patternArray2[n2];
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                return matcher.group(1);
            }
            ++n2;
        }
        return null;
    }

    public static String parseChromeVer(String string) {
        Matcher matcher = VER_PATTERN.matcher(string);
        return matcher.find() ? matcher.group(1) : "88";
    }

    public static char getRandomChar() {
        char c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(ThreadLocalRandom.current().nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ".length()));
        if (!ThreadLocalRandom.current().nextBoolean()) return c;
        return Character.toLowerCase(c);
    }

    public static String generateStrongString() {
        return Utils.getRandomString(11) + Utils.getRandomNumber(4);
    }

    public static JsonArray exportCookies(CookieStore cookieStore) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
        JsonArray jsonArray = new JsonArray();
        Iterator iterator = cookieStore.get(Boolean.valueOf(true), ".yeezysupply.com", "/").iterator();
        while (iterator.hasNext()) {
            Cookie cookie = (Cookie)iterator.next();
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("domain", (Object)cookie.domain());
            jsonObject.put("name", (Object)cookie.name());
            jsonObject.put("path", (Object)cookie.path());
            jsonObject.put("value", (Object)cookie.value());
            jsonArray.add((Object)jsonObject);
        }
        return jsonArray;
    }

    public static String getStringCharacterOnly(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < n) {
            stringBuilder.append(Utils.getRandomChar());
            ++n2;
        }
        return stringBuilder.toString().toUpperCase(Locale.ROOT);
    }

    public static String centerStringBraced(int n, String string) {
        return String.format("[%-" + n + "s]", String.format("%" + (string.length() + (n - string.length()) / 2) + "s", string));
    }

    public static boolean isInteger(String string) {
        if (string == null) {
            return false;
        }
        int n = string.length();
        if (n == 0) {
            return false;
        }
        int n2 = 0;
        if (string.charAt(0) == '-') {
            if (n == 1) {
                return false;
            }
            n2 = 1;
        }
        while (n2 < n) {
            char c = string.charAt(n2);
            if (c < '0') return false;
            if (c > '9') {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public static void ensureBrotli() {
        try {
            Brotli4jLoader.ensureAvailability();
            byte[] byArray = Encoder.compress((byte[])"Meow".getBytes());
            DirectDecompress directDecompress = Decoder.decompress((byte[])byArray);
            if (directDecompress.getResultStatus() == DecoderJNI.Status.DONE) {
                return;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
        System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
        System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
        System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
        System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
    }

    public static String getRandomString(int n) {
        String string = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < n) {
            stringBuilder.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(ThreadLocalRandom.current().nextInt("abcdefghijklmnopqrstuvwxyz0123456789".length())));
            ++n2;
        }
        return stringBuilder.toString();
    }

    public static String secureHexstring(int n) {
        byte[] byArray = new byte[n];
        ThreadLocalRandom.current().nextBytes(byArray);
        return Hex.toHexString((byte[])byArray);
    }

    public static String reverseString(String string) {
        return new StringBuilder(string).reverse().toString();
    }

    public static double smartNextDouble() {
        double d = ThreadLocalRandom.current().nextDouble();
        if (!("" + d).contains("E")) return d;
        d = d * Double.longBitsToDouble(0x4000000000000000L) + Double.longBitsToDouble(4587366580439587226L);
        return d;
    }

    public static String quickParseFirstNonEmpty(String string, Pattern pattern) {
        String string2;
        Matcher matcher = pattern.matcher(string);
        do {
            if (!matcher.find()) return null;
        } while ((string2 = matcher.group(1)).isBlank());
        return string2;
    }

    public static String decodeHexString(String string) {
        if (!string.contains("\\x")) {
            return string;
        }
        string = string.replace("\\x", "").replace("\"", "");
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            String string2 = string.substring(n, n + 2);
            stringBuilder.append((char)Integer.parseInt(string2, 16));
            n += 2;
        }
        return stringBuilder.toString();
    }
}

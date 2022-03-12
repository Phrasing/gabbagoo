/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.tools;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deobfuscator {
    public static Pattern V2_S_STRINGS;
    public static Pattern MOD_STRINGS;
    public static Pattern S_STRINGS;
    public static Pattern B64_STRINGS;

    public static String n(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8);
        int n = 0;
        while (n < string2.length()) {
            int n2 = Character.codePointAt("zUP6yS7", n % 7);
            stringBuilder.append((char)(n2 ^ Character.codePointAt(string2, n)));
            ++n;
        }
        return "\"" + stringBuilder + "\"";
    }

    public static String deobV2(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.group(1).length() <= 0) continue;
            try {
                string = string.replace(matcher.group(0), Deobfuscator.t(matcher.group(1)));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed on: " + matcher.group(1));
            }
        }
        return string;
    }

    static {
        S_STRINGS = Pattern.compile("[S, u, o, r, t, w, i , n, s, O, e]\\(\"(.*?)\"\\)");
        B64_STRINGS = Pattern.compile("ut\\(\"(.*?)\"\\)");
        V2_S_STRINGS = Pattern.compile("[e, r, n, l, f, o, t]\\(\"(.*?)\"\\)");
        MOD_STRINGS = Pattern.compile(" \\+ r\\(\"(.*?)\"\\)");
    }

    public static String deBase64(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.group(1).length() <= 0) continue;
            try {
                System.out.println(matcher.group(0) + " - " + new String(Base64.getDecoder().decode(matcher.group(1)), StandardCharsets.UTF_8));
                string = string.replace(matcher.group(0), "\"" + new String(Base64.getDecoder().decode(matcher.group(1)), StandardCharsets.UTF_8).replace("\"", "\\\"") + "\"");
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed on: " + matcher.group(1));
            }
        }
        return string;
    }

    public static void main(String[] stringArray) {
        System.out.println(Deobfuscator.n("Kg1lA04"));
        String string = Deobfuscator.readJsFile("rawCaptcha.js");
    }

    public static String readJsFile(String string) {
        String string2 = null;
        try (FileInputStream fileInputStream = new FileInputStream(string);){
            string2 = new String(((InputStream)fileInputStream).readAllBytes());
            return string2;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return string2;
    }

    public static void outputJsFile(String string) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream("rdeobbed.js"), StandardCharsets.UTF_8));
            writer.write(string);
            return;
        }
        catch (IOException iOException) {
            return;
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception exception) {}
        }
    }

    public static String t(String string) {
        String string2 = new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8);
        int n = Character.codePointAt(string2, 0);
        Object object = "";
        int n2 = 1;
        while (n2 < string2.length()) {
            object = (String)object + (char)(n ^ Character.codePointAt(string2, n2));
            ++n2;
        }
        return object;
    }

    public static String deob(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.group(1).length() <= 0) continue;
            try {
                string = string.replace(matcher.group(0), " + " + Deobfuscator.n(matcher.group(1)));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed on: " + matcher.group(1));
            }
        }
        return string;
    }
}


/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.tmx;

import io.trickle.task.antibot.impl.tmx.Decoding;
import io.trickle.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLParser {
    public static Pattern ENCODED_STR_PAT;
    public List<String> urls;
    public static Pattern FUNCTION_VAR_PAT;
    public Map<String, Decoding> decodingMap;

    public URLParser(String string) {
        string = string.replace("{", "\n").replace("}", "\n").replace(";", "\n");
        this.decodingMap = new HashMap<String, Decoding>();
        this.urls = new ArrayList<String>();
        this.parseFuncVars(string);
        this.parseUrls(string);
    }

    public void parseFuncVars(String string) {
        Matcher matcher = FUNCTION_VAR_PAT.matcher(string);
        while (matcher.find()) {
            String[] stringArray = matcher.group(1).split("\\.");
            String string2 = stringArray.length == 1 ? stringArray[0] : stringArray[1];
            string2 = string2.replace("var", "").trim();
            String string3 = matcher.group(2);
            this.decodingMap.put(string2, new Decoding(string3));
        }
    }

    static {
        FUNCTION_VAR_PAT = Pattern.compile("(.*?) {0,2}= {0,2}new.*\\(\"(.*?)\"");
        ENCODED_STR_PAT = Pattern.compile("([.-z]*)\\(([0-9]*),([0-9]*)\\)");
    }

    public void parseUrls(String string) {
        Matcher matcher = ENCODED_STR_PAT.matcher(string);
        System.out.println(this.decodingMap.keySet());
        while (matcher.find()) {
            try {
                String string2;
                String[] stringArray = matcher.group(1).split("\\.");
                String string3 = string2 = stringArray.length == 2 ? stringArray[0] : stringArray[1];
                String string4 = this.decodingMap.get(string2 = string2.replace("?", "").trim()).td_f(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
                if (!string4.contains("http")) continue;
                this.urls.add(string4);
            }
            catch (Exception exception) {}
        }
    }

    public static void main(String[] stringArray) {
        URLParser uRLParser = new URLParser(Utils.readFileAsString("/Users/bayanrasooly/Documents/GitHub/TrickleV1.0/dev/bestbuy/tmx_raw.js"));
        System.out.println(uRLParser.urls);
    }
}


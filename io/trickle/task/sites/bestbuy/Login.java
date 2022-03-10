/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  javax.mail.search.SearchTerm
 */
package io.trickle.task.sites.bestbuy;

import io.trickle.task.sites.bestbuy.Login$1;
import io.trickle.util.Utils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.mail.search.SearchTerm;

public class Login {
    public static Pattern ALPHA_PATTERN;
    public static Pattern initDataPattern;
    public static SearchTerm SEARCH_TERM;
    public String challengeType;
    public static Pattern CODE_PATTERN;
    public JsonObject initData;
    public String flowOptions;

    public String getCode() {
        JsonArray jsonArray = this.initData.getJsonArray("codeList");
        int n = 0;
        while (n < jsonArray.size()) {
            String string = new String(Base64.getDecoder().decode(jsonArray.getString(n)));
            if (Utils.hasPattern(string, CODE_PATTERN)) {
                return jsonArray.getString(n);
            }
            ++n;
        }
        return "no-code";
    }

    public String getAlpha() {
        JsonArray jsonArray = this.initData.getJsonArray("alpha");
        int n = 0;
        while (n < jsonArray.size()) {
            String string = new String(Base64.getDecoder().decode(Utils.reverseString(jsonArray.getString(n))));
            if (Utils.hasPattern(string, ALPHA_PATTERN)) {
                return jsonArray.getString(n);
            }
            ++n;
        }
        return "no-alpha";
    }

    public static Login loginValues(String string) {
        return new Login(string);
    }

    static {
        SEARCH_TERM = new Login$1();
        initDataPattern = Pattern.compile("var initData = (.*?});");
        ALPHA_PATTERN = Pattern.compile("^[0-9]+_A_.+$");
        CODE_PATTERN = Pattern.compile("^\\d+_X_.+$");
    }

    public Login(String string) {
        this.initData = new JsonObject(Objects.requireNonNull(Utils.quickParseFirst(string, initDataPattern)));
    }
}


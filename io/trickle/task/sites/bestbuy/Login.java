/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.bestbuy.Login$1
 *  io.trickle.util.Utils
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  javax.mail.search.SearchTerm
 */
package io.trickle.task.sites.bestbuy;

import io.trickle.task.sites.bestbuy.Login;
import io.trickle.util.Utils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.mail.search.SearchTerm;

public class Login {
    public static Pattern initDataPattern;
    public String challengeType;
    public JsonObject initData;
    public static Pattern ALPHA_PATTERN;
    public static SearchTerm SEARCH_TERM;
    public String flowOptions;
    public static Pattern CODE_PATTERN;

    static {
        SEARCH_TERM = new 1();
        initDataPattern = Pattern.compile("var initData = (.*?});");
        ALPHA_PATTERN = Pattern.compile("^[0-9]+_A_.+$");
        CODE_PATTERN = Pattern.compile("^\\d+_X_.+$");
    }

    public static Login loginValues(String string) {
        return new Login(string);
    }

    public String getAlpha() {
        JsonArray jsonArray = this.initData.getJsonArray("alpha");
        int n = 0;
        while (n < jsonArray.size()) {
            String string = new String(Base64.getDecoder().decode(Utils.reverseString((String)jsonArray.getString(n))));
            if (Utils.hasPattern((String)string, (Pattern)ALPHA_PATTERN)) {
                return jsonArray.getString(n);
            }
            ++n;
        }
        return "no-alpha";
    }

    public Login(String string) {
        this.initData = new JsonObject(Objects.requireNonNull(Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{initDataPattern})));
    }

    public String getCode() {
        JsonArray jsonArray = this.initData.getJsonArray("codeList");
        int n = 0;
        while (n < jsonArray.size()) {
            String string = new String(Base64.getDecoder().decode(jsonArray.getString(n)));
            if (Utils.hasPattern((String)string, (Pattern)CODE_PATTERN)) {
                return jsonArray.getString(n);
            }
            ++n;
        }
        return "no-code";
    }
}

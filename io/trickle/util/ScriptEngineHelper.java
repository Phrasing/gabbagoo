/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.util.js.Script
 */
package io.trickle.util;

import io.trickle.util.js.Script;

public class ScriptEngineHelper {
    public static Script script = Script.fromCode((String)"\nfunction parseHexedTime(ciVal){\n    const hex =  escape(ciVal).split(\"%uDB40%uDD\").slice(1).reduce(function (t, n) {\n        return t + String.fromCodePoint(parseInt(n.substr(0, 2), 16));\n    }, \"\");\n\n    return decode(hex, 10);\n}\n\nfunction decode(t, n) {\n    for (var e = \"\", r = 0; r < t.length; r++)\n        e += String.fromCharCode(n ^ t.charCodeAt(r));\n    return e\n}\n");

    public static String calculateTime(String string) {
        return script.call("parseHexedTime", new String[]{string});
    }

    public static void test() {
        try {
            String string = ScriptEngineHelper.calculateTime("c62913f82818e7f8d800486099230cdfbbaa3a2b849f9f72d5644351ebb20a267934cab127cc2f2a123eead2f0056c4aebd46e4773d85cbb53a214cba4dc612c\udb40\udd38\udb40\udd33\udb40\udd39");
            if (string.equals("293")) return;
            throw new Exception();
        }
        catch (Throwable throwable) {
            System.out.println("Please report this to the developers: Error setting up environment, some functionality MAY BE limited: [sc]");
        }
    }
}

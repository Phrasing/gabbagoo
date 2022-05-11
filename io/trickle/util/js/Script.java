/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.Context
 *  org.graalvm.polyglot.HostAccess
 *  org.graalvm.polyglot.PolyglotAccess
 *  org.graalvm.polyglot.Source
 *  org.graalvm.polyglot.Value
 */
package io.trickle.util.js;

import java.io.IOException;
import java.io.Reader;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public class Script {
    public static String TYPE_JS = "js";
    public ThreadLocal<Context> jsCtx;
    public Source source;

    public static Context builder() {
        return Context.newBuilder((String[])new String[0]).allowHostAccess(HostAccess.ALL).allowNativeAccess(true).allowPolyglotAccess(PolyglotAccess.ALL).build();
    }

    public String call(String string, String ... stringArray) {
        Context context = this.getContext();
        Value value = context.getBindings("js").getMember(string);
        return value.execute((Object[])stringArray).toString();
    }

    public static Script fromStream(Reader reader) {
        try {
            return new Script(reader);
        }
        catch (IOException iOException) {
            System.out.println("Error loading script: " + iOException.getMessage());
            return null;
        }
    }

    public Script(String string) {
        this.source = Source.newBuilder((String)"js", (CharSequence)string, (String)"src.js").build();
        this.jsCtx = new ThreadLocal();
    }

    public Script(Reader reader) {
        this.source = Source.newBuilder((String)"js", (Reader)reader, (String)"src.js").build();
        this.jsCtx = new ThreadLocal();
    }

    public Value execute(String string) {
        Context context = this.getContext();
        Value value = context.getBindings("js").getMember(string);
        return value.execute(new Object[0]);
    }

    public static Script fromCode(String string) {
        try {
            return new Script(string);
        }
        catch (IOException iOException) {
            System.out.println("Error loading script: " + iOException.getMessage());
            return null;
        }
    }

    public Context getContext() {
        Context context = this.jsCtx.get();
        if (context != null) return context;
        Context context2 = Script.builder();
        context2.eval(this.source);
        this.jsCtx.set(context2);
        return this.getContext();
    }
}

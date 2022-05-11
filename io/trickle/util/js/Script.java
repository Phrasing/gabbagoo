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
   public ThreadLocal jsCtx;
   public Source source;

   public static Context builder() {
      return Context.newBuilder(new String[0]).allowHostAccess(HostAccess.ALL).allowNativeAccess(true).allowPolyglotAccess(PolyglotAccess.ALL).build();
   }

   public String call(String var1, String... var2) {
      Context var3 = this.getContext();
      Value var4 = var3.getBindings("js").getMember(var1);
      return var4.execute((Object[])var2).toString();
   }

   public static Script fromStream(Reader var0) {
      try {
         return new Script(var0);
      } catch (IOException var2) {
         System.out.println("Error loading script: " + var2.getMessage());
         return null;
      }
   }

   public Script(String var1) {
      this.source = Source.newBuilder("js", var1, "src.js").build();
      this.jsCtx = new ThreadLocal();
   }

   public Script(Reader var1) {
      this.source = Source.newBuilder("js", var1, "src.js").build();
      this.jsCtx = new ThreadLocal();
   }

   public Value execute(String var1) {
      Context var2 = this.getContext();
      Value var3 = var2.getBindings("js").getMember(var1);
      return var3.execute(new Object[0]);
   }

   public static Script fromCode(String var0) {
      try {
         return new Script(var0);
      } catch (IOException var2) {
         System.out.println("Error loading script: " + var2.getMessage());
         return null;
      }
   }

   public Context getContext() {
      Context var1 = (Context)this.jsCtx.get();
      if (var1 == null) {
         Context var2 = builder();
         var2.eval(this.source);
         this.jsCtx.set(var2);
         return this.getContext();
      } else {
         return var1;
      }
   }
}

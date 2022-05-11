module org.graalvm.js {
   requires com.oracle.truffle.regex;
   requires org.graalvm.sdk;
   requires org.graalvm.truffle;

   exports com.oracle.truffle.js.lang to
      org.graalvm.truffle;

   uses com.oracle.truffle.js.runtime.Evaluator;

   provides com.oracle.truffle.api.TruffleLanguage.Provider with
      com.oracle.truffle.js.lang.JavaScriptLanguageProvider;
   provides com.oracle.truffle.js.runtime.Evaluator with
      com.oracle.truffle.js.parser.GraalJSEvaluator;
}

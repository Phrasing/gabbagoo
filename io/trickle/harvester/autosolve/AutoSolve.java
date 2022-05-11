package io.trickle.harvester.autosolve;

import com.fuzzy.aycd.autosolve.AbstractAutoSolveManager;
import com.fuzzy.aycd.autosolve.model.AutoSolveStatus;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaToken;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaTokenRequest;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;

public class AutoSolve extends AbstractAutoSolveManager {
   public void onStatusChanged(AutoSolveStatus var1) {
   }

   public AutoSolve(String var1) {
      super(var1);
   }

   public void onCaptchaTokenRequestCancelled(CaptchaTokenRequest var1) {
   }

   public AutoSolve(OkHttpClient.Builder var1, String var2) {
      super(var1, var2);
   }

   public void onCaptchaTokenReceived(CaptchaToken var1) {
   }

   public Logger getLogger() {
      return super.getLogger();
   }
}

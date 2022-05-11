package io.trickle.harvester;

import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback.Response;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.core.VertxSingleton;
import io.trickle.util.Pair;
import io.trickle.util.request.Request;
import io.vertx.core.http.RequestOptions;
import java.util.Optional;

public class HybridHarvester$RequestInterceptor implements InterceptUrlRequestCallback {
   public InterceptUrlRequestCallback.Response on(InterceptUrlRequestCallback.Params var1) {
      String var2 = var1.urlRequest().url();
      if (var2.contains("/cart/add")) {
         System.out.println("Manual cart found");
         RequestOptions var3 = Request.convertToVertx(var1);
         Optional var4 = var1.uploadData();
         VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, var2);
         VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.CART_ADDRESS, new Pair(var3, var4));
      }

      return Response.proceed();
   }

   public Object on(Object var1) {
      return this.on((InterceptUrlRequestCallback.Params)var1);
   }
}

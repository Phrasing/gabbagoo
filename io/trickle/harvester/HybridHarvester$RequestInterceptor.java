/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Response
 *  io.trickle.basicgui.LinkOverrideListener
 *  io.trickle.core.VertxSingleton
 *  io.trickle.util.Pair
 *  io.trickle.util.request.Request
 *  io.vertx.core.http.RequestOptions
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.core.VertxSingleton;
import io.trickle.util.Pair;
import io.trickle.util.request.Request;
import io.vertx.core.http.RequestOptions;
import java.util.Optional;

public class HybridHarvester$RequestInterceptor
implements InterceptUrlRequestCallback {
    public InterceptUrlRequestCallback.Response on(InterceptUrlRequestCallback.Params params) {
        String string = params.urlRequest().url();
        if (!string.contains("/cart/add")) return InterceptUrlRequestCallback.Response.proceed();
        System.out.println("Manual cart found");
        RequestOptions requestOptions = Request.convertToVertx((InterceptUrlRequestCallback.Params)params);
        Optional optional = params.uploadData();
        VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, (Object)string);
        VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.CART_ADDRESS, (Object)new Pair((Object)requestOptions, (Object)optional));
        return InterceptUrlRequestCallback.Response.proceed();
    }

    public Object on(Object object) {
        return this.on((InterceptUrlRequestCallback.Params)object);
    }
}

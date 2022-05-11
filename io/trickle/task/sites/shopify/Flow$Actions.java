package io.trickle.task.sites.shopify;

import java.util.Map;

public interface Flow$Actions {
   boolean needsPrice();

   Map requiredCookies();

   boolean needsAuthTokens();
}

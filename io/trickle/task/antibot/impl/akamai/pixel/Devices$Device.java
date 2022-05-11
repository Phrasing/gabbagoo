package io.trickle.task.antibot.impl.akamai.pixel;

import io.vertx.core.json.JsonObject;

public interface Devices$Device {
   boolean isFp();

   JsonObject getSr();

   String getPs();

   JsonObject getTiming();

   String getZh();

   String getNap();

   int getB();

   boolean isSp();

   String getBr();

   JsonObject getDp();

   double getJsv();

   JsonObject getCrc();

   boolean isIeps();

   String getFh();

   String getFonts();

   JsonObject getNav();

   JsonObject getBt();

   String getBp();

   String getCv();

   String getLt();

   boolean isAv();

   boolean isFc();

   boolean isAp();

   int getC();
}

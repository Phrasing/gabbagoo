package io.trickle.task.sites.footlocker;

import io.trickle.task.Task;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.TaskApiClient;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.TimeUnit;

public class FootlockerAPI extends TaskApiClient {
   public Task task;

   public FootlockerAPI(Task var1) {
      this.task = var1;
   }

   public HttpRequest homePage() {
      HttpRequest var1 = super.client.getAbs("https://www.finishline.com/").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
      var1.putHeaders(Headers$Pseudo.MASP.get());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("Upgrade-Insecure-Requests", "1");
      var1.putHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; startmebot/1.0; +https://start.me/bot)");
      var1.putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("Sec-Fetch-Site", "none");
      var1.putHeader("Sec-Fetch-Mode", "navigate");
      var1.putHeader("Sec-Fetch-User", "?1");
      var1.putHeader("Sec-Fetch-Dest", "document");
      var1.putHeader("Accept-Encoding", "gzip, deflate, br");
      var1.putHeader("Accept-Language", "en-US,en;q=0.9");
      return var1;
   }
}

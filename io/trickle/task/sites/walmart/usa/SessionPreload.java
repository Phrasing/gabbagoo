package io.trickle.task.sites.walmart.usa;

import io.trickle.util.request.Request;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class SessionPreload {
   public WalmartAPI client;

   public static CompletableFuture async$putLocation(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      boolean var10001;
      switch (var3) {
         case 0:
            var1 = var0.client.putLocation();

            try {
               var10000 = Request.send(var1, (new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"postalCode\":\"" + var0.client.getTask().getProfile().getZip() + "\",\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\",\"serviceTypes\":\"ALL\"}")).toBuffer());
               if (!var10000.isDone()) {
                  CompletableFuture var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$putLocation);
               }
               break;
            } catch (Throwable var6) {
               var10001 = false;
               return CompletableFuture.completedFuture((Object)null);
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         HttpResponse var7 = (HttpResponse)var10000.join();
         if (var7 != null) {
         }
      } catch (Throwable var5) {
         var10001 = false;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$putCart(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      label35: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var3) {
            case 0:
               var1 = var0.client.putCart();

               try {
                  String var10003 = var0.client.getTask().getProfile().getZip();
                  var10000 = Request.send(var1, (new JsonObject("{\"currencyCode\":\"USD\",\"location\":{\"postalCode\":\"" + var10003 + "\",\"state\":\"" + var0.client.getTask().getProfile().getState() + "\",\"country\":\"USA\",\"isZipLocated\":false},\"storeIds\":[2648,5434,2031,2280,5426]}")).toBuffer());
                  if (!var10000.isDone()) {
                     CompletableFuture var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$putCart);
                  }
                  break;
               } catch (Throwable var6) {
                  var10001 = false;
                  break label35;
               }
            case 1:
               var10000 = var2;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            HttpResponse var7 = (HttpResponse)var10000.join();
            if (var7 != null) {
            }
         } catch (Throwable var5) {
            var10001 = false;
         }
      }

      var0.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
      var0.client.getWebClient().cookieStore().removeAnyMatch("CRT");
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fetchCart() {
      HttpRequest var1 = this.client.getCart();

      try {
         CompletableFuture var10000 = Request.send(var1);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchCart);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fetch() {
      try {
         CompletableFuture var10000 = this.zyDid();
         CompletableFuture var2;
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
         var10000 = this.fetchCart();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
         var10000 = this.updateLocation();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
         var10000 = this.putLocation();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
         var10000 = this.doPressoSearch();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
         this.client.getWebClient().cookieStore().put("test_cookie", "CheckForPermission", ".walmart.com");
         var10000 = this.fetchTerraFirma();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
         var10000 = this.fetchMidas();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         }

         var10000.join();
      } catch (Throwable var3) {
      }

      this.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
      this.client.getWebClient().cookieStore().removeAnyMatch("CRT");
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture createSession(Walmart var0) {
      return (new SessionPreload((API)var0.getClient())).fetch();
   }

   public static CompletableFuture async$fetch(SessionPreload var0, CompletableFuture var1, int var2, Object var3) {
      label100: {
         CompletableFuture var10000;
         boolean var10001;
         label108: {
            CompletableFuture var12;
            label109: {
               label110: {
                  label111: {
                     label86: {
                        label85: {
                           switch (var2) {
                              case 0:
                                 try {
                                    var10000 = var0.zyDid();
                                    if (!var10000.isDone()) {
                                       var12 = var10000;
                                       return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                                    }
                                    break;
                                 } catch (Throwable var11) {
                                    var10001 = false;
                                    break label100;
                                 }
                              case 1:
                                 var10000 = var1;
                                 break;
                              case 2:
                                 var10000 = var1;
                                 break label85;
                              case 3:
                                 var10000 = var1;
                                 break label86;
                              case 4:
                                 var10000 = var1;
                                 break label111;
                              case 5:
                                 var10000 = var1;
                                 break label110;
                              case 6:
                                 var10000 = var1;
                                 break label109;
                              case 7:
                                 var10000 = var1;
                                 break label108;
                              default:
                                 throw new IllegalArgumentException();
                           }

                           try {
                              var10000.join();
                              var10000 = var0.fetchCart();
                              if (!var10000.isDone()) {
                                 var12 = var10000;
                                 return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                              }
                           } catch (Throwable var10) {
                              var10001 = false;
                              break label100;
                           }
                        }

                        try {
                           var10000.join();
                           var10000 = var0.updateLocation();
                           if (!var10000.isDone()) {
                              var12 = var10000;
                              return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                           }
                        } catch (Throwable var9) {
                           var10001 = false;
                           break label100;
                        }
                     }

                     try {
                        var10000.join();
                        var10000 = var0.putLocation();
                        if (!var10000.isDone()) {
                           var12 = var10000;
                           return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }
                     } catch (Throwable var8) {
                        var10001 = false;
                        break label100;
                     }
                  }

                  try {
                     var10000.join();
                     var10000 = var0.doPressoSearch();
                     if (!var10000.isDone()) {
                        var12 = var10000;
                        return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                     }
                  } catch (Throwable var7) {
                     var10001 = false;
                     break label100;
                  }
               }

               try {
                  var10000.join();
                  var0.client.getWebClient().cookieStore().put("test_cookie", "CheckForPermission", ".walmart.com");
                  var10000 = var0.fetchTerraFirma();
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                  }
               } catch (Throwable var6) {
                  var10001 = false;
                  break label100;
               }
            }

            try {
               var10000.join();
               var10000 = var0.fetchMidas();
               if (!var10000.isDone()) {
                  var12 = var10000;
                  return var12.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
               }
            } catch (Throwable var5) {
               var10001 = false;
               break label100;
            }
         }

         try {
            var10000.join();
         } catch (Throwable var4) {
            var10001 = false;
         }
      }

      var0.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
      var0.client.getWebClient().cookieStore().removeAnyMatch("CRT");
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture zyDid() {
      HttpRequest var1 = this.client.updateCheck();

      try {
         CompletableFuture var10000 = Request.send(var1);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$zyDid);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fetchMidas() {
      HttpRequest var1 = this.client.midasScan();

      try {
         CompletableFuture var10000 = Request.send(var1);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchMidas);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$fetchCart(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      boolean var10001;
      switch (var3) {
         case 0:
            var1 = var0.client.getCart();

            try {
               var10000 = Request.send(var1);
               if (!var10000.isDone()) {
                  CompletableFuture var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchCart);
               }
               break;
            } catch (Throwable var6) {
               var10001 = false;
               return CompletableFuture.completedFuture((Object)null);
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         HttpResponse var7 = (HttpResponse)var10000.join();
         if (var7 != null) {
         }
      } catch (Throwable var5) {
         var10001 = false;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$zyDid(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      boolean var10001;
      switch (var3) {
         case 0:
            var1 = var0.client.updateCheck();

            try {
               var10000 = Request.send(var1);
               if (!var10000.isDone()) {
                  CompletableFuture var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$zyDid);
               }
               break;
            } catch (Throwable var6) {
               var10001 = false;
               return CompletableFuture.completedFuture((Object)null);
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         HttpResponse var7 = (HttpResponse)var10000.join();
         if (var7 != null) {
         }
      } catch (Throwable var5) {
         var10001 = false;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$doPressoSearch(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      boolean var10001;
      switch (var3) {
         case 0:
            var1 = var0.client.presoSearch();

            try {
               var10000 = Request.send(var1);
               if (!var10000.isDone()) {
                  CompletableFuture var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$doPressoSearch);
               }
               break;
            } catch (Throwable var6) {
               var10001 = false;
               return CompletableFuture.completedFuture((Object)null);
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         HttpResponse var7 = (HttpResponse)var10000.join();
         if (var7 != null) {
         }
      } catch (Throwable var5) {
         var10001 = false;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture doPressoSearch() {
      HttpRequest var1 = this.client.presoSearch();

      try {
         CompletableFuture var10000 = Request.send(var1);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$doPressoSearch);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture putLocation() {
      HttpRequest var1 = this.client.putLocation();

      try {
         CompletableFuture var10000 = Request.send(var1, (new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"postalCode\":\"" + this.client.getTask().getProfile().getZip() + "\",\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\",\"serviceTypes\":\"ALL\"}")).toBuffer());
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$putLocation);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture putCart() {
      HttpRequest var1 = this.client.putCart();

      try {
         String var10003 = this.client.getTask().getProfile().getZip();
         CompletableFuture var10000 = Request.send(var1, (new JsonObject("{\"currencyCode\":\"USD\",\"location\":{\"postalCode\":\"" + var10003 + "\",\"state\":\"" + this.client.getTask().getProfile().getState() + "\",\"country\":\"USA\",\"isZipLocated\":false},\"storeIds\":[2648,5434,2031,2280,5426]}")).toBuffer());
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$putCart);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      this.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
      this.client.getWebClient().cookieStore().removeAnyMatch("CRT");
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$fetchMidas(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      boolean var10001;
      switch (var3) {
         case 0:
            var1 = var0.client.midasScan();

            try {
               var10000 = Request.send(var1);
               if (!var10000.isDone()) {
                  CompletableFuture var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchMidas);
               }
               break;
            } catch (Throwable var6) {
               var10001 = false;
               return CompletableFuture.completedFuture((Object)null);
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         HttpResponse var7 = (HttpResponse)var10000.join();
         if (var7 != null) {
         }
      } catch (Throwable var5) {
         var10001 = false;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$updateLocation(SessionPreload var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      boolean var10001;
      switch (var3) {
         case 0:
            var1 = var0.client.getLocation();

            try {
               var10000 = Request.send(var1, (new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\"}")).toBuffer());
               if (!var10000.isDone()) {
                  CompletableFuture var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$updateLocation);
               }
               break;
            } catch (Throwable var6) {
               var10001 = false;
               return CompletableFuture.completedFuture((Object)null);
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         HttpResponse var7 = (HttpResponse)var10000.join();
         if (var7 != null) {
         }
      } catch (Throwable var5) {
         var10001 = false;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fetchTerraFirma() {
      try {
         String var10002 = this.client.getTask().getProfile().getZip();
         JsonObject var1 = new JsonObject("{\"variables\":\"{\\\"casperSlots\\\":{\\\"fulfillmentType\\\":\\\"ACC\\\",\\\"reservationType\\\":\\\"SLOTS\\\"},\\\"postalAddress\\\":{\\\"addressType\\\":\\\"RESIDENTIAL\\\",\\\"countryCode\\\":\\\"USA\\\",\\\"postalCode\\\":\\\"" + var10002 + "\\\",\\\"stateOrProvinceCode\\\":\\\"" + this.client.getTask().getProfile().getState() + "\\\",\\\"zipLocated\\\":true},\\\"storeFrontIds\\\":[{\\\"distance\\\":2.24,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91672\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91672},{\\\"distance\\\":3.04,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"5936\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":5936},{\\\"distance\\\":3.31,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"90563\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":90563},{\\\"distance\\\":3.41,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91675\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91675},{\\\"distance\\\":5.58,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91121\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91121}],\\\"productId\\\":\\\"152481472\\\",\\\"selected\\\":false}\"}");

         for(int var2 = 0; var2 < 5; ++var2) {
            HttpRequest var3 = this.client.terraFirma("152481472", ThreadLocalRandom.current().nextBoolean());
            CompletableFuture var10000 = Request.send(var3, var1.toBuffer());
            if (!var10000.isDone()) {
               CompletableFuture var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchTerraFirma);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null && var4.statusCode() != 429) {
               return CompletableFuture.completedFuture((Object)null);
            }
         }
      } catch (Throwable var6) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture updateLocation() {
      HttpRequest var1 = this.client.getLocation();

      try {
         CompletableFuture var10000 = Request.send(var1, (new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\"}")).toBuffer());
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(SessionPreload::async$updateLocation);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
         }
      } catch (Throwable var4) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public SessionPreload(API var1) {
      this.client = (WalmartAPI)var1;
   }

   public static CompletableFuture async$fetchTerraFirma(SessionPreload param0, JsonObject param1, int param2, HttpRequest param3, CompletableFuture param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }
}

package io.trickle.task.sites;

public enum Site {
   ONENESS,
   HAVEN,
   JIMMY_JAZZ,
   FUNKO,
   HLORENZO,
   CULTURE_KINGS,
   CONCEPTS,
   SAINT_ALFRED,
   OVO,
   SHOEPALACE,
   KITH,
   REIGNING_CHAMP,
   CREME,
   DSMNY,
   RSVP_GALLERY,
   DWAYN_TEST,
   SHOPNICEKICKS,
   UNION,
   ENERGIE334,
   ERIC_EMMANUEL,
   UNSUPPORTED_SITE,
   DSM_JP_ESHOP,
   BAPE,
   WALMART_CA,
   STAPLE_PIGEON,
   YCMC,
   LAPSTONE_AND_HAMMER,
   UNDEFEATED,
   WISH_ATL,
   FEAR_OF_GOD,
   TWELVE_AM_RUN,
   RENARTS,
   CHINATOWN_MARKET,
   JDSPORTS,
   DSML,
   AMAZON,
   CULTURE_KINGS_AU,
   NOTRE,
   SUEDE,
   MCT,
   TROPHY_ROOM,
   YEEZY,
   DSM_LONDON_ESHOP,
   SOCIALSTATUS,
   ZINGARO,
   DTLR,
   SNEAKER_POLITICS,
   FEATURE,
   JUICE_STORE,
   APB_STORE,
   CUSTOM_SHOPIFY,
   WALMART,
   WALMART_NEW,
   HIBBETT,
   PALACE_EU,
   JJJJound,
   XHIBITION,
   WINDANDSEA,
   FINISHLINE,
   BESTBUY,
   BLENDS,
   SOLEFLY,
   NOIRFONCE,
   PATTA,
   CALIF,
   PALACE,
   NRML,
   A_MA_MANIERE,
   HANON,
   BAPE_US,
   NEIGHBORHOOD,
   DEADSTOCK,
   OFF_THE_HOOK,
   CORPORATE,
   HATCLUB,
   DSMSG,
   DSMJP,
   BODEGA,
   LIKELIHOOD,
   ANTI_SOCIAL,
   AIME_LEON_DORE,
   COMMONWEALTH,
   BBC_ICE_CREAM,
   ATMOS,
   DSM_US_ESHOP,
   HUMANMADE,
   KAWS_TOKYO,
   PACKER_SHOES,
   KAWSONE,
   CAPSULE,
   UNION_TOKYO;

   public static Site[] $VALUES = new Site[]{UNSUPPORTED_SITE, AMAZON, WALMART, WALMART_CA, WALMART_NEW, YEEZY, FINISHLINE, JDSPORTS, HIBBETT, BESTBUY, MCT, CALIF, BAPE, BAPE_US, WINDANDSEA, HATCLUB, ZINGARO, KAWS_TOKYO, KITH, NEIGHBORHOOD, SHOEPALACE, KAWSONE, SHOPNICEKICKS, DTLR, DEADSTOCK, HAVEN, SOCIALSTATUS, HANON, PALACE, PALACE_EU, A_MA_MANIERE, AIME_LEON_DORE, COMMONWEALTH, CORPORATE, CREME, CULTURE_KINGS, CULTURE_KINGS_AU, DSML, DSMNY, DSMSG, DSMJP, DSM_US_ESHOP, DSM_JP_ESHOP, DSM_LONDON_ESHOP, HUMANMADE, ERIC_EMMANUEL, TWELVE_AM_RUN, BBC_ICE_CREAM, CHINATOWN_MARKET, BLENDS, NOIRFONCE, REIGNING_CHAMP, BODEGA, ANTI_SOCIAL, FEAR_OF_GOD, FEATURE, FUNKO, SOLEFLY, NOTRE, ONENESS, STUSSY, SUEDE, PATTA, RSVP_GALLERY, UNDEFEATED, TROPHY_ROOM, WISH_ATL, CONCEPTS, STAPLE_PIGEON, APB_STORE, ATMOS, CAPSULE, HLORENZO, JIMMY_JAZZ, JUICE_STORE, LAPSTONE_AND_HAMMER, LIKELIHOOD, NRML, OVO, OFF_THE_HOOK, PACKER_SHOES, PAM_PAM_LONDON, RENARTS, SAINT_ALFRED, EXTRA_BUTTER, JJJJound, SNEAKER_POLITICS, UNION, UNION_TOKYO, YCMC, XHIBITION, DWAYN_TEST, ENERGIE334, CUSTOM_SHOPIFY};
   PAM_PAM_LONDON,
   STUSSY,
   EXTRA_BUTTER;

   public static Site getSite(String var0) {
      String var1 = var0.toLowerCase();
      if (var1.contains("yeezy")) {
         return YEEZY;
      } else if (var1.contains("bestbuy")) {
         return BESTBUY;
      } else if (!var1.equalsIgnoreCase("walmart") && !var1.equalsIgnoreCase("walmartnew")) {
         if (var1.equalsIgnoreCase("walmartca")) {
            return WALMART_CA;
         } else if (var1.contains("hibbett")) {
            return HIBBETT;
         } else if (var1.contains("mct")) {
            return MCT;
         } else if (var1.replace(".", "").replace(" ", "").contains("bape") && var1.contains("us")) {
            return BAPE_US;
         } else if (var1.contains("bape")) {
            return BAPE;
         } else if (var1.contains("wind")) {
            return WINDANDSEA;
         } else if (!var1.contains("zingaro") && !var1.contains("tonari")) {
            if (!var1.contains("livestock") && !var1.contains("deadstock")) {
               if (var1.contains("haven")) {
                  return HAVEN;
               } else if (!var1.contains("neighborhood") && !var1.contains("nbhd")) {
                  if (var1.contains("kith")) {
                     return KITH;
                  } else if (var1.contains("dtlr")) {
                     return DTLR;
                  } else if (var1.contains("kawsone")) {
                     return KAWSONE;
                  } else if (var1.contains("hatclub")) {
                     return HATCLUB;
                  } else if (var1.replace(" ", "").contains("shoepalace")) {
                     return SHOEPALACE;
                  } else if (var1.replace(" ", "").contains("shopnicekicks")) {
                     return SHOPNICEKICKS;
                  } else if (var1.contains("anti") && var1.contains("social")) {
                     return ANTI_SOCIAL;
                  } else if (var1.contains("social")) {
                     return SOCIALSTATUS;
                  } else if (var1.contains("hanon")) {
                     return HANON;
                  } else if (var1.contains("palace")) {
                     return var1.contains("eu") ? PALACE_EU : PALACE;
                  } else if (var1.contains("maniere")) {
                     return A_MA_MANIERE;
                  } else if (var1.contains("aime")) {
                     return AIME_LEON_DORE;
                  } else if (var1.contains("commonwealth")) {
                     return COMMONWEALTH;
                  } else if (var1.contains("corporate")) {
                     return CORPORATE;
                  } else if (var1.contains("creme")) {
                     return CREME;
                  } else if (var1.contains("culture")) {
                     return var1.contains("au") ? CULTURE_KINGS_AU : CULTURE_KINGS;
                  } else if (var1.contains("human")) {
                     return HUMANMADE;
                  } else if (var1.contains("dsm")) {
                     if (var1.contains("l")) {
                        return DSML;
                     } else if (var1.contains("n")) {
                        return DSMNY;
                     } else if (var1.contains("s")) {
                        return DSMSG;
                     } else if (var1.contains("j") || var1.contains("g")) {
                        return DSMJP;
                     } else {
                        return UNSUPPORTED_SITE;
                     }
                  } else if (var1.contains("dover")) {
                     if (var1.contains("us.")) {
                        return DSM_US_ESHOP;
                     } else if (var1.contains("jp.")) {
                        return DSM_JP_ESHOP;
                     } else {
                        return DSM_LONDON_ESHOP;
                     }
                  } else if (var1.contains("eric")) {
                     return ERIC_EMMANUEL;
                  } else if (var1.contains("am") && var1.contains("run")) {
                     return TWELVE_AM_RUN;
                  } else if (var1.contains("bbc")) {
                     return BBC_ICE_CREAM;
                  } else if (var1.contains("china") || var1.contains("ctm")) {
                     return CHINATOWN_MARKET;
                  } else if (var1.contains("blend")) {
                     return BLENDS;
                  } else if (var1.contains("noir")) {
                     return NOIRFONCE;
                  } else if (var1.contains("reigning")) {
                     return REIGNING_CHAMP;
                  } else if (!var1.contains("bodega") && !var1.contains("bdga")) {
                     if (var1.contains("fear") && var1.contains("god")) {
                        return FEAR_OF_GOD;
                     } else if (var1.contains("feature")) {
                        return FEATURE;
                     } else if (var1.contains("funko")) {
                        return FUNKO;
                     } else if (var1.contains("solefly")) {
                        return SOLEFLY;
                     } else if (var1.contains("notre")) {
                        return NOTRE;
                     } else if (var1.contains("oneness")) {
                        return ONENESS;
                     } else if (var1.contains("stussy")) {
                        return STUSSY;
                     } else if (var1.contains("suede")) {
                        return SUEDE;
                     } else if (var1.contains("patta")) {
                        return PATTA;
                     } else if (var1.contains("rsvp")) {
                        return RSVP_GALLERY;
                     } else if (var1.contains("undefeated")) {
                        return UNDEFEATED;
                     } else if (var1.contains("trophy")) {
                        return TROPHY_ROOM;
                     } else if (var1.contains("wish")) {
                        return WISH_ATL;
                     } else if (!var1.contains("concept") && !var1.contains("cncpt")) {
                        if (var1.contains("staple") && var1.contains("pigeon")) {
                           return STAPLE_PIGEON;
                        } else if (var1.contains("apb")) {
                           return APB_STORE;
                        } else if (var1.contains("atmos")) {
                           return ATMOS;
                        } else if (var1.contains("capsule") && var1.contains("toronto")) {
                           return CAPSULE;
                        } else if (var1.contains("h") && var1.contains("lorenzo")) {
                           return HLORENZO;
                        } else if (var1.contains("jimmy") && var1.contains("jazz")) {
                           return JIMMY_JAZZ;
                        } else if (var1.contains("juice")) {
                           return JUICE_STORE;
                        } else if (var1.contains("lapstone")) {
                           return LAPSTONE_AND_HAMMER;
                        } else if (var1.contains("likelihood")) {
                           return LIKELIHOOD;
                        } else if (var1.contains("nrml")) {
                           return NRML;
                        } else if (var1.contains("october") && var1.contains("us")) {
                           return OVO;
                        } else if (var1.contains("off") && var1.contains("hook")) {
                           return OFF_THE_HOOK;
                        } else if (var1.contains("packer")) {
                           return PACKER_SHOES;
                        } else if (var1.contains("pam") && var1.contains("london")) {
                           return PAM_PAM_LONDON;
                        } else if (var1.contains("renarts")) {
                           return RENARTS;
                        } else if (var1.contains("saint") && var1.contains("alfred")) {
                           return SAINT_ALFRED;
                        } else if (var1.contains("butter")) {
                           return EXTRA_BUTTER;
                        } else if (var1.contains("jound")) {
                           return JJJJound;
                        } else if (var1.contains("sneaker") && var1.contains("politics")) {
                           return SNEAKER_POLITICS;
                        } else if (var1.contains("union")) {
                           if (var1.contains("tokyo")) {
                              return UNION_TOKYO;
                           } else {
                              return UNION;
                           }
                        } else if (var1.contains("ycmc")) {
                           return YCMC;
                        } else if (var1.contains("xhibition")) {
                           return XHIBITION;
                        } else if (var1.contains("energie3")) {
                           return ENERGIE334;
                        } else if (var1.contains("dwayn8080")) {
                           return DWAYN_TEST;
                        } else if (var1.contains(".")) {
                           return CUSTOM_SHOPIFY;
                        } else {
                           return UNSUPPORTED_SITE;
                        }
                     } else {
                        return CONCEPTS;
                     }
                  } else {
                     return BODEGA;
                  }
               } else {
                  return NEIGHBORHOOD;
               }
            } else {
               return DEADSTOCK;
            }
         } else {
            return ZINGARO;
         }
      } else {
         return WALMART_NEW;
      }
   }
}

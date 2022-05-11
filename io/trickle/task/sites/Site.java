/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites;

public enum Site {
    UNSUPPORTED_SITE,
    AMAZON,
    WALMART,
    WALMART_CA,
    WALMART_NEW,
    YEEZY,
    FINISHLINE,
    JDSPORTS,
    HIBBETT,
    BESTBUY,
    MCT,
    CALIF,
    BAPE,
    BAPE_US,
    WINDANDSEA,
    HATCLUB,
    ZINGARO,
    KAWS_TOKYO,
    KITH,
    NEIGHBORHOOD,
    SHOEPALACE,
    KAWSONE,
    SHOPNICEKICKS,
    DTLR,
    DEADSTOCK,
    HAVEN,
    SOCIALSTATUS,
    HANON,
    PALACE,
    PALACE_EU,
    A_MA_MANIERE,
    AIME_LEON_DORE,
    COMMONWEALTH,
    CORPORATE,
    CREME,
    CULTURE_KINGS,
    CULTURE_KINGS_AU,
    DSML,
    DSMNY,
    DSMSG,
    DSMJP,
    DSM_US_ESHOP,
    DSM_JP_ESHOP,
    DSM_LONDON_ESHOP,
    HUMANMADE,
    ERIC_EMMANUEL,
    TWELVE_AM_RUN,
    BBC_ICE_CREAM,
    CHINATOWN_MARKET,
    BLENDS,
    NOIRFONCE,
    REIGNING_CHAMP,
    BODEGA,
    ANTI_SOCIAL,
    FEAR_OF_GOD,
    FEATURE,
    FUNKO,
    SOLEFLY,
    NOTRE,
    ONENESS,
    STUSSY,
    SUEDE,
    PATTA,
    RSVP_GALLERY,
    UNDEFEATED,
    TROPHY_ROOM,
    WISH_ATL,
    CONCEPTS,
    STAPLE_PIGEON,
    APB_STORE,
    ATMOS,
    CAPSULE,
    HLORENZO,
    JIMMY_JAZZ,
    JUICE_STORE,
    LAPSTONE_AND_HAMMER,
    LIKELIHOOD,
    NRML,
    OVO,
    OFF_THE_HOOK,
    PACKER_SHOES,
    PAM_PAM_LONDON,
    RENARTS,
    SAINT_ALFRED,
    EXTRA_BUTTER,
    JJJJound,
    SNEAKER_POLITICS,
    UNION,
    UNION_TOKYO,
    YCMC,
    XHIBITION,
    DWAYN_TEST,
    ENERGIE334,
    CUSTOM_SHOPIFY;


    public static Site getSite(String string) {
        String string2 = string.toLowerCase();
        if (string2.contains("yeezy")) {
            return YEEZY;
        }
        if (string2.contains("bestbuy")) {
            return BESTBUY;
        }
        if (string2.equalsIgnoreCase("walmart")) return WALMART_NEW;
        if (string2.equalsIgnoreCase("walmartnew")) {
            return WALMART_NEW;
        }
        if (string2.equalsIgnoreCase("walmartca")) {
            return WALMART_CA;
        }
        if (string2.contains("hibbett")) {
            return HIBBETT;
        }
        if (string2.contains("mct")) {
            return MCT;
        }
        if (string2.replace(".", "").replace(" ", "").contains("bape") && string2.contains("us")) {
            return BAPE_US;
        }
        if (string2.contains("bape")) {
            return BAPE;
        }
        if (string2.contains("wind")) {
            return WINDANDSEA;
        }
        if (string2.contains("zingaro")) return ZINGARO;
        if (string2.contains("tonari")) {
            return ZINGARO;
        }
        if (string2.contains("livestock")) return DEADSTOCK;
        if (string2.contains("deadstock")) {
            return DEADSTOCK;
        }
        if (string2.contains("haven")) {
            return HAVEN;
        }
        if (string2.contains("neighborhood")) return NEIGHBORHOOD;
        if (string2.contains("nbhd")) {
            return NEIGHBORHOOD;
        }
        if (string2.contains("kith")) {
            return KITH;
        }
        if (string2.contains("dtlr")) {
            return DTLR;
        }
        if (string2.contains("kawsone")) {
            return KAWSONE;
        }
        if (string2.contains("hatclub")) {
            return HATCLUB;
        }
        if (string2.replace(" ", "").contains("shoepalace")) {
            return SHOEPALACE;
        }
        if (string2.replace(" ", "").contains("shopnicekicks")) {
            return SHOPNICEKICKS;
        }
        if (string2.contains("anti") && string2.contains("social")) {
            return ANTI_SOCIAL;
        }
        if (string2.contains("social")) {
            return SOCIALSTATUS;
        }
        if (string2.contains("hanon")) {
            return HANON;
        }
        if (string2.contains("palace")) {
            return string2.contains("eu") ? PALACE_EU : PALACE;
        }
        if (string2.contains("maniere")) {
            return A_MA_MANIERE;
        }
        if (string2.contains("aime")) {
            return AIME_LEON_DORE;
        }
        if (string2.contains("commonwealth")) {
            return COMMONWEALTH;
        }
        if (string2.contains("corporate")) {
            return CORPORATE;
        }
        if (string2.contains("creme")) {
            return CREME;
        }
        if (string2.contains("culture")) {
            return string2.contains("au") ? CULTURE_KINGS_AU : CULTURE_KINGS;
        }
        if (string2.contains("human")) {
            return HUMANMADE;
        }
        if (string2.contains("dsm")) {
            if (string2.contains("l")) {
                return DSML;
            }
            if (string2.contains("n")) {
                return DSMNY;
            }
            if (string2.contains("s")) {
                return DSMSG;
            }
            if (string2.contains("j")) return DSMJP;
            if (!string2.contains("g")) return UNSUPPORTED_SITE;
            return DSMJP;
        }
        if (string2.contains("dover")) {
            if (string2.contains("us.")) {
                return DSM_US_ESHOP;
            }
            if (!string2.contains("jp.")) return DSM_LONDON_ESHOP;
            return DSM_JP_ESHOP;
        }
        if (string2.contains("eric")) {
            return ERIC_EMMANUEL;
        }
        if (string2.contains("am") && string2.contains("run")) {
            return TWELVE_AM_RUN;
        }
        if (string2.contains("bbc")) {
            return BBC_ICE_CREAM;
        }
        if (string2.contains("china")) return CHINATOWN_MARKET;
        if (string2.contains("ctm")) {
            return CHINATOWN_MARKET;
        }
        if (string2.contains("blend")) {
            return BLENDS;
        }
        if (string2.contains("noir")) {
            return NOIRFONCE;
        }
        if (string2.contains("reigning")) {
            return REIGNING_CHAMP;
        }
        if (string2.contains("bodega")) return BODEGA;
        if (string2.contains("bdga")) {
            return BODEGA;
        }
        if (string2.contains("fear") && string2.contains("god")) {
            return FEAR_OF_GOD;
        }
        if (string2.contains("feature")) {
            return FEATURE;
        }
        if (string2.contains("funko")) {
            return FUNKO;
        }
        if (string2.contains("solefly")) {
            return SOLEFLY;
        }
        if (string2.contains("notre")) {
            return NOTRE;
        }
        if (string2.contains("oneness")) {
            return ONENESS;
        }
        if (string2.contains("stussy")) {
            return STUSSY;
        }
        if (string2.contains("suede")) {
            return SUEDE;
        }
        if (string2.contains("patta")) {
            return PATTA;
        }
        if (string2.contains("rsvp")) {
            return RSVP_GALLERY;
        }
        if (string2.contains("undefeated")) {
            return UNDEFEATED;
        }
        if (string2.contains("trophy")) {
            return TROPHY_ROOM;
        }
        if (string2.contains("wish")) {
            return WISH_ATL;
        }
        if (string2.contains("concept")) return CONCEPTS;
        if (string2.contains("cncpt")) {
            return CONCEPTS;
        }
        if (string2.contains("staple") && string2.contains("pigeon")) {
            return STAPLE_PIGEON;
        }
        if (string2.contains("apb")) {
            return APB_STORE;
        }
        if (string2.contains("atmos")) {
            return ATMOS;
        }
        if (string2.contains("capsule") && string2.contains("toronto")) {
            return CAPSULE;
        }
        if (string2.contains("h") && string2.contains("lorenzo")) {
            return HLORENZO;
        }
        if (string2.contains("jimmy") && string2.contains("jazz")) {
            return JIMMY_JAZZ;
        }
        if (string2.contains("juice")) {
            return JUICE_STORE;
        }
        if (string2.contains("lapstone")) {
            return LAPSTONE_AND_HAMMER;
        }
        if (string2.contains("likelihood")) {
            return LIKELIHOOD;
        }
        if (string2.contains("nrml")) {
            return NRML;
        }
        if (string2.contains("october") && string2.contains("us")) {
            return OVO;
        }
        if (string2.contains("off") && string2.contains("hook")) {
            return OFF_THE_HOOK;
        }
        if (string2.contains("packer")) {
            return PACKER_SHOES;
        }
        if (string2.contains("pam") && string2.contains("london")) {
            return PAM_PAM_LONDON;
        }
        if (string2.contains("renarts")) {
            return RENARTS;
        }
        if (string2.contains("saint") && string2.contains("alfred")) {
            return SAINT_ALFRED;
        }
        if (string2.contains("butter")) {
            return EXTRA_BUTTER;
        }
        if (string2.contains("jound")) {
            return JJJJound;
        }
        if (string2.contains("sneaker") && string2.contains("politics")) {
            return SNEAKER_POLITICS;
        }
        if (string2.contains("union")) {
            if (!string2.contains("tokyo")) return UNION;
            return UNION_TOKYO;
        }
        if (string2.contains("ycmc")) {
            return YCMC;
        }
        if (string2.contains("xhibition")) {
            return XHIBITION;
        }
        if (string2.contains("energie3")) {
            return ENERGIE334;
        }
        if (string2.contains("dwayn8080")) {
            return DWAYN_TEST;
        }
        if (!string2.contains(".")) return UNSUPPORTED_SITE;
        return CUSTOM_SHOPIFY;
    }
}

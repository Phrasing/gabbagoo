/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.Task
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.Mode
 *  io.trickle.task.sites.shopify.util.SiteParser$1
 *  io.trickle.util.Pair
 */
package io.trickle.task.sites.shopify.util;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Mode;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.util.Pair;

public class SiteParser {
    public static Pair getProperties(Site site) {
        switch (1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 12: {
                return new Pair((Object)"_hkey", (Object)"789gfd78934hfk74jml849320");
            }
            case 10: {
                return new Pair((Object)"upsell", (Object)"mens");
            }
            case 50: {
                return new Pair((Object)"_stussy_lip", (Object)"_qdXpxlhm8D");
            }
        }
        return null;
    }

    public static String getURLFromSite(Task task) {
        switch (1.$SwitchMap$io$trickle$task$sites$Site[task.getSite().ordinal()]) {
            case 1: {
                return "mct.tokyo";
            }
            case 2: {
                return "us.bape.com";
            }
            case 3: {
                return "bape.com";
            }
            case 4: {
                return "windandsea.jp";
            }
            case 5: {
                return "zingaro.shop";
            }
            case 6: {
                return "www.deadstock.ca";
            }
            case 7: {
                return "www.hatclub.com";
            }
            case 8: {
                return "shop.havenshop.com";
            }
            case 9: {
                return "www.neighborhood.jp";
            }
            case 10: {
                return "kith.com";
            }
            case 11: {
                return "www.dtlr.com";
            }
            case 12: {
                return "kawsone.com";
            }
            case 13: {
                return "www.shoepalace.com";
            }
            case 14: {
                return "store.kaws-tokyo-first.jp";
            }
            case 15: {
                return "shopnicekicks.com";
            }
            case 16: {
                return "www.socialstatuspgh.com";
            }
            case 17: {
                return "www.hanon-shop.com";
            }
            case 18: {
                return "shop.palaceskateboards.com";
            }
            case 19: {
                return "shop-usa.palaceskateboards.com";
            }
            case 20: {
                return "www.a-ma-maniere.com";
            }
            case 21: {
                return "www.aimeleondore.com";
            }
            case 22: {
                return "commonwealth-ftgg.com";
            }
            case 23: {
                return "corporategotem.com";
            }
            case 24: {
                return "creme321.com";
            }
            case 25: {
                return "www.culturekings.com.au";
            }
            case 26: {
                return "www.culturekings.com";
            }
            case 27: {
                return "humanmade.jp";
            }
            case 28: {
                return "eflash.doverstreetmarket.com";
            }
            case 29: {
                return "eflash-us.doverstreetmarket.com";
            }
            case 30: {
                return "eflash-sg.doverstreetmarket.com";
            }
            case 31: {
                return "eflash-jp.doverstreetmarket.com";
            }
            case 32: {
                return "shop-us.doverstreetmarket.com";
            }
            case 33: {
                return "shop-jp.doverstreetmarket.com";
            }
            case 34: {
                return "shop.doverstreetmarket.com";
            }
            case 35: {
                return "www.ericemanuel.com";
            }
            case 36: {
                return "www.12amrun.com";
            }
            case 37: {
                return "www.bbcicecream.com";
            }
            case 38: {
                return "thechinatownmarket.com";
            }
            case 39: {
                return "www.blendsus.com";
            }
            case 40: {
                return "www.noirfonce.eu";
            }
            case 41: {
                return "shop.reigningchamp.com";
            }
            case 42: {
                return "bdgastore.com";
            }
            case 43: {
                return "www.antisocialsocialclub.com";
            }
            case 44: {
                return "fearofgod.com";
            }
            case 45: {
                return "feature.com";
            }
            case 46: {
                return "checkout.funko.com";
            }
            case 47: {
                return "www.solefly.com";
            }
            case 48: {
                return "www.notre-shop.com";
            }
            case 49: {
                return "www.onenessboutique.com";
            }
            case 50: {
                return "www.stussy.com";
            }
            case 51: {
                return "suede-store.com";
            }
            case 52: {
                return "www.patta.nl";
            }
            case 53: {
                return "rsvpgallery.com";
            }
            case 54: {
                return "undefeated.com";
            }
            case 55: {
                return "www.trophyroomstore.com";
            }
            case 56: {
                return "wishatl.com";
            }
            case 57: {
                return "cncpts.com";
            }
            case 58: {
                return "www.staplepigeon.com";
            }
            case 59: {
                return "www.apbstore.com";
            }
            case 60: {
                return "www.atmosusa.com";
            }
            case 61: {
                return "www.capsuletoronto.com";
            }
            case 62: {
                return "www.hlorenzo.com";
            }
            case 63: {
                return "www.jimmyjazz.com";
            }
            case 64: {
                return "juicestore.com";
            }
            case 65: {
                return "www.lapstoneandhammer.com";
            }
            case 66: {
                return "likelihood.us";
            }
            case 67: {
                return "nrml.ca";
            }
            case 68: {
                return "us.octobersveryown.com";
            }
            case 69: {
                return "offthehook.ca";
            }
            case 70: {
                return "packershoes.com";
            }
            case 71: {
                return "pampamlondon.com";
            }
            case 72: {
                return "renarts.com";
            }
            case 73: {
                return "www.saintalfred.com";
            }
            case 74: {
                return "extrabutterny.com";
            }
            case 75: {
                return "www.jjjjound.com";
            }
            case 76: {
                return "sneakerpolitics.com";
            }
            case 77: {
                return "www.uniontokyo.jp";
            }
            case 78: {
                return "store.unionlosangeles.com";
            }
            case 79: {
                return "www.ycmc.com";
            }
            case 80: {
                return "www.xhibition.co";
            }
            case 81: {
                return "energie334.com";
            }
            case 82: {
                return "dwayn8080.myshopify.com";
            }
        }
        return task.getSiteUserEntry();
    }

    public static String getWalletsAuth(Site site) {
        switch (1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return "ff65cd500f4230764983c5ad6b1189a4";
            }
            case 3: {
                return "c1ef5db10e26afc64a0792ed1e244f65";
            }
            case 4: {
                return "77a5ac7b3d81a017632f303091c7b9c9";
            }
            case 5: {
                return "dc694e5424b01f7240623f6595a2f45e";
            }
            case 10: {
                return "ad7484cade5cec927a8d5f6ed774b524";
            }
            case 9: {
                return "8a08855061ca89042518dd188518f665";
            }
            case 15: {
                return "9d6556dc3ee20bf6b1a0971ad22f8238";
            }
            case 13: {
                return "8417f5cbfcb3e9747114208dcd8805ce";
            }
            case 2: {
                return "a45de9ba54947c6286e329042853dafc";
            }
            case 6: {
                return "29c1e1054770e9694717256c270f4359";
            }
            case 8: {
                return "b2d28cc6049fca1d79dbc390a0fef334";
            }
            case 11: {
                return "855eb882c2ce4ccbf3d77cd87a671014";
            }
            case 16: {
                return "a62a7cdb1689c171a551ac61e92dc08a";
            }
            case 17: {
                return "f73913c594921bdd6a18a32bc2e2d7df";
            }
            case 18: {
                return "";
            }
            case 19: {
                return "59571a4e503fc3164848c544a5fda777";
            }
            case 20: {
                return "23348e90253d0a4895d5524b79914bbe";
            }
            case 21: {
                return "e80eac1bcd1da4a8924975cb6db23066";
            }
            case 22: {
                return "9f57a59e7362969406227b0d77620ba9";
            }
            case 23: {
                return "b9a5dd57f09a3fe60a9884699b820cb4";
            }
            case 24: {
                return "195828e2ef14c58600012bc14a1f7081";
            }
            case 25: {
                return "caf0e6745d86e975d0f9aac94f8fe0cc";
            }
            case 26: {
                return "006f958feaf55788fcd9233f655ef2b1";
            }
            case 27: {
                return "c96e2c5fc4942663ae406b6cccdf583a";
            }
            case 28: {
                return "";
            }
            case 29: {
                return "";
            }
            case 30: {
                return "";
            }
            case 31: {
                return "";
            }
            case 32: {
                return "6a5422eafb690305d53b824f3a9f90ef";
            }
            case 33: {
                return "4a722c33f828c3ca513cbe54ce988c75";
            }
            case 34: {
                return "284ee81302f1c5629d9428572f1980bf";
            }
            case 35: {
                return "1add1d8285c287e7fdfd4a3873807a11";
            }
            case 36: {
                return "e5b0d0dc103ac126c494f8cc1fd70fe9";
            }
            case 37: {
                return "344bd1cc389a1b9d715d6a9103761258";
            }
            case 38: {
                return "aed80fc12652b472b86cd42e2ee42f73";
            }
            case 39: {
                return "a695a9ac76b89f50663628617f8498f6";
            }
            case 40: {
                return "39aa752c77d4ac53e9daf9d49f61c0ad";
            }
            case 41: {
                return "cdecb98ef85c1fac66576864dbd5ab9c";
            }
            case 42: {
                return "dbd316d5c797eb8e3caede9dd08f92ef";
            }
            case 43: {
                return "4189262a3c6b7c3fea963c5602c51182";
            }
            case 44: {
                return "0cf616930515540d894572734816ed6a";
            }
            case 45: {
                return "d3ced42695b5dc27734d4638c3a97ae1";
            }
            case 46: {
                return "a861544cb572058d21ab033f62010615";
            }
            case 47: {
                return "b51037e17cfc5e142a20ef01f0b44751";
            }
            case 48: {
                return "2f85af10bb30c1b7a78259b6c1c49d53";
            }
            case 49: {
                return "5de5d471976e82ceba582a81606e0ac5";
            }
            case 50: {
                return "69a4942adf685cdace4f72eb706542b4";
            }
            case 51: {
                return "71f164fa1c603d9d3da229312d52cc1d";
            }
            case 52: {
                return "94ce7a06ff5db9b60a31f8bfa66df69e";
            }
            case 53: {
                return "68861eece3d19735546a05faf429e759";
            }
            case 54: {
                return "a0faf54ad7ec6fbbab86cd3f949c3cb9";
            }
            case 55: {
                return "1b124ca786ab5fc9065699f1a383a139";
            }
            case 56: {
                return "7678df516143cdcb4c72168e8556b583";
            }
            case 57: {
                return "5f6b24ca255661ff3414cee10472efd1";
            }
            case 58: {
                return "2d91ae06229ae2f075a0db19de909e7c";
            }
            case 59: {
                return "cf7dd82098a4388648acb12a5a9aa063";
            }
            case 60: {
                return "52842063af9a7e1efb679833f792a84e";
            }
            case 61: {
                return "748d6a9d626b2a10df7fd111523a3136";
            }
            case 62: {
                return "e57efe46789bb77c0b824f7304502e6e";
            }
            case 63: {
                return "f0f1700d9e233bb78b9918071320970a";
            }
            case 64: {
                return "9a6e64f5fcbd1ddee3e5d00bfee47e5c";
            }
            case 65: {
                return "49b214ac5d8865c0b591436dc44be785";
            }
            case 66: {
                return "ae488d768336f38126f7d163c8738900";
            }
            case 67: {
                return "6f55f8c75577c1063d8489c94d9ab122";
            }
            case 68: {
                return "97f3874f6d268f1c681d88ef789edee3";
            }
            case 69: {
                return "2e0e6c2f045e1c04714f65b0a0b30b0c";
            }
            case 70: {
                return "b26bacc7a6c3fb5ea949af1df91f3d1b";
            }
            case 71: {
                return "f8dd53300f7585887bad7dec5f014fe5";
            }
            case 72: {
                return "fe288dcbb1f39f890295118d31cef77e";
            }
            case 73: {
                return "849af6fa065de14b021e119a4ad12bc0";
            }
            case 74: {
                return "9474824e25b8e9a0f6ecd7d8be9a79de";
            }
            case 75: {
                return "c68a4abb16f49b255aee526fb1ab065c";
            }
            case 76: {
                return "e7f4837ec5196326af7949de4b8381fe";
            }
            case 78: {
                return "2a9b9ce6e2c50b7c81322cbd33a008ce";
            }
            case 79: {
                return "2d7b5b163b178f528840fe47b894412e";
            }
            case 80: {
                return "7f11f1a79cb6f8d07bca2f1d113177ef";
            }
            case 82: {
                return "ad7484cade5cec927a8d5f6ed774b524";
            }
            case 7: {
                return "119ffadd7e2ca814ab40d1928e03db9d";
            }
        }
        return null;
    }

    public static Mode getMode(String string) {
        if (string.contains("normal")) {
            return Mode.NORMAL;
        }
        if (string.contains("fast")) {
            return Mode.FAST;
        }
        if (!string.contains("hybrid")) return Mode.HUMAN;
        return Mode.HYBRID;
    }

    public static String getGatewayFromSite(Site site, boolean bl) {
        switch (1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return "23332880433";
            }
            case 3: {
                if (!bl) return "46434943107";
                return "48703537283";
            }
            case 4: {
                return "16598368320";
            }
            case 5: {
                return "61075226777";
            }
            case 14: {
                return "66384658613";
            }
            case 10: {
                return "128707719";
            }
            case 9: {
                if (!bl) return "35536273485";
                return "35269509197";
            }
            case 15: {
                return "6735901";
            }
            case 12: {
                return "26365427785";
            }
            case 13: {
                return "39578599470";
            }
            case 2: {
                return "3180838";
            }
            case 6: {
                return "8308339";
            }
            case 8: {
                return "5021317";
            }
            case 11: {
                return "36165058639";
            }
            case 16: {
                return "18736001";
            }
            case 17: {
                return "6822068256";
            }
            case 18: {
                return "";
            }
            case 19: {
                return "";
            }
            case 20: {
                return "26102467";
            }
            case 21: {
                return "4435025";
            }
            case 22: {
                return "6732633";
            }
            case 23: {
                return "38778535980";
            }
            case 24: {
                return "";
            }
            case 25: {
                return "18476826699";
            }
            case 26: {
                return "6834618481";
            }
            case 27: {
                return "9440886820";
            }
            case 28: {
                return "";
            }
            case 29: {
                return "";
            }
            case 30: {
                return "";
            }
            case 31: {
                return "";
            }
            case 32: {
                return "49114710168";
            }
            case 33: {
                return "63956484249";
            }
            case 34: {
                return "58284638364";
            }
            case 35: {
                return "6982297";
            }
            case 36: {
                return "32034499";
            }
            case 37: {
                return "19067457";
            }
            case 38: {
                return "104201289";
            }
            case 39: {
                return "8789131311";
            }
            case 40: {
                return "25732036";
            }
            case 41: {
                return "113988484";
            }
            case 42: {
                return "3923843";
            }
            case 43: {
                return "76855495";
            }
            case 44: {
                return "39976581";
            }
            case 45: {
                return "5901373";
            }
            case 46: {
                return "41338373";
            }
            case 47: {
                return "";
            }
            case 48: {
                return "8410639";
            }
            case 49: {
                return "3919159";
            }
            case 50: {
                return "21594505312";
            }
            case 51: {
                return "35781541953";
            }
            case 52: {
                return "22655139875";
            }
            case 53: {
                return "18566209";
            }
            case 54: {
                return "5730877";
            }
            case 7: {
                return "19188801";
            }
            case 55: {
                return "37628248142";
            }
            case 56: {
                return "34678149";
            }
            case 57: {
                return "";
            }
            case 58: {
                return "54720705";
            }
            case 59: {
                return "9556131876";
            }
            case 60: {
                return "8180203572";
            }
            case 61: {
                return "";
            }
            case 62: {
                return "118964878";
            }
            case 63: {
                return "37105762413";
            }
            case 64: {
                return "38506922042";
            }
            case 65: {
                return "12160961";
            }
            case 66: {
                return "7452353";
            }
            case 67: {
                return "75922051";
            }
            case 68: {
                return "237797396";
            }
            case 69: {
                return "61989958";
            }
            case 70: {
                return "3723727";
            }
            case 71: {
                return "7268817";
            }
            case 72: {
                return "18134660";
            }
            case 73: {
                return "3953145";
            }
            case 74: {
                return "";
            }
            case 75: {
                return "7615291";
            }
            case 76: {
                return "6692225";
            }
            case 77: {
                return "65720942743";
            }
            case 78: {
                return "6757828";
            }
            case 79: {
                return "38994542692";
            }
            case 80: {
                return "22402692";
            }
        }
        return null;
    }
}

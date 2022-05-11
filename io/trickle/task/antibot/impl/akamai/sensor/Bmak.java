package io.trickle.task.antibot.impl.akamai.sensor;

import io.trickle.task.antibot.impl.akamai.Devices;
import io.trickle.task.antibot.impl.akamai.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.captcha.util.Ww;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Bmak {
   public int xagg;
   public String ins;
   public int brv;
   public String prod;
   public String vcact;
   public String informinfo;
   public String cns;
   public int ke_cnt;
   public List mn_al;
   public int pe_vel;
   public String fpcf_rVal;
   public int mn_wt;
   public int pen;
   public int init_time;
   public int mn_mc_lmt;
   public int mn_stout;
   public boolean firstLoad;
   public String ckie;
   public String mn_abck;
   public int mn_tout;
   public int aj_indx;
   public String mn_cc;
   public int te_vel;
   public String dmact;
   public String abck;
   public String mn_psn;
   public Devices$Device device;
   public int te_cnt;
   public String fpcf_rCFP;
   public int mn_mc_indx;
   public String pact;
   public List mn_lg;
   public int me_cnt;
   public int den;
   public String doact;
   public int d2;
   public Map mn_r;
   public List mn_il;
   public static String personal_dwayn_json = "{\"ap\":true,\"bt\":{\"charging\":true,\"chargingTime\":0,\"dischargingTime\":\"Infinity\",\"level\":1,\"onchargingchange\":null,\"onchargingtimechange\":null,\"ondischargingtimechange\":null,\"onlevelchange\":null},\"fonts\":\"4,14,15,16,21,22,23,43,44,47,48,49,50,51\",\"fh\":\"f78bc5f5ba69eb06e56c3827f66c171eb2b27f75\",\"timing\":\"\",\"bp\":\"1038350511,-1979380391,1738406762,749224105\",\"sr\":{\"inner\":[3356,1306],\"outer\":[3356,1417],\"screen\":[0,23],\"pageOffset\":[0,0],\"avail\":[3440,1417],\"size\":[3440,1440],\"client\":[3340,176],\"colorDepth\":24,\"pixelDepth\":24},\"dp\":{\"XDomainRequest\":0,\"createPopup\":0,\"removeEventListener\":1,\"globalStorage\":0,\"openDatabase\":1,\"indexedDB\":1,\"attachEvent\":0,\"ActiveXObject\":0,\"dispatchEvent\":1,\"addBehavior\":0,\"addEventListener\":1,\"detachEvent\":0,\"fireEvent\":0,\"MutationObserver\":1,\"HTMLMenuItemElement\":0,\"Int8Array\":1,\"postMessage\":1,\"querySelector\":1,\"getElementsByClassName\":1,\"images\":1,\"compatMode\":\"CSS1Compat\",\"documentMode\":0,\"all\":1,\"now\":1,\"contextMenu\":0},\"lt\":\"1622868421082-4\",\"ps\":\"true,true\",\"cv\":\"f50fd5bc1e5aa5aab1cd866593c22ef9d61f14e3\",\"fp\":false,\"sp\":false,\"br\":\"Chrome\",\"ieps\":false,\"av\":false,\"b\":1,\"c\":0,\"jsv\":\"1.7\",\"nav\":{\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appName\":\"Netscape\",\"appCodeName\":\"Mozilla\",\"appVersion\":\"5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appMinorVersion\":0,\"product\":\"Gecko\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"vendorSub\":\"\",\"buildID\":0,\"platform\":\"MacIntel\",\"oscpu\":0,\"hardwareConcurrency\":12,\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"systemLanguage\":0,\"userLanguage\":0,\"doNotTrack\":null,\"msDoNotTrack\":0,\"cookieEnabled\":true,\"geolocation\":1,\"vibrate\":1,\"maxTouchPoints\":0,\"webdriver\":false,\"plugins\":[\"Chrome PDF Plugin\",\"Chrome PDF Viewer\",\"Native Client\"]},\"crc\":{\"window.chrome\":{\"app\":{\"isInstalled\":false,\"InstallState\":{\"DISABLED\":\"disabled\",\"INSTALLED\":\"installed\",\"NOT_INSTALLED\":\"not_installed\"},\"RunningState\":{\"CANNOT_RUN\":\"cannot_run\",\"READY_TO_RUN\":\"ready_to_run\",\"RUNNING\":\"running\"}},\"runtime\":{\"OnInstalledReason\":{\"CHROME_UPDATE\":\"chrome_update\",\"INSTALL\":\"install\",\"SHARED_MODULE_UPDATE\":\"shared_module_update\",\"UPDATE\":\"update\"},\"OnRestartRequiredReason\":{\"APP_UPDATE\":\"app_update\",\"OS_UPDATE\":\"os_update\",\"PERIODIC\":\"periodic\"},\"PlatformArch\":{\"ARM\":\"arm\",\"ARM64\":\"arm64\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformNaclArch\":{\"ARM\":\"arm\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformOs\":{\"ANDROID\":\"android\",\"CROS\":\"cros\",\"LINUX\":\"linux\",\"MAC\":\"mac\",\"OPENBSD\":\"openbsd\",\"WIN\":\"win\"},\"RequestUpdateCheckStatus\":{\"NO_UPDATE\":\"no_update\",\"THROTTLED\":\"throttled\",\"UPDATE_AVAILABLE\":\"update_available\"}}}},\"nap\":\"11321144241322243122\",\"fc\":true,\"ua\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"product\":\"Gecko\",\"availWidth\":3440,\"availHeight\":1417,\"width\":3440,\"height\":1440,\"innerHeight\":1306,\"innerWidth\":3356,\"outerWidth\":3356,\"brave\":false,\"windowPerms\":{\"t\":1,\"a\":1,\"e\":0,\"n\":0,\"o\":1,\"m\":1,\"r\":1,\"i\":0,\"c\":1,\"b\":1,\"d\":0,\"s\":1,\"k\":1,\"l\":1},\"_phantom\":0,\"webdriver\":0,\"domAutomation\":0,\"activeXObject\":0,\"callPhantom\":0,\"documentMode\":0,\"isChrome\":0,\"isOnline\":1,\"isOpera\":0,\"hasInstallTrigger\":0,\"hasHTMLElement\":0,\"hasRTCPeerConnection\":1,\"hasMozInnerScreen\":0,\"hasVibrate\":1,\"hasBattery\":1,\"hasForEach\":0,\"hasFileReader\":1,\"deviceOrientation\":\"do_en\",\"deviceMotion\":\"dm_en\",\"touchEvent\":\"t_en\",\"navigatorFasSettings\":30261693,\"sed\":\"0,0,0,0,1,0,0\",\"languages\":\"en-US\",\"pluginsLength\":3,\"colorDepth\":24,\"pixelDepth\":24,\"cookieEnabled\":true,\"javaEnabled\":false,\"doNotTrack\":-1,\"canvasFP1\":\"2130238721\",\"canvasFP2\":\"-955688593\",\"rCFP\":\"-1241881201\",\"rVal\":\"649\",\"pluginInfo\":\",7,8\",\"sessionStorage\":true,\"localStorage\":true,\"indexedDB\":true,\"timezoneOffset\":240,\"webRTC\":true,\"voices\":\"Google Deutsch_de-DEGoogle US English_en-USGoogle UK English Female_en-GBGoogle UK English Male_en-GBGoogle español_es-ESGoogle español de Estados Unidos_es-USGoogle français_fr-FRGoogle हिन्दी_hi-INGoogle Bahasa Indonesia_id-IDGoogle italiano_it-ITGoogle 日本語_ja-JPGoogle 한국의_ko-KRGoogle Nederlands_nl-NLGoogle polski_pl-PLGoogle português do Brasil_pt-BRGoogle русский_ru-RUGoogle 普通话（中国大陆）_zh-CNGoogle 粤語（香港）_zh-HKGoogle 國語（臺灣）_zh-TWAlex_en-USAlice_it-ITAlva_sv-SEAmelie_fr-CAAnna_de-DECarmit_he-ILDamayanti_id-IDDaniel_en-GBDiego_es-AREllen_nl-BEFiona_enFred_en-USIoana_ro-ROJoana_pt-PTJorge_es-ESJuan_es-MXKanya_th-THKaren_en-AUKyoko_ja-JPLaura_sk-SKLekha_hi-INLuca_it-ITLuciana_pt-BRMaged_ar-SAMariska_hu-HUMei-Jia_zh-TWMelina_el-GRMilena_ru-RUMoira_en-IEMonica_es-ESNora_nb-NOPaulina_es-MXRishi_en-INSamantha_en-USSara_da-DKSatu_fi-FISin-ji_zh-HKTessa_en-ZAThomas_fr-FRTing-Ting_zh-CNVeena_en-INVictoria_en-USXander_nl-NLYelda_tr-TRYuna_ko-KRYuri_ru-RUZosia_pl-PLZuzana_cs-CZ\",\"isMobile\":false}";
   public int aj_ss;
   public int z1;
   public boolean rst;
   public String lang;
   public boolean fpValCalculated;
   public int mn_sen;
   public int wen;
   public static String api_public_key = "afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq";
   public long tst;
   public int nav_perm;
   public int ke_vel;
   public int o9;
   public int mn_lcl;
   public String sensor_data;
   public int mn_state;
   public int mn_ct;
   public String psub;
   public String loc;
   public static double ver = Double.longBitsToDouble(4610244866546629345L);
   public int pe_cnt;
   public Long startTime;
   public int dme_vel;
   public int fpcf_td;
   public long mn_rt;
   public int me_vel;
   public int ta;
   public String kact;
   public int y1;
   public int plen;
   public String uar;
   public Map mn_lc;
   public static String cs = "0a46G5m17Vrp4o4c";
   public String fpcf_fpValstr;
   public String tact;
   public List mn_tcl;
   public int n_ck;
   public Map mn_ld;
   public int aj_type;
   public boolean pstate;
   public int mn_cd;
   public String mr;
   public boolean bm;
   public String documentURL;
   public int doe_vel;
   public int d3;
   public String mact;
   public String mn_ts;
   public long start_ts;

   public String t(int var1) {
      return Character.toString(var1);
   }

   public int rir(int var1, int var2, int var3, int var4) {
      if (var1 > var2 && var1 <= var3) {
         var1 += var4 % (var3 - var2);
         if (var1 > var3) {
            var1 = var1 - var3 + var2;
         }
      }

      return var1;
   }

   public void updateDocumentUrl(String var1) {
      this.documentURL = var1;
   }

   public String getDocumentUrl() {
      return this.documentURL;
   }

   public static void main(String[] var0) {
      Bmak var1 = new Bmak(new JsonObject("{\"ap\":true,\"bt\":{\"charging\":true,\"chargingTime\":0,\"dischargingTime\":\"Infinity\",\"level\":1,\"onchargingchange\":null,\"onchargingtimechange\":null,\"ondischargingtimechange\":null,\"onlevelchange\":null},\"fonts\":\"4,14,15,16,21,22,23,43,44,47,48,49,50,51\",\"fh\":\"f78bc5f5ba69eb06e56c3827f66c171eb2b27f75\",\"timing\":\"\",\"bp\":\"1038350511,-1979380391,1738406762,749224105\",\"sr\":{\"inner\":[3356,1306],\"outer\":[3356,1417],\"screen\":[0,23],\"pageOffset\":[0,0],\"avail\":[3440,1417],\"size\":[3440,1440],\"client\":[3340,176],\"colorDepth\":24,\"pixelDepth\":24},\"dp\":{\"XDomainRequest\":0,\"createPopup\":0,\"removeEventListener\":1,\"globalStorage\":0,\"openDatabase\":1,\"indexedDB\":1,\"attachEvent\":0,\"ActiveXObject\":0,\"dispatchEvent\":1,\"addBehavior\":0,\"addEventListener\":1,\"detachEvent\":0,\"fireEvent\":0,\"MutationObserver\":1,\"HTMLMenuItemElement\":0,\"Int8Array\":1,\"postMessage\":1,\"querySelector\":1,\"getElementsByClassName\":1,\"images\":1,\"compatMode\":\"CSS1Compat\",\"documentMode\":0,\"all\":1,\"now\":1,\"contextMenu\":0},\"lt\":\"1622868421082-4\",\"ps\":\"true,true\",\"cv\":\"f50fd5bc1e5aa5aab1cd866593c22ef9d61f14e3\",\"fp\":false,\"sp\":false,\"br\":\"Chrome\",\"ieps\":false,\"av\":false,\"b\":1,\"c\":0,\"jsv\":\"1.7\",\"nav\":{\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appName\":\"Netscape\",\"appCodeName\":\"Mozilla\",\"appVersion\":\"5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appMinorVersion\":0,\"product\":\"Gecko\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"vendorSub\":\"\",\"buildID\":0,\"platform\":\"MacIntel\",\"oscpu\":0,\"hardwareConcurrency\":12,\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"systemLanguage\":0,\"userLanguage\":0,\"doNotTrack\":null,\"msDoNotTrack\":0,\"cookieEnabled\":true,\"geolocation\":1,\"vibrate\":1,\"maxTouchPoints\":0,\"webdriver\":false,\"plugins\":[\"Chrome PDF Plugin\",\"Chrome PDF Viewer\",\"Native Client\"]},\"crc\":{\"window.chrome\":{\"app\":{\"isInstalled\":false,\"InstallState\":{\"DISABLED\":\"disabled\",\"INSTALLED\":\"installed\",\"NOT_INSTALLED\":\"not_installed\"},\"RunningState\":{\"CANNOT_RUN\":\"cannot_run\",\"READY_TO_RUN\":\"ready_to_run\",\"RUNNING\":\"running\"}},\"runtime\":{\"OnInstalledReason\":{\"CHROME_UPDATE\":\"chrome_update\",\"INSTALL\":\"install\",\"SHARED_MODULE_UPDATE\":\"shared_module_update\",\"UPDATE\":\"update\"},\"OnRestartRequiredReason\":{\"APP_UPDATE\":\"app_update\",\"OS_UPDATE\":\"os_update\",\"PERIODIC\":\"periodic\"},\"PlatformArch\":{\"ARM\":\"arm\",\"ARM64\":\"arm64\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformNaclArch\":{\"ARM\":\"arm\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformOs\":{\"ANDROID\":\"android\",\"CROS\":\"cros\",\"LINUX\":\"linux\",\"MAC\":\"mac\",\"OPENBSD\":\"openbsd\",\"WIN\":\"win\"},\"RequestUpdateCheckStatus\":{\"NO_UPDATE\":\"no_update\",\"THROTTLED\":\"throttled\",\"UPDATE_AVAILABLE\":\"update_available\"}}}},\"nap\":\"11321144241322243122\",\"fc\":true,\"ua\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"product\":\"Gecko\",\"availWidth\":3440,\"availHeight\":1417,\"width\":3440,\"height\":1440,\"innerHeight\":1306,\"innerWidth\":3356,\"outerWidth\":3356,\"brave\":false,\"windowPerms\":{\"t\":1,\"a\":1,\"e\":0,\"n\":0,\"o\":1,\"m\":1,\"r\":1,\"i\":0,\"c\":1,\"b\":1,\"d\":0,\"s\":1,\"k\":1,\"l\":1},\"_phantom\":0,\"webdriver\":0,\"domAutomation\":0,\"activeXObject\":0,\"callPhantom\":0,\"documentMode\":0,\"isChrome\":0,\"isOnline\":1,\"isOpera\":0,\"hasInstallTrigger\":0,\"hasHTMLElement\":0,\"hasRTCPeerConnection\":1,\"hasMozInnerScreen\":0,\"hasVibrate\":1,\"hasBattery\":1,\"hasForEach\":0,\"hasFileReader\":1,\"deviceOrientation\":\"do_en\",\"deviceMotion\":\"dm_en\",\"touchEvent\":\"t_en\",\"navigatorFasSettings\":30261693,\"sed\":\"0,0,0,0,1,0,0\",\"languages\":\"en-US\",\"pluginsLength\":3,\"colorDepth\":24,\"pixelDepth\":24,\"cookieEnabled\":true,\"javaEnabled\":false,\"doNotTrack\":-1,\"canvasFP1\":\"2130238721\",\"canvasFP2\":\"-955688593\",\"rCFP\":\"-1241881201\",\"rVal\":\"649\",\"pluginInfo\":\",7,8\",\"sessionStorage\":true,\"localStorage\":true,\"indexedDB\":true,\"timezoneOffset\":240,\"webRTC\":true,\"voices\":\"Google Deutsch_de-DEGoogle US English_en-USGoogle UK English Female_en-GBGoogle UK English Male_en-GBGoogle español_es-ESGoogle español de Estados Unidos_es-USGoogle français_fr-FRGoogle हिन्दी_hi-INGoogle Bahasa Indonesia_id-IDGoogle italiano_it-ITGoogle 日本語_ja-JPGoogle 한국의_ko-KRGoogle Nederlands_nl-NLGoogle polski_pl-PLGoogle português do Brasil_pt-BRGoogle русский_ru-RUGoogle 普通话（中国大陆）_zh-CNGoogle 粤語（香港）_zh-HKGoogle 國語（臺灣）_zh-TWAlex_en-USAlice_it-ITAlva_sv-SEAmelie_fr-CAAnna_de-DECarmit_he-ILDamayanti_id-IDDaniel_en-GBDiego_es-AREllen_nl-BEFiona_enFred_en-USIoana_ro-ROJoana_pt-PTJorge_es-ESJuan_es-MXKanya_th-THKaren_en-AUKyoko_ja-JPLaura_sk-SKLekha_hi-INLuca_it-ITLuciana_pt-BRMaged_ar-SAMariska_hu-HUMei-Jia_zh-TWMelina_el-GRMilena_ru-RUMoira_en-IEMonica_es-ESNora_nb-NOPaulina_es-MXRishi_en-INSamantha_en-USSara_da-DKSatu_fi-FISin-ji_zh-HKTessa_en-ZAThomas_fr-FRTing-Ting_zh-CNVeena_en-INVictoria_en-USXander_nl-NLYelda_tr-TRYuna_ko-KRYuri_ru-RUZosia_pl-PLZuzana_cs-CZ\",\"isMobile\":false}"));
      System.out.println(var1.genPageLoadSensor("B5806C529A27B49450855034F916639F~-1~YAAQv8ZMaE4RmCR6AQAA+I9PMAZYxa+iYdpaoiRda9YOn3VPk+Mdmw4yP3VgYRTWmNVLZ678nvJM45v/t3S9Ex5584s1XaH0Y6sBjhxyd2dWs+H33nRlFpJhS+gUy5XBJbi4g74s+pp3H1+LcItNsh/njOtrvW6PTYw+oTI3l1XlQEvz+3KAtd8POBI9GOHS6S+STZJAI3uE5vj4f3lJJ2mGAHwYMuCw0S8Qb5E7j2ECftrQQtzHPI/qRSyt8kajCj02olOJd8DLu6pewJvIZRIAagAo5/XK/htTrV3Gv7e905F5Ht2PS7hC8iS7QojDuM1HlOFSsk9A193eo9NzkgL1xnvX32QnOtWSMCH+eRUc+DMBCMH8cmBL6BTqzv0jZjF28cSXGG448E9RutqOJNB5hYgQNQ==~-1~-1~-1"));
      System.out.println(var1.genSecondSensor("5B2C972C16D759F35448A4A92C43772E~-1~YAAQbBDeFxZsJRt6AQAAwWTqNAZvbCWdOGDboeG/mcPrPOBCCpM+vsidlFFKj5LjbJeKs2TdsjEJuFZpaF4/6zMjxXEnkWvt7fGtG/h6Ag6Kn56s6p4C0BEJlls263qGZaT+UZwDQdZ4OHgRlqzJGpt06ZQdHkqo2YVsj9McZDT9jzf0gME7/rdmNnyWZgA+Pq/jF+Z4lWrGfZS6duyfmXXqz2hAqZo06L5gxdCEZpo05/voyt7WAkmLNWg5GdIr3ny+AaB+Su2XM4O2s+YIjr4WKNgNnnfqt3xj3ur8BiazEoFC8OWvQQgsPBAEF0w2ysuiKNMN3SZ2gsd/zYISFsjDaBlIngQ8VNmavWHu6M/6iHU4UhK2D7uFDQp58KsGFJNsp3AoIVeT4PnMGsmGXpPXBOM7nEJCKTRPCkpui2puvcAEF3j7Fq3F19mRMDZgfg==~-1~||1-PyoliHBRta-500-10-1000-2||~-1"));
   }

   public List get_mn_params_from_abck() {
      ArrayList var1 = new ArrayList();
      String[] var2 = this.abck.split("~");
      if (var2.length >= 5) {
         String var3 = var2[0];
         String var4 = var2[4];
         String[] var5 = var4.split("\\|\\|");
         if (var4.contains("||")) {
            var5 = (String[])Arrays.copyOf(var5, var5.length + 1);
            var5[var5.length - 1] = "";
         }

         if (var5.length > 0) {
            for(int var6 = 0; var6 < var5.length; ++var6) {
               String var7 = var5[var6];
               String[] var8 = var7.split("-");
               if (var8.length >= 5) {
                  int var9 = Integer.parseInt(var8[0]);
                  String var10 = var8[1];
                  int var11 = Integer.parseInt(var8[2]);
                  int var12 = Integer.parseInt(var8[3]);
                  int var13 = Integer.parseInt(var8[4]);
                  int var14 = 1;
                  if (var8.length >= 6) {
                     var14 = Integer.parseInt(var8[5]);
                  }

                  Object[] var15 = new Object[]{var9, var3, var10, var11, var12, var13, var14};
                  if (2 == var14) {
                     var1.add(0, var15);
                  } else {
                     var1.add(var15);
                  }
               }
            }
         }
      }

      return var1;
   }

   public int[] jrs(long var1) {
      int var3 = (int)(Double.longBitsToDouble(4681608360884174848L) * Math.random() + Double.longBitsToDouble(4666723172467343360L));
      String var4 = "" + var1 * (long)var3;
      int var5 = 0;
      ArrayList var6 = new ArrayList();

      for(boolean var7 = var4.length() >= 18; var6.size() < 6; var5 = var7 ? var5 + 3 : var5 + 2) {
         var6.add(Integer.parseInt(var4.substring(var5, var5 + 2)));
      }

      return new int[]{var3, this.cal_dis(var6)};
   }

   public long x2() {
      return this.get_cf_date();
   }

   public String genPageLoadSensor(String var1) {
      this.abck = var1;
      this.bpd();
      ++this.aj_indx;
      this.firstLoad = false;
      return "{\"sensor_data\":\"" + this.sensor_data + "\"}";
   }

   public String data() {
      int var1 = this.device.getColorDepth();
      int var2 = this.device.getPixelDepth();
      boolean var3 = this.device.isCookieEnabled();
      boolean var4 = this.device.isJavaEnabled();
      int var5 = this.device.getDoNotTrack();
      String var6 = "dis";
      String var7 = this.device.getCanvas1();
      String var8 = this.device.getCanvas2();
      return String.join(";", var7, var8, var6, this.device.getPluginInfo(), String.valueOf(this.device.sessionStorageKey()), String.valueOf(this.device.localStorageKey()), String.valueOf(this.device.indexedDBKey()), String.valueOf(this.device.getTimezoneOffset()), String.valueOf(this.device.webrtcKey()), String.valueOf(var1), String.valueOf(var2), String.valueOf(var3), String.valueOf(var4), String.valueOf(var5));
   }

   public void get_browser() {
      this.psub = this.device.getProductSub();
      this.lang = this.device.getLanguage();
      this.prod = this.device.getProduct();
      this.plen = this.device.getPluginLength();
   }

   public void fpVal() {
      this.fpValCalculated = true;
      long var1 = Instant.now().toEpochMilli();
      this.data();
      long var3 = Instant.now().toEpochMilli();
      long var10000 = var3 - var1;
   }

   public void to() {
      int var1 = (int)((double)this.x2() % Double.longBitsToDouble(4711630319722168320L));
      this.d3 = var1;
      int var2 = var1;
      int var3 = Integer.parseInt(Character.toString(51));

      for(int var4 = 0; var4 < 5; ++var4) {
         int var5 = (int)((double)var1 / Math.pow(Double.longBitsToDouble(4621819117588971520L), (double)var4)) % 10;
         int var6 = var5 + 1;
         var2 = this.cc(var5, var2, var6);
      }

      this.o9 = var2 * var3;
   }

   public static String convertToHex(double var0) {
      if (var0 == Double.longBitsToDouble(0L)) {
         return "0";
      } else {
         StringBuilder var2 = new StringBuilder();
         if (var0 < Double.longBitsToDouble(4607182418800017408L) && var0 > Double.longBitsToDouble(-4616189618054758400L)) {
            var2.append("0.");
         }

         for(int var3 = 0; var3 < 16 && var0 != Double.longBitsToDouble(0L); ++var3) {
            double var4 = var0 * Double.longBitsToDouble(4625196817309499392L);
            var0 = var4 - (double)((int)var4);
            var2.append(Integer.toString((int)var4, 16));
         }

         return var2.toString();
      }
   }

   public String get_cookie() {
      this.cookieChkRead(this.abck);
      return this.abck;
   }

   public Object[] mn_get_new_challenge_params(List var1) {
      Integer var2 = null;
      Integer var3 = null;
      Integer var4 = null;
      if (var1 != null && !var1.isEmpty()) {
         for(int var5 = 0; var5 < var1.size(); ++var5) {
            Object[] var6 = (Object[])var1.get(var5);
            if (var6.length > 0) {
               int var7 = (Integer)var6[0];
               String var8 = this.mn_abck + this.start_ts + var6[2];
               int var9 = (Integer)var6[6];

               int var10;
               for(var10 = 0; var10 < this.mn_lcl && 1 == var7 && !((String)this.mn_lc.get(var10)).equals(var8); ++var10) {
               }

               if (var10 == this.mn_lcl) {
                  var2 = var5;
                  if (2 == var9) {
                     var3 = var5;
                  }

                  if (3 == var9) {
                     var4 = var5;
                  }
               }
            }
         }
      }

      if (null != var4) {
         Objects.requireNonNull(this);
      }

      Object[] var10000;
      if (null != var3) {
         Objects.requireNonNull(this);
         var10000 = (Object[])var1.get(var3);
      } else if (null != var2) {
         Objects.requireNonNull(this);
         var10000 = (Object[])var1.get(var2);
      } else {
         var10000 = null;
      }

      return var10000;
   }

   public int bdm(long[] var1, int var2) {
      int var3 = 0;
      long[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         long var7 = var4[var6];
         var3 = var3 << 8 | (int)var7;
         var3 %= var2;
      }

      return var3;
   }

   public long get_cf_date() {
      return Instant.now().toEpochMilli();
   }

   public String bd() {
      ArrayList var1 = new ArrayList();
      byte var2 = 0;
      var1.add(",cpen:" + var2);
      int var3 = this.device.hasActiveXObject();
      var1.add("i1:" + var3);
      int var4 = this.device.getDocumentMode();
      var1.add("dm:" + var4);
      int var5 = this.device.isChrome();
      var1.add("cwen:" + var5);
      int var6 = this.device.isOnline();
      var1.add("non:" + var6);
      int var7 = this.device.isOpera();
      var1.add("opc:" + var7);
      int var8 = this.device.hasInstallTrigger();
      var1.add("fc:" + var8);
      int var9 = this.device.hasHTMLElement();
      var1.add("sc:" + var9);
      int var10 = this.device.hasRTCPeerConnection();
      var1.add("wrc:" + var10);
      int var11 = this.device.hasMozInnerScreen();
      var1.add("isc:" + var11);
      this.d2 = this.z1 / 23;
      int var12 = this.device.hasVibrate();
      var1.add("vib:" + var12);
      int var13 = this.device.hasBattery();
      var1.add("bat:" + var13);
      int var14 = this.device.hasForEach();
      var1.add("x11:" + var14);
      int var15 = this.device.hasFileReader();
      var1.add("x12:" + var15);
      return String.join(",", var1);
   }

   public Devices$Device getDevice() {
      return this.device;
   }

   public void gbrv() {
      this.brv = this.device.isBrave() ? 1 : 0;
   }

   public void calc_fp() {
      this.fpVal();
      this.aj_type = 9;
      this.bpd();
   }

   public void bmisc() {
      this.pen = 0;
      this.wen = 0;
      this.den = 0;
   }

   public String genSecondSensor(String var1) {
      this.abck = var1;
      this.calc_fp();
      return "{\"sensor_data\":\"" + this.sensor_data + "\"}";
   }

   public Bmak() {
      this.pstate = false;
      this.mn_mc_lmt = 10;
      this.mn_r = new HashMap();
      this.mn_mc_lmt = 10;
      this.mn_lc = new HashMap();
      this.mn_ld = new HashMap();
      this.pstate = false;
      this.start_ts = Instant.now().toEpochMilli();
      this.mn_cd = 10000;
      this.mn_mc_indx = 0;
      this.mn_cc = "";
      this.mn_al = new ArrayList();
      this.mn_il = new ArrayList();
      this.mn_tcl = new ArrayList();
      this.mn_lg = new ArrayList();
      this.mn_abck = "";
      this.mn_ts = "";
      this.mn_psn = "";
      this.mn_rt = 0L;
      this.mn_wt = 0;
      this.mn_stout = 1000;
      this.mn_lcl = 0;
      this.mn_state = 0;
      this.mn_sen = 0;
      this.mn_tout = 100;
      this.mn_ct = 1;
      this.device = Devices.random();
      this.to();
      this.bm = true;
      this.ckie = "_abck";
      this.uar = this.getUA();
      this.y1 = 2016;
      this.loc = "";
      this.documentURL = "https://www.yeezysupply.com/";
      this.aj_type = 0;
      this.aj_indx = 0;
      this.ke_vel = 0;
      this.me_vel = 0;
      this.doe_vel = 0;
      this.dme_vel = 0;
      this.te_vel = 0;
      this.pe_vel = 0;
      this.init_time = 0;
      this.fpcf_td = -999999;
      this.ke_cnt = 0;
      this.me_cnt = 0;
      this.pe_cnt = 0;
      this.te_cnt = 0;
      this.ta = 0;
      this.n_ck = 1;
      this.fpcf_rVal = "-1";
      this.fpcf_rCFP = "-1";
      this.fpcf_fpValstr = "-1";
      this.mr = "-1";
      this.firstLoad = true;
      this.sensor_data = "";
      this.informinfo = "";
      this.kact = "";
      this.mact = "";
      this.tact = "";
      this.doact = "";
      this.dmact = "";
      this.pact = "";
      this.vcact = "";
      this.nav_perm = 8;
      this.tst = -1L;
      this.aj_ss = 0;
   }

   public void cookieChkRead(String var1) {
   }

   public void mn_update_challenge_details(Object[] var1) {
      this.mn_sen = (Integer)var1[0];
      this.mn_abck = (String)var1[1];
      this.mn_psn = (String)var1[2];
      this.mn_cd = (Integer)var1[3];
      this.mn_tout = (Integer)var1[4];
      this.mn_stout = (Integer)var1[5];
      this.mn_ct = (Integer)var1[6];
      this.mn_ts = "" + this.start_ts;
      this.mn_cc = this.mn_abck + this.start_ts + this.mn_psn;
   }

   public void genWithNoEvents() {
      this.tst = this.get_cf_date() - this.startTime;
      this.patp();
   }

   public String od(String var1, String var2) {
      int var3 = var2.length();
      ArrayList var4 = new ArrayList();
      if (var3 > 0) {
         for(int var5 = 0; var5 < var1.length(); ++var5) {
            int var6 = Character.codePointAt(var1, var5);
            char var7 = var1.charAt(var5);
            int var8 = Character.codePointAt(var2, var5 % var3);
            var6 = this.rir(var6, 47, 57, var8);
            if (var6 != Character.codePointAt(var1, var5)) {
               var7 = (char)var6;
            }

            var4.add(String.valueOf(var7));
         }

         if (var4.size() > 0) {
            return String.join("", var4);
         }
      }

      return var1;
   }

   public int cc(int var1, int var2, int var3) {
      int var4 = var1 % 4;
      if (var4 == 2) {
         var4 = 3;
      }

      int var5 = 42 + var4;
      if (var5 == 42) {
         return var2 * var3;
      } else {
         return var5 == 43 ? var2 + var3 : var2 - var3;
      }
   }

   public void patp() {
      ++this.aj_ss;
      this.rst = false;
   }

   public String mn_pr() {
      String var10000 = String.join(",", this.mn_al);
      return var10000 + ";" + String.join(",", this.mn_tcl) + ";" + String.join(",", this.mn_il) + ";" + String.join(",", this.mn_lg) + ";";
   }

   public void bpd() {
      long var1 = this.get_cf_date();
      if (this.startTime == null) {
         this.startTime = Instant.now().toEpochMilli() - 1L;
      }

      long var3 = this.get_cf_date() - this.startTime;
      String var5 = this.get_cookie();
      String var6 = this.gd();
      String var7 = this.device.getDeviceOrientation();
      String var8 = this.device.getDeviceMotion();
      String var9 = this.device.getTouchEvent();
      String var10 = var7 + "," + var8 + "," + var9;
      String var11 = this.getforminfo();
      String var12 = this.getDocumentUrl();
      String var13 = this.aj_type + "," + this.aj_indx;
      int var14 = this.ke_vel + this.me_vel + this.doe_vel + this.dme_vel + this.te_vel + this.pe_vel;
      String var10000 = this.t(80);
      String var15 = var10000 + this.t(105) + this.t(90) + this.t(116) + this.t(69);
      int[] var16 = this.jrs(this.startTime);
      long var17 = this.get_cf_date() - this.startTime;
      int var19 = this.d2 / 6;
      long var20 = this.device.getNavigatorFasSettings();
      String[] var22 = new String[]{String.valueOf(this.ke_vel + 1), String.valueOf(this.me_vel + 32), String.valueOf(this.te_vel + 32), String.valueOf(this.doe_vel), String.valueOf(this.dme_vel), String.valueOf(this.pe_vel), String.valueOf(var14), String.valueOf(var3), String.valueOf(this.init_time), String.valueOf(this.startTime), String.valueOf(this.fpcf_td), String.valueOf(this.d2), String.valueOf(this.ke_cnt), String.valueOf(this.me_cnt), String.valueOf(var19), String.valueOf(this.pe_cnt), String.valueOf(this.te_cnt), String.valueOf(var17), String.valueOf(this.ta), String.valueOf(this.n_ck), var5, String.valueOf(this.ab(var5)), this.fpcf_rVal, this.fpcf_rCFP, String.valueOf(var20), var15, String.valueOf(var16[0]), String.valueOf(var16[1])};
      String var23 = String.join(",", var22);
      String var24 = "" + this.ab(this.fpcf_fpValstr);
      String var25 = "";
      String var26 = this.device.getSed();
      String var27 = "";
      String var28 = "";
      String var29 = "";
      this.sensor_data = "1.68-1,2,-94,-100," + var6 + "-1,2,-94,-101," + var10 + "-1,2,-94,-105," + this.informinfo + "-1,2,-94,-102," + var11 + "-1,2,-94,-108," + this.kact + "-1,2,-94,-110," + this.mact + "-1,2,-94,-117," + this.tact + "-1,2,-94,-111," + this.doact + "-1,2,-94,-109," + this.dmact + "-1,2,-94,-114," + this.pact + "-1,2,-94,-103," + this.vcact + "-1,2,-94,-112," + var12 + "-1,2,-94,-115," + var23 + "-1,2,-94,-106," + var13;
      this.sensor_data = this.sensor_data + "-1,2,-94,-119," + this.mr + "-1,2,-94,-122," + var26 + "-1,2,-94,-123," + var27 + "-1,2,-94,-124," + var28 + "-1,2,-94,-126," + var29 + "-1,2,-94,-127," + this.nav_perm;
      long var30 = (long)(24 ^ this.ab(this.sensor_data));
      this.sensor_data = this.sensor_data + "-1,2,-94,-70," + this.fpcf_fpValstr + "-1,2,-94,-80," + var24 + "-1,2,-94,-116," + this.o9 + "-1,2,-94,-118," + var30 + "-1,2,-94,-129," + var25 + "-1,2,-94,-121,";
      String var32 = this.od("0a46G5m17Vrp4o4c", "afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq").substring(0, 16);
      int var33 = (int)((double)this.get_cf_date() / Double.longBitsToDouble(4704985352480227328L));
      long var34 = this.get_cf_date();
      String var36 = var32 + this.od(String.valueOf(var33), var32) + this.sensor_data;
      this.sensor_data = var36 + ";" + (this.get_cf_date() - var1) + ";" + this.tst + ";" + (this.get_cf_date() - var34);
   }

   public long[] mn_s(String var1) {
      long[] var2 = new long[]{1116352408L, 1899447441L, 3049323471L, 3921009573L, 961987163L, 1508970993L, 2453635748L, 2870763221L, 3624381080L, 310598401L, 607225278L, 1426881987L, 1925078388L, 2162078206L, 2614888103L, 3248222580L, 3835390401L, 4022224774L, 264347078L, 604807628L, 770255983L, 1249150122L, 1555081692L, 1996064986L, 2554220882L, 2821834349L, 2952996808L, 3210313671L, 3336571891L, 3584528711L, 113926993L, 338241895L, 666307205L, 773529912L, 1294757372L, 1396182291L, 1695183700L, 1986661051L, 2177026350L, 2456956037L, 2730485921L, 2820302411L, 3259730800L, 3345764771L, 3516065817L, 3600352804L, 4094571909L, 275423344L, 430227734L, 506948616L, 659060556L, 883997877L, 958139571L, 1322822218L, 1537002063L, 1747873779L, 1955562222L, 2024104815L, 2227730452L, 2361852424L, 2428436474L, 2756734187L, 3204031479L, 3329325298L};
      int var3 = 1779033703;
      long var4 = 3144134277L;
      int var6 = 1013904242;
      long var7 = 2773480762L;
      int var9 = 1359893119;
      long var10 = 2600822924L;
      int var12 = 528734635;
      long var13 = 1541459225L;
      int var16 = 8 * var1.length();
      String var15 = var1 + "\u0080";
      double var17 = (double)var15.length() / Double.longBitsToDouble(4616189618054758400L) + Double.longBitsToDouble(4611686018427387904L);
      int var19 = (int)Math.ceil(var17 / Double.longBitsToDouble(4625196817309499392L));
      long[][] var20 = new long[var19][16];

      for(int var21 = 0; var21 < var19; ++var21) {
         try {
            for(int var22 = 0; var22 < 16; ++var22) {
               var20[var21][var22] = (long)(Ww.leftBitwise((long)Character.codePointAt(var15, 64 * var21 + 4 * var22), 24L) | Ww.leftBitwise((long)Character.codePointAt(var15, 64 * var21 + 4 * var22 + 1), 16L) | Ww.leftBitwise((long)Character.codePointAt(var15, 64 * var21 + 4 * var22 + 2), 8L) | Character.codePointAt(var15, 64 * var21 + 4 * var22 + 3));
            }
         } catch (StringIndexOutOfBoundsException var57) {
         }
      }

      double var58 = (double)var16 / Math.pow(Double.longBitsToDouble(4611686018427387904L), Double.longBitsToDouble(4629700416936869888L));
      var20[var19 - 1][14] = (long)Math.floor(var58);
      var20[var19 - 1][15] = (long)var16;

      for(int var23 = 0; var23 < var19; ++var23) {
         HashMap var26 = new HashMap();
         long var27 = (long)var3;
         long var29 = var4;
         long var31 = (long)var6;
         long var33 = var7;
         long var35 = (long)var9;
         long var24 = var10;
         long var37 = (long)var12;
         long var39 = var13;

         for(int var41 = 0; var41 < 64; ++var41) {
            long var42 = 0L;
            long var44;
            if (var41 < 16) {
               var26.put(var41, var20[var23][var41]);
            } else {
               var42 = this.rotate_right((Long)var26.get(var41 - 15), 7) ^ this.rotate_right((Long)var26.get(var41 - 15), 18) ^ (long)Ww.rightTripleBitwise((Long)var26.get(var41 - 15), 3L);
               Long var54 = (Long)var26.get(var41 - 2);
               if (var54 == null) {
                  var54 = 0L;
               }

               var44 = this.rotate_right(var54, 17) ^ this.rotate_right(var54, 19) ^ (long)Ww.rightTripleBitwise(var54, 10L);
               Long var55 = (Long)var26.get(var41 - 16);
               if (var55 == null) {
                  var55 = 0L;
               }

               Long var56 = (Long)var26.get(var41 - 7);
               if (var56 == null) {
                  var56 = 0L;
               }

               var26.put(var41, var55 + var42 + var56 + var44);
            }

            var44 = this.rotate_right(var35, 6) ^ this.rotate_right(var35, 11) ^ this.rotate_right(var35, 25);
            long var46 = var35 & var24 ^ ~var35 & var37;
            long var48 = var39 + var44 + var46 + var2[var41] + (Long)var26.get(var41);
            var42 = this.rotate_right(var27, 2) ^ this.rotate_right(var27, 13) ^ this.rotate_right(var27, 22);
            long var50 = var27 & var29 ^ var27 & var31 ^ var29 & var31;
            long var52 = var42 + var50;
            var39 = var37;
            var37 = var24;
            var24 = var35;
            var35 = var33 + var48;
            var33 = var31;
            var31 = var29;
            var29 = var27;
            var27 = var48 + var52;
         }

         var3 = (int)((long)var3 + var27);
         var4 += var29;
         var6 = (int)((long)var6 + var31);
         var7 += var33;
         var9 = (int)((long)var9 + var35);
         var10 += var24;
         var12 = (int)((long)var12 + var37);
         var13 += var39;
      }

      return new long[]{(long)(Ww.rightBitwise((long)var3, 24L) & 255), (long)(Ww.rightBitwise((long)var3, 16L) & 255), (long)(Ww.rightBitwise((long)var3, 8L) & 255), (long)(255 & var3), (long)(Ww.rightBitwise(var4, 24L) & 255), (long)(Ww.rightBitwise(var4, 16L) & 255), (long)(Ww.rightBitwise(var4, 8L) & 255), 255L & var4, (long)(Ww.rightBitwise((long)var6, 24L) & 255), (long)(Ww.rightBitwise((long)var6, 16L) & 255), (long)(Ww.rightBitwise((long)var6, 8L) & 255), (long)(255 & var6), (long)(Ww.rightBitwise(var7, 24L) & 255), (long)(Ww.rightBitwise(var7, 16L) & 255), (long)(Ww.rightBitwise(var7, 8L) & 255), 255L & var7, (long)(Ww.rightBitwise((long)var9, 24L) & 255), (long)(Ww.rightBitwise((long)var9, 16L) & 255), (long)(Ww.rightBitwise((long)var9, 8L) & 255), (long)(255 & var9), (long)(Ww.rightBitwise(var10, 24L) & 255), (long)(Ww.rightBitwise(var10, 16L) & 255), (long)(Ww.rightBitwise(var10, 8L) & 255), 255L & var10, (long)(Ww.rightBitwise((long)var12, 24L) & 255), (long)(Ww.rightBitwise((long)var12, 16L) & 255), (long)(Ww.rightBitwise((long)var12, 8L) & 255), (long)(255 & var12), (long)(Ww.rightBitwise(var13, 24L) & 255), (long)(Ww.rightBitwise(var13, 16L) & 255), (long)(Ww.rightBitwise(var13, 8L) & 255), 255L & var13};
   }

   public void mn_poll() {
      if (0 == this.mn_state) {
         List var1 = this.get_mn_params_from_abck();
         Object[] var2 = this.mn_get_new_challenge_params(var1);
         if (var2 != null && var2.length != 0) {
            this.mn_update_challenge_details(var2);
            if (this.mn_sen != 0) {
               this.mn_state = 1;
               this.mn_mc_indx = 0;
               this.mn_al = new ArrayList();
               this.mn_il = new ArrayList();
               this.mn_tcl = new ArrayList();
               this.mn_lg = new ArrayList();
               long var3 = this.get_cf_date();
               this.mn_rt = var3 - this.start_ts;
               this.mn_wt = 0;
               this.mn_w();
            }
         }
      }

   }

   public int mn_w() {
      boolean var1 = false;
      int var2 = 0;
      long var3 = 0L;
      String var5 = "";
      long var6 = this.get_cf_date();
      int var8 = this.mn_cd + this.mn_mc_indx;

      while(!var1) {
         var5 = convertToHex(ThreadLocalRandom.current().nextDouble());
         String var9 = this.mn_cc + var8 + var5;
         long[] var10 = this.mn_s(var9);
         if (0 == this.bdm(var10, var8)) {
            var1 = true;
            var3 = this.get_cf_date() - var6;
            this.mn_al.add(var5);
            this.mn_tcl.add("" + var3);
            this.mn_il.add("" + var2);
            if (0 == this.mn_mc_indx) {
               this.mn_lg.add(this.mn_abck);
               this.mn_lg.add(this.mn_ts);
               this.mn_lg.add(this.mn_psn);
               this.mn_lg.add(this.mn_cc);
               this.mn_lg.add("" + this.mn_cd);
               this.mn_lg.add("" + var8);
               this.mn_lg.add(var5);
               this.mn_lg.add(var9);
               this.mn_lg.add((String)Arrays.stream(var10).mapToObj(String::valueOf).collect(Collectors.joining(",")));
               this.mn_lg.add("" + this.mn_rt);
            }
         } else {
            ++var2;
            if (var2 % 1000 == 0 && (var3 = this.get_cf_date() - var6) > (long)this.mn_stout) {
               return this.mn_wt = (int)((long)this.mn_wt + var3);
            }
         }
      }

      ++this.mn_mc_indx;
      int var10000 = this.mn_mc_indx;
      Objects.requireNonNull(this);
      if (var10000 < 10) {
         this.mn_w();
      } else {
         this.mn_mc_indx = 0;
         this.mn_lc.put(this.mn_lcl, this.mn_cc);
         this.mn_ld.put(this.mn_lcl, "" + this.mn_cd);
         ++this.mn_lcl;
         this.mn_state = 0;
         this.mn_lg.add(String.valueOf(this.mn_wt));
         this.mn_lg.add(String.valueOf(this.get_cf_date()));
         this.mn_r.put(this.mn_abck + this.mn_psn, this.mn_pr());
      }

      return -69;
   }

   public String getUA() {
      return this.device.getUserAgent();
   }

   public Map mn_get_current_challenges() {
      List var1 = this.get_mn_params_from_abck();
      HashMap var2 = new HashMap();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.size(); ++var3) {
            Object[] var4 = (Object[])var1.get(var3);
            if (var4.length > 0) {
               String var5 = var4[6].toString();
               String var6;
               if (var4[1] instanceof Integer && var4[2] instanceof Integer) {
                  var6 = "" + ((Integer)var4[1] + (Integer)var4[2]);
               } else {
                  String var10000 = var4[1].toString();
                  var6 = var10000 + var4[2].toString();
               }

               var2.put(var5, var6);
            }
         }
      }

      return var2;
   }

   public void bc() {
      JsonObject var1 = this.device.getWindowPerms();
      int var2 = var1.getInteger("t");
      int var3 = var1.getInteger("a");
      int var4 = var1.getInteger("e");
      int var5 = var1.getInteger("n");
      int var6 = var1.getInteger("o");
      int var7 = var1.getInteger("m");
      int var8 = var1.getInteger("r");
      int var9 = var1.getInteger("i");
      int var10 = var1.getInteger("c");
      int var11 = var1.getInteger("b");
      int var12 = var1.getInteger("d");
      int var13 = var1.getInteger("s");
      int var14 = var1.getInteger("k");
      int var15 = var1.getInteger("l");
      this.xagg = var2 + (var3 << 1) + (var4 << 2) + (var5 << 3) + (var6 << 4) + (var7 << 5) + (var8 << 6) + (var9 << 7) + (var14 << 8) + (var15 << 9) + (var10 << 10) + (var11 << 11) + (var12 << 12) + (var13 << 13);
   }

   public long rotate_right(long var1, int var3) {
      return (long)(Ww.rightTripleBitwise(var1, (long)var3) | Ww.leftBitwise(var1, 32L) - var3);
   }

   public String gd() {
      String var1 = this.uar;
      int var10000 = this.ab(var1);
      String var2 = "" + var10000;
      String var3 = String.valueOf(this.startTime / 2L);
      if (this.startTime % 2L != 0L) {
         var3 = var3 + ".5";
      }

      int var4 = this.device.getScreenAvailWidth();
      int var5 = this.device.getScreenAvailHeight();
      int var6 = this.device.getScreenWidth();
      int var7 = this.device.getScreenHeight();
      int var8 = this.device.getInnerHeight();
      int var9 = this.device.getInnerWidth();
      int var10 = this.device.getOuterWidth();
      this.z1 = (int)(this.startTime / (long)(this.y1 * this.y1));
      double var11 = Math.random();
      int var13 = (int)(Double.longBitsToDouble(4652007308841189376L) * var11 / Double.longBitsToDouble(4611686018427387904L));
      String var14 = "" + var11;
      String var15 = var14.substring(0, 11);
      var14 = var15 + var13;
      this.gbrv();
      this.get_browser();
      this.bc();
      this.bmisc();
      return var1 + ",uaend," + this.xagg + "," + this.psub + "," + this.lang + "," + this.prod + "," + this.plen + "," + this.pen + "," + this.wen + "," + this.den + "," + this.z1 + "," + this.d3 + "," + var4 + "," + var5 + "," + var6 + "," + var7 + "," + var9 + "," + var8 + "," + var10 + "," + this.bd() + "," + var2 + "," + var14 + "," + var3 + "," + this.brv + ",loc:" + this.loc;
   }

   public int ab(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         int var2 = 0;

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            int var4 = Character.codePointAt(var1, var3);
            if (var4 < 128) {
               var2 += var4;
            }
         }

         return var2;
      }
   }

   public Bmak(JsonObject var1) {
      this.mn_r = new HashMap();
      this.mn_mc_lmt = 10;
      this.mn_lc = new HashMap();
      this.mn_ld = new HashMap();
      this.pstate = false;
      this.start_ts = Instant.now().toEpochMilli();
      this.mn_cd = 10000;
      this.mn_mc_indx = 0;
      this.mn_cc = "";
      this.mn_al = new ArrayList();
      this.mn_il = new ArrayList();
      this.mn_tcl = new ArrayList();
      this.mn_lg = new ArrayList();
      this.mn_abck = "";
      this.mn_ts = "";
      this.mn_psn = "";
      this.mn_rt = 0L;
      this.mn_wt = 0;
      this.mn_stout = 1000;
      this.mn_lcl = 0;
      this.mn_state = 0;
      this.mn_sen = 0;
      this.mn_tout = 100;
      this.mn_ct = 1;
      this.device = Devices.genFromJson(var1);
      this.to();
      this.bm = true;
      this.ckie = "_abck";
      this.uar = this.getUA();
      this.y1 = 2016;
      this.loc = "";
      this.documentURL = "https://www.yeezysupply.com/";
      this.aj_type = 0;
      this.aj_indx = 0;
      this.ke_vel = 0;
      this.me_vel = 0;
      this.doe_vel = 0;
      this.dme_vel = 0;
      this.te_vel = 0;
      this.pe_vel = 0;
      this.init_time = 0;
      this.fpcf_td = -999999;
      this.ke_cnt = 0;
      this.me_cnt = 0;
      this.pe_cnt = 0;
      this.te_cnt = 0;
      this.ta = 0;
      this.n_ck = 1;
      this.fpcf_rVal = "-1";
      this.fpcf_rCFP = "-1";
      this.fpcf_fpValstr = "-1";
      this.mr = "-1";
      this.firstLoad = true;
      this.sensor_data = "";
      this.informinfo = "";
      this.kact = "";
      this.mact = "";
      this.tact = "";
      this.doact = "";
      this.dmact = "";
      this.pact = "";
      this.vcact = "";
      this.nav_perm = 8;
      this.tst = -1L;
      this.aj_ss = 0;
   }

   public String getforminfo() {
      String var1 = "";
      String var2 = "";
      this.ins = var1;
      this.cns = var1;
      return var2;
   }

   public int cal_dis(List var1) {
      int var2 = (Integer)var1.get(0) - (Integer)var1.get(1);
      int var3 = (Integer)var1.get(2) - (Integer)var1.get(3);
      int var4 = (Integer)var1.get(4) - (Integer)var1.get(5);
      double var5 = Math.sqrt((double)(var2 * var2 + var3 * var3 + var4 * var4));
      return (int)var5;
   }
}

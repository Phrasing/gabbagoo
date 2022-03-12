/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.sensor;

import io.trickle.task.antibot.impl.akamai.Devices;
import io.trickle.task.antibot.impl.akamai.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.captcha.util.Ww;
import io.vertx.core.json.JsonObject;
import java.lang.invoke.CallSite;
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
    public String cns;
    public Long startTime;
    public int d3;
    public String sensor_data;
    public List<String> mn_lg;
    public int mn_tout;
    public int z1;
    public int plen;
    public List<String> mn_il;
    public String mact;
    public int d2;
    public String loc;
    public int den;
    public boolean rst;
    public String tact;
    public String doact;
    public Map<Integer, String> mn_ld;
    public int te_vel;
    public int aj_indx;
    public int mn_mc_indx;
    public String mr;
    public String mn_abck;
    public int fpcf_td;
    public int me_cnt;
    public int dme_vel;
    public String fpcf_rCFP;
    public int mn_cd;
    public String ins;
    public boolean bm;
    public String fpcf_rVal;
    public int mn_wt;
    public String vcact;
    public int pen;
    public int te_cnt;
    public int doe_vel;
    public boolean fpValCalculated;
    public String mn_ts;
    public String kact;
    public static String personal_dwayn_json;
    public long mn_rt;
    public int mn_ct;
    public Map<Integer, String> mn_lc;
    public boolean pstate;
    public String mn_psn;
    public Devices$Device device;
    public static double ver;
    public int pe_vel;
    public int wen;
    public String lang;
    public String dmact;
    public int init_time;
    public long tst;
    public int nav_perm;
    public String documentURL;
    public static String api_public_key;
    public int y1;
    public int aj_type;
    public String mn_cc;
    public List<String> mn_al;
    public String fpcf_fpValstr;
    public int mn_lcl;
    public long start_ts;
    public int ta;
    public int pe_cnt;
    public int xagg;
    public String ckie;
    public int mn_mc_lmt;
    public int o9;
    public String uar;
    public int mn_sen;
    public int brv;
    public Map<String, String> mn_r;
    public boolean firstLoad;
    public String informinfo;
    public int mn_state;
    public String pact;
    public int aj_ss;
    public int me_vel;
    public String psub;
    public int mn_stout;
    public int ke_vel;
    public int ke_cnt;
    public String abck;
    public List<String> mn_tcl;
    public int n_ck;
    public String prod;
    public static String cs;

    public void bpd() {
        long l = this.get_cf_date();
        if (this.startTime == null) {
            this.startTime = Instant.now().toEpochMilli() - 1L;
        }
        long l2 = this.get_cf_date() - this.startTime;
        String string = this.get_cookie();
        String string2 = this.gd();
        String string3 = this.device.getDeviceOrientation();
        String string4 = this.device.getDeviceMotion();
        String string5 = this.device.getTouchEvent();
        String string6 = string3 + "," + string4 + "," + string5;
        String string7 = this.getforminfo();
        String string8 = this.getDocumentUrl();
        String string9 = this.aj_type + "," + this.aj_indx;
        int n = this.ke_vel + this.me_vel + this.doe_vel + this.dme_vel + this.te_vel + this.pe_vel;
        String string10 = this.t(80) + this.t(105) + this.t(90) + this.t(116) + this.t(69);
        int[] nArray = this.jrs(this.startTime);
        long l3 = this.get_cf_date() - this.startTime;
        int n2 = this.d2 / 6;
        long l4 = this.device.getNavigatorFasSettings();
        CharSequence[] charSequenceArray = new String[]{String.valueOf(this.ke_vel + 1), String.valueOf(this.me_vel + 32), String.valueOf(this.te_vel + 32), String.valueOf(this.doe_vel), String.valueOf(this.dme_vel), String.valueOf(this.pe_vel), String.valueOf(n), String.valueOf(l2), String.valueOf(this.init_time), String.valueOf(this.startTime), String.valueOf(this.fpcf_td), String.valueOf(this.d2), String.valueOf(this.ke_cnt), String.valueOf(this.me_cnt), String.valueOf(n2), String.valueOf(this.pe_cnt), String.valueOf(this.te_cnt), String.valueOf(l3), String.valueOf(this.ta), String.valueOf(this.n_ck), string, String.valueOf(this.ab(string)), this.fpcf_rVal, this.fpcf_rCFP, String.valueOf(l4), string10, String.valueOf(nArray[0]), String.valueOf(nArray[1])};
        String string11 = String.join((CharSequence)",", charSequenceArray);
        String string12 = "" + this.ab(this.fpcf_fpValstr);
        String string13 = "";
        String string14 = this.device.getSed();
        String string15 = "";
        String string16 = "";
        String string17 = "";
        this.sensor_data = "1.68-1,2,-94,-100," + string2 + "-1,2,-94,-101," + string6 + "-1,2,-94,-105," + this.informinfo + "-1,2,-94,-102," + string7 + "-1,2,-94,-108," + this.kact + "-1,2,-94,-110," + this.mact + "-1,2,-94,-117," + this.tact + "-1,2,-94,-111," + this.doact + "-1,2,-94,-109," + this.dmact + "-1,2,-94,-114," + this.pact + "-1,2,-94,-103," + this.vcact + "-1,2,-94,-112," + string8 + "-1,2,-94,-115," + string11 + "-1,2,-94,-106," + string9;
        this.sensor_data = this.sensor_data + "-1,2,-94,-119," + this.mr + "-1,2,-94,-122," + string14 + "-1,2,-94,-123," + string15 + "-1,2,-94,-124," + string16 + "-1,2,-94,-126," + string17 + "-1,2,-94,-127," + this.nav_perm;
        long l5 = 0x18 ^ this.ab(this.sensor_data);
        this.sensor_data = this.sensor_data + "-1,2,-94,-70," + this.fpcf_fpValstr + "-1,2,-94,-80," + string12 + "-1,2,-94,-116," + this.o9 + "-1,2,-94,-118," + l5 + "-1,2,-94,-129," + string13 + "-1,2,-94,-121,";
        String string18 = this.od("0a46G5m17Vrp4o4c", "afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq").substring(0, 16);
        int n3 = (int)((double)this.get_cf_date() / Double.longBitsToDouble(4704985352480227328L));
        long l6 = this.get_cf_date();
        String string19 = string18 + this.od(String.valueOf(n3), string18) + this.sensor_data;
        this.sensor_data = string19 + ";" + (this.get_cf_date() - l) + ";" + this.tst + ";" + (this.get_cf_date() - l6);
    }

    public void patp() {
        ++this.aj_ss;
        this.rst = false;
    }

    public void to() {
        int n;
        this.d3 = n = (int)((double)this.x2() % Double.longBitsToDouble(4711630319722168320L));
        int n2 = n;
        int n3 = Integer.parseInt(Character.toString(51));
        int n4 = 0;
        while (true) {
            if (n4 >= 5) {
                this.o9 = n2 * n3;
                return;
            }
            int n5 = (int)((double)n / Math.pow(Double.longBitsToDouble(0x4024000000000000L), n4)) % 10;
            int n6 = n5 + 1;
            n2 = this.cc(n5, n2, n6);
            ++n4;
        }
    }

    public int rir(int n, int n2, int n3, int n4) {
        if (n <= n2) return n;
        if (n > n3) return n;
        if ((n += n4 % (n3 - n2)) <= n3) return n;
        return n - n3 + n2;
    }

    public long get_cf_date() {
        return Instant.now().toEpochMilli();
    }

    public void calc_fp() {
        this.fpVal();
        this.aj_type = 9;
        this.bpd();
    }

    public int[] jrs(long l) {
        boolean bl;
        int n = (int)(Double.longBitsToDouble(4681608360884174848L) * Math.random() + Double.longBitsToDouble(4666723172467343360L));
        String string = "" + l * (long)n;
        int n2 = 0;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        boolean bl2 = bl = string.length() >= 18;
        while (arrayList.size() < 6) {
            arrayList.add(Integer.parseInt(string.substring(n2, n2 + 2)));
            n2 = bl ? n2 + 3 : n2 + 2;
        }
        return new int[]{n, this.cal_dis(arrayList)};
    }

    public String get_cookie() {
        this.cookieChkRead(this.abck);
        return this.abck;
    }

    public static String convertToHex(double d) {
        if (d == Double.longBitsToDouble(0L)) {
            return "0";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (d < Double.longBitsToDouble(0x3FF0000000000000L) && d > Double.longBitsToDouble(-4616189618054758400L)) {
            stringBuilder.append("0.");
        }
        int n = 0;
        while (n < 16) {
            if (d == Double.longBitsToDouble(0L)) return stringBuilder.toString();
            double d2 = d * Double.longBitsToDouble(0x4030000000000000L);
            d = d2 - (double)((int)d2);
            stringBuilder.append(Integer.toString((int)d2, 16));
            ++n;
        }
        return stringBuilder.toString();
    }

    public int bdm(long[] lArray, int n) {
        int n2 = 0;
        long[] lArray2 = lArray;
        int n3 = lArray2.length;
        int n4 = 0;
        while (n4 < n3) {
            long l = lArray2[n4];
            n2 = n2 << 8 | (int)l;
            n2 %= n;
            ++n4;
        }
        return n2;
    }

    public int cal_dis(List list) {
        int n = (Integer)list.get(0) - (Integer)list.get(1);
        int n2 = (Integer)list.get(2) - (Integer)list.get(3);
        int n3 = (Integer)list.get(4) - (Integer)list.get(5);
        double d = Math.sqrt(n * n + n2 * n2 + n3 * n3);
        return (int)d;
    }

    public String getUA() {
        return this.device.getUserAgent();
    }

    public Bmak(JsonObject jsonObject) {
        this.mn_r = new HashMap<String, String>();
        this.mn_mc_lmt = 10;
        this.mn_lc = new HashMap<Integer, String>();
        this.mn_ld = new HashMap<Integer, String>();
        this.pstate = false;
        this.start_ts = Instant.now().toEpochMilli();
        this.mn_cd = 10000;
        this.mn_mc_indx = 0;
        this.mn_cc = "";
        this.mn_al = new ArrayList<String>();
        this.mn_il = new ArrayList<String>();
        this.mn_tcl = new ArrayList<String>();
        this.mn_lg = new ArrayList<String>();
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
        this.device = Devices.genFromJson(jsonObject);
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

    public String data() {
        int n = this.device.getColorDepth();
        int n2 = this.device.getPixelDepth();
        boolean bl = this.device.isCookieEnabled();
        boolean bl2 = this.device.isJavaEnabled();
        int n3 = this.device.getDoNotTrack();
        String string = "dis";
        String string2 = this.device.getCanvas1();
        String string3 = this.device.getCanvas2();
        return String.join((CharSequence)";", string2, string3, string, this.device.getPluginInfo(), String.valueOf(this.device.sessionStorageKey()), String.valueOf(this.device.localStorageKey()), String.valueOf(this.device.indexedDBKey()), String.valueOf(this.device.getTimezoneOffset()), String.valueOf(this.device.webrtcKey()), String.valueOf(n), String.valueOf(n2), String.valueOf(bl), String.valueOf(bl2), String.valueOf(n3));
    }

    public Devices$Device getDevice() {
        return this.device;
    }

    public void cookieChkRead(String string) {
    }

    public int cc(int n, int n2, int n3) {
        int n4;
        int n5 = n % 4;
        if (n5 == 2) {
            n5 = 3;
        }
        if ((n4 = 42 + n5) == 42) {
            return n2 * n3;
        }
        if (n4 != 43) return n2 - n3;
        return n2 + n3;
    }

    public String genSecondSensor(String string) {
        this.abck = string;
        this.calc_fp();
        return "{\"sensor_data\":\"" + this.sensor_data + "\"}";
    }

    public String gd() {
        String string = this.uar;
        String string2 = "" + this.ab(string);
        Object object = String.valueOf(this.startTime / 2L);
        if (this.startTime % 2L != 0L) {
            object = (String)object + ".5";
        }
        int n = this.device.getScreenAvailWidth();
        int n2 = this.device.getScreenAvailHeight();
        int n3 = this.device.getScreenWidth();
        int n4 = this.device.getScreenHeight();
        int n5 = this.device.getInnerHeight();
        int n6 = this.device.getInnerWidth();
        int n7 = this.device.getOuterWidth();
        this.z1 = (int)(this.startTime / (long)(this.y1 * this.y1));
        double d = Math.random();
        int n8 = (int)(Double.longBitsToDouble(4652007308841189376L) * d / Double.longBitsToDouble(0x4000000000000000L));
        String string3 = "" + d;
        string3 = string3.substring(0, 11) + n8;
        this.gbrv();
        this.get_browser();
        this.bc();
        this.bmisc();
        return string + ",uaend," + this.xagg + "," + this.psub + "," + this.lang + "," + this.prod + "," + this.plen + "," + this.pen + "," + this.wen + "," + this.den + "," + this.z1 + "," + this.d3 + "," + n + "," + n2 + "," + n3 + "," + n4 + "," + n6 + "," + n5 + "," + n7 + "," + this.bd() + "," + string2 + "," + string3 + "," + (String)object + "," + this.brv + ",loc:" + this.loc;
    }

    public String bd() {
        ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
        int n = 0;
        arrayList.add((CallSite)((Object)(",cpen:" + n)));
        int n2 = this.device.hasActiveXObject();
        arrayList.add((CallSite)((Object)("i1:" + n2)));
        int n3 = this.device.getDocumentMode();
        arrayList.add((CallSite)((Object)("dm:" + n3)));
        int n4 = this.device.isChrome();
        arrayList.add((CallSite)((Object)("cwen:" + n4)));
        int n5 = this.device.isOnline();
        arrayList.add((CallSite)((Object)("non:" + n5)));
        int n6 = this.device.isOpera();
        arrayList.add((CallSite)((Object)("opc:" + n6)));
        int n7 = this.device.hasInstallTrigger();
        arrayList.add((CallSite)((Object)("fc:" + n7)));
        int n8 = this.device.hasHTMLElement();
        arrayList.add((CallSite)((Object)("sc:" + n8)));
        int n9 = this.device.hasRTCPeerConnection();
        arrayList.add((CallSite)((Object)("wrc:" + n9)));
        int n10 = this.device.hasMozInnerScreen();
        arrayList.add((CallSite)((Object)("isc:" + n10)));
        this.d2 = this.z1 / 23;
        int n11 = this.device.hasVibrate();
        arrayList.add((CallSite)((Object)("vib:" + n11)));
        int n12 = this.device.hasBattery();
        arrayList.add((CallSite)((Object)("bat:" + n12)));
        int n13 = this.device.hasForEach();
        arrayList.add((CallSite)((Object)("x11:" + n13)));
        int n14 = this.device.hasFileReader();
        arrayList.add((CallSite)((Object)("x12:" + n14)));
        return String.join((CharSequence)",", arrayList);
    }

    public void updateDocumentUrl(String string) {
        this.documentURL = string;
    }

    public void mn_poll() {
        if (0 != this.mn_state) return;
        List list = this.get_mn_params_from_abck();
        Object[] objectArray = this.mn_get_new_challenge_params(list);
        if (objectArray == null) return;
        if (objectArray.length == 0) return;
        this.mn_update_challenge_details(objectArray);
        if (this.mn_sen == 0) return;
        this.mn_state = 1;
        this.mn_mc_indx = 0;
        this.mn_al = new ArrayList<String>();
        this.mn_il = new ArrayList<String>();
        this.mn_tcl = new ArrayList<String>();
        this.mn_lg = new ArrayList<String>();
        long l = this.get_cf_date();
        this.mn_rt = l - this.start_ts;
        this.mn_wt = 0;
        this.mn_w();
    }

    public String genPageLoadSensor(String string) {
        this.abck = string;
        this.bpd();
        ++this.aj_indx;
        this.firstLoad = false;
        return "{\"sensor_data\":\"" + this.sensor_data + "\"}";
    }

    public List get_mn_params_from_abck() {
        ArrayList<Object[]> arrayList = new ArrayList<Object[]>();
        String[] stringArray = this.abck.split("~");
        if (stringArray.length < 5) return arrayList;
        String string = stringArray[0];
        String string2 = stringArray[4];
        String[] stringArray2 = string2.split("\\|\\|");
        if (string2.contains("||")) {
            stringArray2 = Arrays.copyOf(stringArray2, stringArray2.length + 1);
            stringArray2[stringArray2.length - 1] = "";
        }
        if (stringArray2.length <= 0) return arrayList;
        int n = 0;
        while (n < stringArray2.length) {
            String string3 = stringArray2[n];
            String[] stringArray3 = string3.split("-");
            if (stringArray3.length >= 5) {
                int n2 = Integer.parseInt(stringArray3[0]);
                String string4 = stringArray3[1];
                int n3 = Integer.parseInt(stringArray3[2]);
                int n4 = Integer.parseInt(stringArray3[3]);
                int n5 = Integer.parseInt(stringArray3[4]);
                int n6 = 1;
                if (stringArray3.length >= 6) {
                    n6 = Integer.parseInt(stringArray3[5]);
                }
                Object[] objectArray = new Object[]{n2, string, string4, n3, n4, n5, n6};
                if (2 == n6) {
                    arrayList.add(0, objectArray);
                } else {
                    arrayList.add(objectArray);
                }
            }
            ++n;
        }
        return arrayList;
    }

    public void gbrv() {
        this.brv = this.device.isBrave() ? 1 : 0;
    }

    public void get_browser() {
        this.psub = this.device.getProductSub();
        this.lang = this.device.getLanguage();
        this.prod = this.device.getProduct();
        this.plen = this.device.getPluginLength();
    }

    static {
        api_public_key = "afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq";
        personal_dwayn_json = "{\"ap\":true,\"bt\":{\"charging\":true,\"chargingTime\":0,\"dischargingTime\":\"Infinity\",\"level\":1,\"onchargingchange\":null,\"onchargingtimechange\":null,\"ondischargingtimechange\":null,\"onlevelchange\":null},\"fonts\":\"4,14,15,16,21,22,23,43,44,47,48,49,50,51\",\"fh\":\"f78bc5f5ba69eb06e56c3827f66c171eb2b27f75\",\"timing\":\"\",\"bp\":\"1038350511,-1979380391,1738406762,749224105\",\"sr\":{\"inner\":[3356,1306],\"outer\":[3356,1417],\"screen\":[0,23],\"pageOffset\":[0,0],\"avail\":[3440,1417],\"size\":[3440,1440],\"client\":[3340,176],\"colorDepth\":24,\"pixelDepth\":24},\"dp\":{\"XDomainRequest\":0,\"createPopup\":0,\"removeEventListener\":1,\"globalStorage\":0,\"openDatabase\":1,\"indexedDB\":1,\"attachEvent\":0,\"ActiveXObject\":0,\"dispatchEvent\":1,\"addBehavior\":0,\"addEventListener\":1,\"detachEvent\":0,\"fireEvent\":0,\"MutationObserver\":1,\"HTMLMenuItemElement\":0,\"Int8Array\":1,\"postMessage\":1,\"querySelector\":1,\"getElementsByClassName\":1,\"images\":1,\"compatMode\":\"CSS1Compat\",\"documentMode\":0,\"all\":1,\"now\":1,\"contextMenu\":0},\"lt\":\"1622868421082-4\",\"ps\":\"true,true\",\"cv\":\"f50fd5bc1e5aa5aab1cd866593c22ef9d61f14e3\",\"fp\":false,\"sp\":false,\"br\":\"Chrome\",\"ieps\":false,\"av\":false,\"b\":1,\"c\":0,\"jsv\":\"1.7\",\"nav\":{\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appName\":\"Netscape\",\"appCodeName\":\"Mozilla\",\"appVersion\":\"5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appMinorVersion\":0,\"product\":\"Gecko\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"vendorSub\":\"\",\"buildID\":0,\"platform\":\"MacIntel\",\"oscpu\":0,\"hardwareConcurrency\":12,\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"systemLanguage\":0,\"userLanguage\":0,\"doNotTrack\":null,\"msDoNotTrack\":0,\"cookieEnabled\":true,\"geolocation\":1,\"vibrate\":1,\"maxTouchPoints\":0,\"webdriver\":false,\"plugins\":[\"Chrome PDF Plugin\",\"Chrome PDF Viewer\",\"Native Client\"]},\"crc\":{\"window.chrome\":{\"app\":{\"isInstalled\":false,\"InstallState\":{\"DISABLED\":\"disabled\",\"INSTALLED\":\"installed\",\"NOT_INSTALLED\":\"not_installed\"},\"RunningState\":{\"CANNOT_RUN\":\"cannot_run\",\"READY_TO_RUN\":\"ready_to_run\",\"RUNNING\":\"running\"}},\"runtime\":{\"OnInstalledReason\":{\"CHROME_UPDATE\":\"chrome_update\",\"INSTALL\":\"install\",\"SHARED_MODULE_UPDATE\":\"shared_module_update\",\"UPDATE\":\"update\"},\"OnRestartRequiredReason\":{\"APP_UPDATE\":\"app_update\",\"OS_UPDATE\":\"os_update\",\"PERIODIC\":\"periodic\"},\"PlatformArch\":{\"ARM\":\"arm\",\"ARM64\":\"arm64\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformNaclArch\":{\"ARM\":\"arm\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformOs\":{\"ANDROID\":\"android\",\"CROS\":\"cros\",\"LINUX\":\"linux\",\"MAC\":\"mac\",\"OPENBSD\":\"openbsd\",\"WIN\":\"win\"},\"RequestUpdateCheckStatus\":{\"NO_UPDATE\":\"no_update\",\"THROTTLED\":\"throttled\",\"UPDATE_AVAILABLE\":\"update_available\"}}}},\"nap\":\"11321144241322243122\",\"fc\":true,\"ua\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"product\":\"Gecko\",\"availWidth\":3440,\"availHeight\":1417,\"width\":3440,\"height\":1440,\"innerHeight\":1306,\"innerWidth\":3356,\"outerWidth\":3356,\"brave\":false,\"windowPerms\":{\"t\":1,\"a\":1,\"e\":0,\"n\":0,\"o\":1,\"m\":1,\"r\":1,\"i\":0,\"c\":1,\"b\":1,\"d\":0,\"s\":1,\"k\":1,\"l\":1},\"_phantom\":0,\"webdriver\":0,\"domAutomation\":0,\"activeXObject\":0,\"callPhantom\":0,\"documentMode\":0,\"isChrome\":0,\"isOnline\":1,\"isOpera\":0,\"hasInstallTrigger\":0,\"hasHTMLElement\":0,\"hasRTCPeerConnection\":1,\"hasMozInnerScreen\":0,\"hasVibrate\":1,\"hasBattery\":1,\"hasForEach\":0,\"hasFileReader\":1,\"deviceOrientation\":\"do_en\",\"deviceMotion\":\"dm_en\",\"touchEvent\":\"t_en\",\"navigatorFasSettings\":30261693,\"sed\":\"0,0,0,0,1,0,0\",\"languages\":\"en-US\",\"pluginsLength\":3,\"colorDepth\":24,\"pixelDepth\":24,\"cookieEnabled\":true,\"javaEnabled\":false,\"doNotTrack\":-1,\"canvasFP1\":\"2130238721\",\"canvasFP2\":\"-955688593\",\"rCFP\":\"-1241881201\",\"rVal\":\"649\",\"pluginInfo\":\",7,8\",\"sessionStorage\":true,\"localStorage\":true,\"indexedDB\":true,\"timezoneOffset\":240,\"webRTC\":true,\"voices\":\"Google Deutsch_de-DEGoogle US English_en-USGoogle UK English Female_en-GBGoogle UK English Male_en-GBGoogle espa\u00f1ol_es-ESGoogle espa\u00f1ol de Estados Unidos_es-USGoogle fran\u00e7ais_fr-FRGoogle \u0939\u093f\u0928\u094d\u0926\u0940_hi-INGoogle Bahasa Indonesia_id-IDGoogle italiano_it-ITGoogle \u65e5\u672c\u8a9e_ja-JPGoogle \ud55c\uad6d\uc758_ko-KRGoogle Nederlands_nl-NLGoogle polski_pl-PLGoogle portugu\u00eas do Brasil_pt-BRGoogle \u0440\u0443\u0441\u0441\u043a\u0438\u0439_ru-RUGoogle\u00a0\u666e\u901a\u8bdd\uff08\u4e2d\u56fd\u5927\u9646\uff09_zh-CNGoogle\u00a0\u7ca4\u8a9e\uff08\u9999\u6e2f\uff09_zh-HKGoogle \u570b\u8a9e\uff08\u81fa\u7063\uff09_zh-TWAlex_en-USAlice_it-ITAlva_sv-SEAmelie_fr-CAAnna_de-DECarmit_he-ILDamayanti_id-IDDaniel_en-GBDiego_es-AREllen_nl-BEFiona_enFred_en-USIoana_ro-ROJoana_pt-PTJorge_es-ESJuan_es-MXKanya_th-THKaren_en-AUKyoko_ja-JPLaura_sk-SKLekha_hi-INLuca_it-ITLuciana_pt-BRMaged_ar-SAMariska_hu-HUMei-Jia_zh-TWMelina_el-GRMilena_ru-RUMoira_en-IEMonica_es-ESNora_nb-NOPaulina_es-MXRishi_en-INSamantha_en-USSara_da-DKSatu_fi-FISin-ji_zh-HKTessa_en-ZAThomas_fr-FRTing-Ting_zh-CNVeena_en-INVictoria_en-USXander_nl-NLYelda_tr-TRYuna_ko-KRYuri_ru-RUZosia_pl-PLZuzana_cs-CZ\",\"isMobile\":false}";
        ver = Double.longBitsToDouble(4610244866546629345L);
        cs = "0a46G5m17Vrp4o4c";
    }

    public long[] mn_s(String string) {
        long[] lArray = new long[]{1116352408L, 1899447441L, 3049323471L, 3921009573L, 961987163L, 1508970993L, 2453635748L, 2870763221L, 3624381080L, 310598401L, 607225278L, 1426881987L, 1925078388L, 2162078206L, 2614888103L, 3248222580L, 3835390401L, 4022224774L, 264347078L, 604807628L, 770255983L, 1249150122L, 1555081692L, 1996064986L, 2554220882L, 2821834349L, 2952996808L, 3210313671L, 3336571891L, 3584528711L, 113926993L, 338241895L, 666307205L, 773529912L, 1294757372L, 1396182291L, 1695183700L, 1986661051L, 2177026350L, 2456956037L, 2730485921L, 2820302411L, 3259730800L, 3345764771L, 3516065817L, 3600352804L, 4094571909L, 275423344L, 430227734L, 506948616L, 659060556L, 883997877L, 958139571L, 1322822218L, 1537002063L, 1747873779L, 1955562222L, 2024104815L, 2227730452L, 2361852424L, 2428436474L, 2756734187L, 3204031479L, 3329325298L};
        int n = 1779033703;
        long l = 3144134277L;
        int n2 = 1013904242;
        long l2 = 2773480762L;
        int n3 = 1359893119;
        long l3 = 2600822924L;
        int n4 = 528734635;
        long l4 = 1541459225L;
        Object object = string;
        int n5 = 8 * ((String)object).length();
        object = (String)object + "\u0080";
        double d = (double)((String)object).length() / Double.longBitsToDouble(0x4010000000000000L) + Double.longBitsToDouble(0x4000000000000000L);
        int n6 = (int)Math.ceil(d / Double.longBitsToDouble(0x4030000000000000L));
        long[][] lArray2 = new long[n6][16];
        for (int i = 0; i < n6; ++i) {
            try {
                for (int j = 0; j < 16; ++j) {
                    lArray2[i][j] = Ww.leftBitwise(Character.codePointAt((CharSequence)object, 64 * i + 4 * j), 24L) | Ww.leftBitwise(Character.codePointAt((CharSequence)object, 64 * i + 4 * j + 1), 16L) | Ww.leftBitwise(Character.codePointAt((CharSequence)object, 64 * i + 4 * j + 2), 8L) | Character.codePointAt((CharSequence)object, 64 * i + 4 * j + 3);
                }
                continue;
            }
            catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                // empty catch block
            }
        }
        double d2 = (double)n5 / Math.pow(Double.longBitsToDouble(0x4000000000000000L), Double.longBitsToDouble(0x4040000000000000L));
        lArray2[n6 - 1][14] = (long)Math.floor(d2);
        lArray2[n6 - 1][15] = n5;
        int n7 = 0;
        while (n7 < n6) {
            HashMap<Integer, Long> hashMap = new HashMap<Integer, Long>();
            long l5 = n;
            long l6 = l;
            long l7 = n2;
            long l8 = l2;
            long l9 = n3;
            long l10 = l3;
            long l11 = n4;
            long l12 = l4;
            for (int i = 0; i < 64; ++i) {
                long l13;
                long l14 = 0L;
                if (i < 16) {
                    hashMap.put(i, lArray2[n7][i]);
                } else {
                    Long l15;
                    l14 = this.rotate_right((Long)hashMap.get(i - 15), 7) ^ this.rotate_right((Long)hashMap.get(i - 15), 18) ^ (long)Ww.rightTripleBitwise((Long)hashMap.get(i - 15), 3L);
                    Long l16 = (Long)hashMap.get(i - 2);
                    if (l16 == null) {
                        l16 = 0L;
                    }
                    l13 = this.rotate_right(l16, 17) ^ this.rotate_right(l16, 19) ^ (long)Ww.rightTripleBitwise(l16, 10L);
                    Long l17 = (Long)hashMap.get(i + 16 + 1);
                    if (l17 == null) {
                        l17 = 0L;
                    }
                    if ((l15 = (Long)hashMap.get(i - 7)) == null) {
                        l15 = 0L;
                    }
                    hashMap.put(i, l17 + l14 + l15 + l13);
                }
                l13 = this.rotate_right(l9, 6) ^ this.rotate_right(l9, 11) ^ this.rotate_right(l9, 25);
                long l18 = l9 & l10 ^ (l9 ^ 0xFFFFFFFFFFFFFFFFL) & l11;
                long l19 = l12 + l13 + l18 + lArray[i] + (Long)hashMap.get(i);
                l14 = this.rotate_right(l5, 2) ^ this.rotate_right(l5, 13) ^ this.rotate_right(l5, 22);
                long l20 = l5 & l6 ^ l5 & l7 ^ l6 & l7;
                long l21 = l14 + l20;
                l12 = l11;
                l11 = l10;
                l10 = l9;
                l9 = l8 + l19;
                l8 = l7;
                l7 = l6;
                l6 = l5;
                l5 = l19 + l21;
            }
            n = (int)((long)n + l5);
            l += l6;
            n2 = (int)((long)n2 + l7);
            l2 += l8;
            n3 = (int)((long)n3 + l9);
            l3 += l10;
            n4 = (int)((long)n4 + l11);
            l4 += l12;
            ++n7;
        }
        return new long[]{Ww.rightBitwise(n, 24L) & 0xFF, Ww.rightBitwise(n, 16L) & 0xFF, Ww.rightBitwise(n, 8L) & 0xFF, 0xFF & n, Ww.rightBitwise(l, 24L) & 0xFF, Ww.rightBitwise(l, 16L) & 0xFF, Ww.rightBitwise(l, 8L) & 0xFF, 0xFFL & l, Ww.rightBitwise(n2, 24L) & 0xFF, Ww.rightBitwise(n2, 16L) & 0xFF, Ww.rightBitwise(n2, 8L) & 0xFF, 0xFF & n2, Ww.rightBitwise(l2, 24L) & 0xFF, Ww.rightBitwise(l2, 16L) & 0xFF, Ww.rightBitwise(l2, 8L) & 0xFF, 0xFFL & l2, Ww.rightBitwise(n3, 24L) & 0xFF, Ww.rightBitwise(n3, 16L) & 0xFF, Ww.rightBitwise(n3, 8L) & 0xFF, 0xFF & n3, Ww.rightBitwise(l3, 24L) & 0xFF, Ww.rightBitwise(l3, 16L) & 0xFF, Ww.rightBitwise(l3, 8L) & 0xFF, 0xFFL & l3, Ww.rightBitwise(n4, 24L) & 0xFF, Ww.rightBitwise(n4, 16L) & 0xFF, Ww.rightBitwise(n4, 8L) & 0xFF, 0xFF & n4, Ww.rightBitwise(l4, 24L) & 0xFF, Ww.rightBitwise(l4, 16L) & 0xFF, Ww.rightBitwise(l4, 8L) & 0xFF, 0xFFL & l4};
    }

    public void genWithNoEvents() {
        this.tst = this.get_cf_date() - this.startTime;
        this.patp();
    }

    public int mn_w() {
        boolean bl = false;
        int n = 0;
        long l = 0L;
        String string = "";
        long l2 = this.get_cf_date();
        int n2 = this.mn_cd + this.mn_mc_indx;
        while (!bl) {
            string = Bmak.convertToHex(ThreadLocalRandom.current().nextDouble());
            String string2 = this.mn_cc + n2 + string;
            long[] lArray = this.mn_s(string2);
            if (0 == this.bdm(lArray, n2)) {
                bl = true;
                l = this.get_cf_date() - l2;
                this.mn_al.add(string);
                this.mn_tcl.add("" + l);
                this.mn_il.add("" + n);
                if (0 != this.mn_mc_indx) continue;
                this.mn_lg.add(this.mn_abck);
                this.mn_lg.add(this.mn_ts);
                this.mn_lg.add(this.mn_psn);
                this.mn_lg.add(this.mn_cc);
                this.mn_lg.add("" + this.mn_cd);
                this.mn_lg.add("" + n2);
                this.mn_lg.add(string);
                this.mn_lg.add(string2);
                this.mn_lg.add(Arrays.stream(lArray).mapToObj(String::valueOf).collect(Collectors.joining(",")));
                this.mn_lg.add("" + this.mn_rt);
                continue;
            }
            if (++n % 1000 != 0 || (l = this.get_cf_date() - l2) <= (long)this.mn_stout) continue;
            this.mn_wt = (int)((long)this.mn_wt + l);
            return this.mn_wt;
        }
        ++this.mn_mc_indx;
        Objects.requireNonNull(this);
        if (this.mn_mc_indx < 10) {
            this.mn_w();
            return -69;
        }
        this.mn_mc_indx = 0;
        this.mn_lc.put(this.mn_lcl, this.mn_cc);
        this.mn_ld.put(this.mn_lcl, "" + this.mn_cd);
        ++this.mn_lcl;
        this.mn_state = 0;
        this.mn_lg.add(String.valueOf(this.mn_wt));
        this.mn_lg.add(String.valueOf(this.get_cf_date()));
        this.mn_r.put(this.mn_abck + this.mn_psn, this.mn_pr());
        return -69;
    }

    public long rotate_right(long l, int n) {
        return Ww.rightTripleBitwise(l, n) | Ww.leftBitwise(l, 32L) - n;
    }

    public String getDocumentUrl() {
        return this.documentURL;
    }

    public int ab(String string) {
        if (string == null) {
            return -1;
        }
        int n = 0;
        int n2 = 0;
        while (n2 < string.length()) {
            int n3 = Character.codePointAt(string, n2);
            if (n3 < 128) {
                n += n3;
            }
            ++n2;
        }
        return n;
    }

    public Bmak() {
        this.pstate = false;
        this.mn_mc_lmt = 10;
        this.mn_r = new HashMap<String, String>();
        this.mn_mc_lmt = 10;
        this.mn_lc = new HashMap<Integer, String>();
        this.mn_ld = new HashMap<Integer, String>();
        this.pstate = false;
        this.start_ts = Instant.now().toEpochMilli();
        this.mn_cd = 10000;
        this.mn_mc_indx = 0;
        this.mn_cc = "";
        this.mn_al = new ArrayList<String>();
        this.mn_il = new ArrayList<String>();
        this.mn_tcl = new ArrayList<String>();
        this.mn_lg = new ArrayList<String>();
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

    public void bc() {
        JsonObject jsonObject = this.device.getWindowPerms();
        int n = jsonObject.getInteger("t");
        int n2 = jsonObject.getInteger("a");
        int n3 = jsonObject.getInteger("e");
        int n4 = jsonObject.getInteger("n");
        int n5 = jsonObject.getInteger("o");
        int n6 = jsonObject.getInteger("m");
        int n7 = jsonObject.getInteger("r");
        int n8 = jsonObject.getInteger("i");
        int n9 = jsonObject.getInteger("c");
        int n10 = jsonObject.getInteger("b");
        int n11 = jsonObject.getInteger("d");
        int n12 = jsonObject.getInteger("s");
        int n13 = jsonObject.getInteger("k");
        int n14 = jsonObject.getInteger("l");
        this.xagg = n + (n2 << 1) + (n3 << 2) + (n4 << 3) + (n5 << 4) + (n6 << 5) + (n7 << 6) + (n8 << 7) + (n13 << 8) + (n14 << 9) + (n9 << 10) + (n10 << 11) + (n11 << 12) + (n12 << 13);
    }

    public Map mn_get_current_challenges() {
        List list = this.get_mn_params_from_abck();
        HashMap<String, CallSite> hashMap = new HashMap<String, CallSite>();
        if (list == null) return hashMap;
        int n = 0;
        while (n < list.size()) {
            Object[] objectArray = (Object[])list.get(n);
            if (objectArray.length > 0) {
                String string = objectArray[6].toString();
                String string2 = objectArray[1] instanceof Integer && objectArray[2] instanceof Integer ? "" + ((Integer)objectArray[1] + (Integer)objectArray[2]) : objectArray[1].toString() + objectArray[2].toString();
                hashMap.put(string, (CallSite)((Object)string2));
            }
            ++n;
        }
        return hashMap;
    }

    public void bmisc() {
        this.pen = 0;
        this.wen = 0;
        this.den = 0;
    }

    public Object[] mn_get_new_challenge_params(List list) {
        Object[] objectArray;
        Integer n = null;
        Integer n2 = null;
        Integer n3 = null;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                int n4;
                Object[] objectArray2 = (Object[])list.get(i);
                if (objectArray2.length <= 0) continue;
                int n5 = (Integer)objectArray2[0];
                String string = this.mn_abck + this.start_ts + objectArray2[2];
                int n6 = (Integer)objectArray2[6];
                for (n4 = 0; n4 < this.mn_lcl && 1 == n5 && !this.mn_lc.get(n4).equals(string); ++n4) {
                }
                if (n4 != this.mn_lcl) continue;
                n = i;
                if (2 == n6) {
                    n2 = i;
                }
                if (3 != n6) continue;
                n3 = i;
            }
        }
        if (null != n3) {
            Objects.requireNonNull(this);
        }
        if (null != n2) {
            Objects.requireNonNull(this);
            objectArray = (Object[])list.get(n2);
            return objectArray;
        }
        if (null == n) return null;
        Objects.requireNonNull(this);
        objectArray = (Object[])list.get(n);
        return objectArray;
    }

    public void fpVal() {
        this.fpValCalculated = true;
        long l = Instant.now().toEpochMilli();
        this.data();
        long l2 = Instant.now().toEpochMilli();
        long l3 = l2 - l;
    }

    public void mn_update_challenge_details(Object[] objectArray) {
        this.mn_sen = (Integer)objectArray[0];
        this.mn_abck = (String)objectArray[1];
        this.mn_psn = (String)objectArray[2];
        this.mn_cd = (Integer)objectArray[3];
        this.mn_tout = (Integer)objectArray[4];
        this.mn_stout = (Integer)objectArray[5];
        this.mn_ct = (Integer)objectArray[6];
        this.mn_ts = "" + this.start_ts;
        this.mn_cc = this.mn_abck + this.start_ts + this.mn_psn;
    }

    public static void main(String[] stringArray) {
        Bmak bmak = new Bmak(new JsonObject("{\"ap\":true,\"bt\":{\"charging\":true,\"chargingTime\":0,\"dischargingTime\":\"Infinity\",\"level\":1,\"onchargingchange\":null,\"onchargingtimechange\":null,\"ondischargingtimechange\":null,\"onlevelchange\":null},\"fonts\":\"4,14,15,16,21,22,23,43,44,47,48,49,50,51\",\"fh\":\"f78bc5f5ba69eb06e56c3827f66c171eb2b27f75\",\"timing\":\"\",\"bp\":\"1038350511,-1979380391,1738406762,749224105\",\"sr\":{\"inner\":[3356,1306],\"outer\":[3356,1417],\"screen\":[0,23],\"pageOffset\":[0,0],\"avail\":[3440,1417],\"size\":[3440,1440],\"client\":[3340,176],\"colorDepth\":24,\"pixelDepth\":24},\"dp\":{\"XDomainRequest\":0,\"createPopup\":0,\"removeEventListener\":1,\"globalStorage\":0,\"openDatabase\":1,\"indexedDB\":1,\"attachEvent\":0,\"ActiveXObject\":0,\"dispatchEvent\":1,\"addBehavior\":0,\"addEventListener\":1,\"detachEvent\":0,\"fireEvent\":0,\"MutationObserver\":1,\"HTMLMenuItemElement\":0,\"Int8Array\":1,\"postMessage\":1,\"querySelector\":1,\"getElementsByClassName\":1,\"images\":1,\"compatMode\":\"CSS1Compat\",\"documentMode\":0,\"all\":1,\"now\":1,\"contextMenu\":0},\"lt\":\"1622868421082-4\",\"ps\":\"true,true\",\"cv\":\"f50fd5bc1e5aa5aab1cd866593c22ef9d61f14e3\",\"fp\":false,\"sp\":false,\"br\":\"Chrome\",\"ieps\":false,\"av\":false,\"b\":1,\"c\":0,\"jsv\":\"1.7\",\"nav\":{\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appName\":\"Netscape\",\"appCodeName\":\"Mozilla\",\"appVersion\":\"5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"appMinorVersion\":0,\"product\":\"Gecko\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"vendorSub\":\"\",\"buildID\":0,\"platform\":\"MacIntel\",\"oscpu\":0,\"hardwareConcurrency\":12,\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"systemLanguage\":0,\"userLanguage\":0,\"doNotTrack\":null,\"msDoNotTrack\":0,\"cookieEnabled\":true,\"geolocation\":1,\"vibrate\":1,\"maxTouchPoints\":0,\"webdriver\":false,\"plugins\":[\"Chrome PDF Plugin\",\"Chrome PDF Viewer\",\"Native Client\"]},\"crc\":{\"window.chrome\":{\"app\":{\"isInstalled\":false,\"InstallState\":{\"DISABLED\":\"disabled\",\"INSTALLED\":\"installed\",\"NOT_INSTALLED\":\"not_installed\"},\"RunningState\":{\"CANNOT_RUN\":\"cannot_run\",\"READY_TO_RUN\":\"ready_to_run\",\"RUNNING\":\"running\"}},\"runtime\":{\"OnInstalledReason\":{\"CHROME_UPDATE\":\"chrome_update\",\"INSTALL\":\"install\",\"SHARED_MODULE_UPDATE\":\"shared_module_update\",\"UPDATE\":\"update\"},\"OnRestartRequiredReason\":{\"APP_UPDATE\":\"app_update\",\"OS_UPDATE\":\"os_update\",\"PERIODIC\":\"periodic\"},\"PlatformArch\":{\"ARM\":\"arm\",\"ARM64\":\"arm64\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformNaclArch\":{\"ARM\":\"arm\",\"MIPS\":\"mips\",\"MIPS64\":\"mips64\",\"X86_32\":\"x86-32\",\"X86_64\":\"x86-64\"},\"PlatformOs\":{\"ANDROID\":\"android\",\"CROS\":\"cros\",\"LINUX\":\"linux\",\"MAC\":\"mac\",\"OPENBSD\":\"openbsd\",\"WIN\":\"win\"},\"RequestUpdateCheckStatus\":{\"NO_UPDATE\":\"no_update\",\"THROTTLED\":\"throttled\",\"UPDATE_AVAILABLE\":\"update_available\"}}}},\"nap\":\"11321144241322243122\",\"fc\":true,\"ua\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36\",\"product\":\"Gecko\",\"availWidth\":3440,\"availHeight\":1417,\"width\":3440,\"height\":1440,\"innerHeight\":1306,\"innerWidth\":3356,\"outerWidth\":3356,\"brave\":false,\"windowPerms\":{\"t\":1,\"a\":1,\"e\":0,\"n\":0,\"o\":1,\"m\":1,\"r\":1,\"i\":0,\"c\":1,\"b\":1,\"d\":0,\"s\":1,\"k\":1,\"l\":1},\"_phantom\":0,\"webdriver\":0,\"domAutomation\":0,\"activeXObject\":0,\"callPhantom\":0,\"documentMode\":0,\"isChrome\":0,\"isOnline\":1,\"isOpera\":0,\"hasInstallTrigger\":0,\"hasHTMLElement\":0,\"hasRTCPeerConnection\":1,\"hasMozInnerScreen\":0,\"hasVibrate\":1,\"hasBattery\":1,\"hasForEach\":0,\"hasFileReader\":1,\"deviceOrientation\":\"do_en\",\"deviceMotion\":\"dm_en\",\"touchEvent\":\"t_en\",\"navigatorFasSettings\":30261693,\"sed\":\"0,0,0,0,1,0,0\",\"languages\":\"en-US\",\"pluginsLength\":3,\"colorDepth\":24,\"pixelDepth\":24,\"cookieEnabled\":true,\"javaEnabled\":false,\"doNotTrack\":-1,\"canvasFP1\":\"2130238721\",\"canvasFP2\":\"-955688593\",\"rCFP\":\"-1241881201\",\"rVal\":\"649\",\"pluginInfo\":\",7,8\",\"sessionStorage\":true,\"localStorage\":true,\"indexedDB\":true,\"timezoneOffset\":240,\"webRTC\":true,\"voices\":\"Google Deutsch_de-DEGoogle US English_en-USGoogle UK English Female_en-GBGoogle UK English Male_en-GBGoogle espa\u00f1ol_es-ESGoogle espa\u00f1ol de Estados Unidos_es-USGoogle fran\u00e7ais_fr-FRGoogle \u0939\u093f\u0928\u094d\u0926\u0940_hi-INGoogle Bahasa Indonesia_id-IDGoogle italiano_it-ITGoogle \u65e5\u672c\u8a9e_ja-JPGoogle \ud55c\uad6d\uc758_ko-KRGoogle Nederlands_nl-NLGoogle polski_pl-PLGoogle portugu\u00eas do Brasil_pt-BRGoogle \u0440\u0443\u0441\u0441\u043a\u0438\u0439_ru-RUGoogle\u00a0\u666e\u901a\u8bdd\uff08\u4e2d\u56fd\u5927\u9646\uff09_zh-CNGoogle\u00a0\u7ca4\u8a9e\uff08\u9999\u6e2f\uff09_zh-HKGoogle \u570b\u8a9e\uff08\u81fa\u7063\uff09_zh-TWAlex_en-USAlice_it-ITAlva_sv-SEAmelie_fr-CAAnna_de-DECarmit_he-ILDamayanti_id-IDDaniel_en-GBDiego_es-AREllen_nl-BEFiona_enFred_en-USIoana_ro-ROJoana_pt-PTJorge_es-ESJuan_es-MXKanya_th-THKaren_en-AUKyoko_ja-JPLaura_sk-SKLekha_hi-INLuca_it-ITLuciana_pt-BRMaged_ar-SAMariska_hu-HUMei-Jia_zh-TWMelina_el-GRMilena_ru-RUMoira_en-IEMonica_es-ESNora_nb-NOPaulina_es-MXRishi_en-INSamantha_en-USSara_da-DKSatu_fi-FISin-ji_zh-HKTessa_en-ZAThomas_fr-FRTing-Ting_zh-CNVeena_en-INVictoria_en-USXander_nl-NLYelda_tr-TRYuna_ko-KRYuri_ru-RUZosia_pl-PLZuzana_cs-CZ\",\"isMobile\":false}"));
        System.out.println(bmak.genPageLoadSensor("B5806C529A27B49450855034F916639F~-1~YAAQv8ZMaE4RmCR6AQAA+I9PMAZYxa+iYdpaoiRda9YOn3VPk+Mdmw4yP3VgYRTWmNVLZ678nvJM45v/t3S9Ex5584s1XaH0Y6sBjhxyd2dWs+H33nRlFpJhS+gUy5XBJbi4g74s+pp3H1+LcItNsh/njOtrvW6PTYw+oTI3l1XlQEvz+3KAtd8POBI9GOHS6S+STZJAI3uE5vj4f3lJJ2mGAHwYMuCw0S8Qb5E7j2ECftrQQtzHPI/qRSyt8kajCj02olOJd8DLu6pewJvIZRIAagAo5/XK/htTrV3Gv7e905F5Ht2PS7hC8iS7QojDuM1HlOFSsk9A193eo9NzkgL1xnvX32QnOtWSMCH+eRUc+DMBCMH8cmBL6BTqzv0jZjF28cSXGG448E9RutqOJNB5hYgQNQ==~-1~-1~-1"));
        System.out.println(bmak.genSecondSensor("5B2C972C16D759F35448A4A92C43772E~-1~YAAQbBDeFxZsJRt6AQAAwWTqNAZvbCWdOGDboeG/mcPrPOBCCpM+vsidlFFKj5LjbJeKs2TdsjEJuFZpaF4/6zMjxXEnkWvt7fGtG/h6Ag6Kn56s6p4C0BEJlls263qGZaT+UZwDQdZ4OHgRlqzJGpt06ZQdHkqo2YVsj9McZDT9jzf0gME7/rdmNnyWZgA+Pq/jF+Z4lWrGfZS6duyfmXXqz2hAqZo06L5gxdCEZpo05/voyt7WAkmLNWg5GdIr3ny+AaB+Su2XM4O2s+YIjr4WKNgNnnfqt3xj3ur8BiazEoFC8OWvQQgsPBAEF0w2ysuiKNMN3SZ2gsd/zYISFsjDaBlIngQ8VNmavWHu6M/6iHU4UhK2D7uFDQp58KsGFJNsp3AoIVeT4PnMGsmGXpPXBOM7nEJCKTRPCkpui2puvcAEF3j7Fq3F19mRMDZgfg==~-1~||1-PyoliHBRta-500-10-1000-2||~-1"));
    }

    public String t(int n) {
        return Character.toString(n);
    }

    public long x2() {
        return this.get_cf_date();
    }

    public String mn_pr() {
        return String.join((CharSequence)",", this.mn_al) + ";" + String.join((CharSequence)",", this.mn_tcl) + ";" + String.join((CharSequence)",", this.mn_il) + ";" + String.join((CharSequence)",", this.mn_lg) + ";";
    }

    public String getforminfo() {
        String string = "";
        String string2 = "";
        this.ins = string;
        this.cns = string;
        return string2;
    }

    public String od(String string, String string2) {
        int n = string2.length();
        ArrayList<String> arrayList = new ArrayList<String>();
        if (n <= 0) return string;
        int n2 = 0;
        while (true) {
            if (n2 >= string.length()) {
                if (arrayList.size() <= 0) return string;
                return String.join((CharSequence)"", arrayList);
            }
            int n3 = Character.codePointAt(string, n2);
            char c2 = string.charAt(n2);
            int n4 = Character.codePointAt(string2, n2 % n);
            if ((n3 = this.rir(n3, 47, 57, n4)) != Character.codePointAt(string, n2)) {
                c2 = (char)n3;
            }
            arrayList.add(String.valueOf(c2));
            ++n2;
        }
    }
}


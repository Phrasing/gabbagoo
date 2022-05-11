package io.trickle.task.sites.amazon;

import java.util.regex.Pattern;

public class Patterns {
   public static Pattern GLOW = Pattern.compile("token' value='(.*?)'");
   public static Pattern ADDRESS_ID = Pattern.compile("input data-addnewaddress=\"add-new\" id=\"ubbShipTo\" name=\"dropdown-selection-ubb\" type=\"hidden\" value=\"(.*?)\"");
   public static Pattern TITLE = Pattern.compile("<title>Amazon.com: (.*?) : Everything Else");
   public static Pattern CSRF = Pattern.compile("name=\"CSRF\" value=\"(.*?)\"");
   public static Pattern API_CSRF = Pattern.compile("\"amazonApiCsrfToken\":\"(.*?)\"");
   public static Pattern CSM_PREFIX = Pattern.compile("\"requestId\":\"(.*?)\"");
   public static Pattern PURCHASE_ID = Pattern.compile("amp;pid=(.*?)&");
   public static Pattern CART_ITEM_ID = Pattern.compile("submit\\.delete\\.(.*?)\"");
}

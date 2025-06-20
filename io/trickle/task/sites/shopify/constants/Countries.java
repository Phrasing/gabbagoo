package io.trickle.task.sites.shopify.constants;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Countries {
   public static Map COUNTRIES;

   public static String fullCountryName(String var0) {
      var0 = var0.replace("JAPAN", "JP");
      return (String)COUNTRIES.get(var0);
   }

   static {
      COUNTRIES = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      COUNTRIES.put("Afghanistan", "AF");
      COUNTRIES.put("Åland Islands", "AX");
      COUNTRIES.put("Albania", "AL");
      COUNTRIES.put("Algeria", "DZ");
      COUNTRIES.put("American Samoa", "AS");
      COUNTRIES.put("Andorra", "AD");
      COUNTRIES.put("Angola", "AO");
      COUNTRIES.put("Anguilla", "AI");
      COUNTRIES.put("Antarctica", "AQ");
      COUNTRIES.put("Antigua & Barbuda", "AG");
      COUNTRIES.put("Argentina", "AR");
      COUNTRIES.put("Armenia", "AM");
      COUNTRIES.put("Aruba", "AW");
      COUNTRIES.put("Australia", "AU");
      COUNTRIES.put("Austria", "AT");
      COUNTRIES.put("Azerbaijan", "AZ");
      COUNTRIES.put("Bahamas", "BS");
      COUNTRIES.put("Bahrain", "BH");
      COUNTRIES.put("Bangladesh", "BD");
      COUNTRIES.put("Barbados", "BB");
      COUNTRIES.put("Belarus", "BY");
      COUNTRIES.put("Belgium", "BE");
      COUNTRIES.put("Belize", "BZ");
      COUNTRIES.put("Benin", "BJ");
      COUNTRIES.put("Bermuda", "BM");
      COUNTRIES.put("Bhutan", "BT");
      COUNTRIES.put("Bolivia", "BO");
      COUNTRIES.put("Bosnia & Herzegovina", "BA");
      COUNTRIES.put("Botswana", "BW");
      COUNTRIES.put("Bouvet Island", "BV");
      COUNTRIES.put("Brazil", "BR");
      COUNTRIES.put("British Indian Ocean Territory", "IO");
      COUNTRIES.put("British Virgin Islands", "VG");
      COUNTRIES.put("Brunei", "BN");
      COUNTRIES.put("Bulgaria", "BG");
      COUNTRIES.put("Burkina Faso", "BF");
      COUNTRIES.put("Burundi", "BI");
      COUNTRIES.put("Cambodia", "KH");
      COUNTRIES.put("Cameroon", "CM");
      COUNTRIES.put("Canada", "CA");
      COUNTRIES.put("Cape Verde", "CV");
      COUNTRIES.put("Caribbean Netherlands", "BQ");
      COUNTRIES.put("Cayman Islands", "KY");
      COUNTRIES.put("Central African Republic", "CF");
      COUNTRIES.put("Chad", "TD");
      COUNTRIES.put("Chile", "CL");
      COUNTRIES.put("China", "CN");
      COUNTRIES.put("Christmas Island", "CX");
      COUNTRIES.put("Colombia", "CO");
      COUNTRIES.put("Comoros", "KM");
      COUNTRIES.put("Congo - Brazzaville", "CG");
      COUNTRIES.put("Congo - Kinshasa", "CD");
      COUNTRIES.put("Cook Islands", "CK");
      COUNTRIES.put("Costa Rica", "CR");
      COUNTRIES.put("Côte d’Ivoire", "CI");
      COUNTRIES.put("Croatia", "HR");
      COUNTRIES.put("Cuba", "CU");
      COUNTRIES.put("Curaçao", "CW");
      COUNTRIES.put("Cyprus", "CY");
      COUNTRIES.put("Czechia", "CZ");
      COUNTRIES.put("Denmark", "DK");
      COUNTRIES.put("Djibouti", "DJ");
      COUNTRIES.put("Dominica", "DM");
      COUNTRIES.put("Dominican Republic", "DO");
      COUNTRIES.put("Ecuador", "EC");
      COUNTRIES.put("Egypt", "EG");
      COUNTRIES.put("El Salvador", "SV");
      COUNTRIES.put("Equatorial Guinea", "GQ");
      COUNTRIES.put("Eritrea", "ER");
      COUNTRIES.put("Estonia", "EE");
      COUNTRIES.put("Eswatini", "SZ");
      COUNTRIES.put("Ethiopia", "ET");
      COUNTRIES.put("Falkland Islands", "FK");
      COUNTRIES.put("Faroe Islands", "FO");
      COUNTRIES.put("Fiji", "FJ");
      COUNTRIES.put("Finland", "FI");
      COUNTRIES.put("France", "FR");
      COUNTRIES.put("French Guiana", "GF");
      COUNTRIES.put("French Polynesia", "PF");
      COUNTRIES.put("French Southern Territories", "TF");
      COUNTRIES.put("Gabon", "GA");
      COUNTRIES.put("Gambia", "GM");
      COUNTRIES.put("Georgia", "GE");
      COUNTRIES.put("Germany", "DE");
      COUNTRIES.put("Ghana", "GH");
      COUNTRIES.put("Gibraltar", "GI");
      COUNTRIES.put("Greece", "GR");
      COUNTRIES.put("Greenland", "GL");
      COUNTRIES.put("Grenada", "GD");
      COUNTRIES.put("Guadeloupe", "GP");
      COUNTRIES.put("Guam", "GU");
      COUNTRIES.put("Guatemala", "GT");
      COUNTRIES.put("Guernsey", "GG");
      COUNTRIES.put("Guinea", "GN");
      COUNTRIES.put("Guinea-Bissau", "GW");
      COUNTRIES.put("Guyana", "GY");
      COUNTRIES.put("Haiti", "HT");
      COUNTRIES.put("Heard & McDonald Islands", "HM");
      COUNTRIES.put("Honduras", "HN");
      COUNTRIES.put("Hong Kong SAR China", "HK");
      COUNTRIES.put("Hungary", "HU");
      COUNTRIES.put("Iceland", "IS");
      COUNTRIES.put("India", "IN");
      COUNTRIES.put("Indonesia", "ID");
      COUNTRIES.put("Iran", "IR");
      COUNTRIES.put("Iraq", "IQ");
      COUNTRIES.put("Ireland", "IE");
      COUNTRIES.put("Isle of Man", "IM");
      COUNTRIES.put("Israel", "IL");
      COUNTRIES.put("Italy", "IT");
      COUNTRIES.put("Jamaica", "JM");
      COUNTRIES.put("Japan", "JP");
      COUNTRIES.put("Jersey", "JE");
      COUNTRIES.put("Jordan", "JO");
      COUNTRIES.put("Kazakhstan", "KZ");
      COUNTRIES.put("Kenya", "KE");
      COUNTRIES.put("Kiribati", "KI");
      COUNTRIES.put("Kuwait", "KW");
      COUNTRIES.put("Kyrgyzstan", "KG");
      COUNTRIES.put("Laos", "LA");
      COUNTRIES.put("Latvia", "LV");
      COUNTRIES.put("Lebanon", "LB");
      COUNTRIES.put("Lesotho", "LS");
      COUNTRIES.put("Liberia", "LR");
      COUNTRIES.put("Libya", "LY");
      COUNTRIES.put("Liechtenstein", "LI");
      COUNTRIES.put("Lithuania", "LT");
      COUNTRIES.put("Luxembourg", "LU");
      COUNTRIES.put("Macao SAR China", "MO");
      COUNTRIES.put("Madagascar", "MG");
      COUNTRIES.put("Malawi", "MW");
      COUNTRIES.put("Malaysia", "MY");
      COUNTRIES.put("Maldives", "MV");
      COUNTRIES.put("Mali", "ML");
      COUNTRIES.put("Malta", "MT");
      COUNTRIES.put("Marshall Islands", "MH");
      COUNTRIES.put("Martinique", "MQ");
      COUNTRIES.put("Mauritania", "MR");
      COUNTRIES.put("Mauritius", "MU");
      COUNTRIES.put("Mayotte", "YT");
      COUNTRIES.put("Mexico", "MX");
      COUNTRIES.put("Micronesia", "FM");
      COUNTRIES.put("Moldova", "MD");
      COUNTRIES.put("Monaco", "MC");
      COUNTRIES.put("Mongolia", "MN");
      COUNTRIES.put("Montenegro", "ME");
      COUNTRIES.put("Montserrat", "MS");
      COUNTRIES.put("Morocco", "MA");
      COUNTRIES.put("Mozambique", "MZ");
      COUNTRIES.put("Myanmar", "MM");
      COUNTRIES.put("Namibia", "NA");
      COUNTRIES.put("Nauru", "NR");
      COUNTRIES.put("Nepal", "NP");
      COUNTRIES.put("Netherlands", "NL");
      COUNTRIES.put("New Caledonia", "NC");
      COUNTRIES.put("New Zealand", "NZ");
      COUNTRIES.put("Nicaragua", "NI");
      COUNTRIES.put("Niger", "NE");
      COUNTRIES.put("Nigeria", "NG");
      COUNTRIES.put("Niue", "NU");
      COUNTRIES.put("Norfolk Island", "NF");
      COUNTRIES.put("North Korea", "KP");
      COUNTRIES.put("North Macedonia", "MK");
      COUNTRIES.put("Northern Mariana Islands", "MP");
      COUNTRIES.put("Norway", "NO");
      COUNTRIES.put("Oman", "OM");
      COUNTRIES.put("Pakistan", "PK");
      COUNTRIES.put("Palau", "PW");
      COUNTRIES.put("Palestinian Territories", "PS");
      COUNTRIES.put("Panama", "PA");
      COUNTRIES.put("Papua New Guinea", "PG");
      COUNTRIES.put("Paraguay", "PY");
      COUNTRIES.put("Peru", "PE");
      COUNTRIES.put("Philippines", "PH");
      COUNTRIES.put("Pitcairn Islands", "PN");
      COUNTRIES.put("Poland", "PL");
      COUNTRIES.put("Portugal", "PT");
      COUNTRIES.put("Puerto Rico", "PR");
      COUNTRIES.put("Qatar", "QA");
      COUNTRIES.put("Réunion", "RE");
      COUNTRIES.put("Romania", "RO");
      COUNTRIES.put("Russia", "RU");
      COUNTRIES.put("Rwanda", "RW");
      COUNTRIES.put("Samoa", "WS");
      COUNTRIES.put("San Marino", "SM");
      COUNTRIES.put("São Tomé & Príncipe", "ST");
      COUNTRIES.put("Saudi Arabia", "SA");
      COUNTRIES.put("Senegal", "SN");
      COUNTRIES.put("Serbia", "RS");
      COUNTRIES.put("Seychelles", "SC");
      COUNTRIES.put("Sierra Leone", "SL");
      COUNTRIES.put("Singapore", "SG");
      COUNTRIES.put("Sint Maarten", "SX");
      COUNTRIES.put("Slovakia", "SK");
      COUNTRIES.put("Slovenia", "SI");
      COUNTRIES.put("Solomon Islands", "SB");
      COUNTRIES.put("Somalia", "SO");
      COUNTRIES.put("South Africa", "ZA");
      COUNTRIES.put("South Korea", "KR");
      COUNTRIES.put("South Sudan", "SS");
      COUNTRIES.put("Spain", "ES");
      COUNTRIES.put("Sri Lanka", "LK");
      COUNTRIES.put("Sudan", "SD");
      COUNTRIES.put("Suriname", "SR");
      COUNTRIES.put("Sweden", "SE");
      COUNTRIES.put("Switzerland", "CH");
      COUNTRIES.put("Syria", "SY");
      COUNTRIES.put("Taiwan", "TW");
      COUNTRIES.put("Tajikistan", "TJ");
      COUNTRIES.put("Tanzania", "TZ");
      COUNTRIES.put("Thailand", "TH");
      COUNTRIES.put("Togo", "TG");
      COUNTRIES.put("Tokelau", "TK");
      COUNTRIES.put("Tonga", "TO");
      COUNTRIES.put("Trinidad", "TT");
      COUNTRIES.put("Tunisia", "TN");
      COUNTRIES.put("Turkey", "TR");
      COUNTRIES.put("Turkmenistan", "TM");
      COUNTRIES.put("Tuvalu", "TV");
      COUNTRIES.put("US Outlying Islands", "UM");
      COUNTRIES.put("Virgin Islands", "VI");
      COUNTRIES.put("Uganda", "UG");
      COUNTRIES.put("Ukraine", "UA");
      COUNTRIES.put("United Arab Emirates", "AE");
      COUNTRIES.put("United Kingdom", "United Kingdom");
      COUNTRIES.put("United States", "US");
      COUNTRIES.put("Uruguay", "UY");
      COUNTRIES.put("Uzbekistan", "UZ");
      COUNTRIES.put("Vanuatu", "VU");
      COUNTRIES.put("Vatican City", "VA");
      COUNTRIES.put("Venezuela", "VE");
      COUNTRIES.put("Vietnam", "VN");
      COUNTRIES.put("Western Sahara", "EH");
      COUNTRIES.put("Yemen", "YE");
      COUNTRIES.put("Zambia", "ZM");
      COUNTRIES.put("Zimbabwe", "ZW");
      COUNTRIES = (Map)COUNTRIES.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
   }
}

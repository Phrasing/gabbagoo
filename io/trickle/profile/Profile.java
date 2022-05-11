package io.trickle.profile;

import io.trickle.task.sites.shopify.constants.Countries;
import io.trickle.task.sites.shopify.constants.States;
import io.trickle.util.Utils;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class Profile {
   public String state;
   public PaymentMethod paymentMethod;
   public CardType cardType;
   public String country;
   public String fullCountry;
   public String firstName;
   public String cardNumber;
   public String address2;
   public String accountPassword = null;
   public String email;
   public String address1;
   public String cvv;
   public String fullState;
   public String expiryYear;
   public String phone;
   public String expiryMonth;
   public String zip;
   public String accountEmail = null;
   public String city;
   public String lastName;

   public String getZip() {
      return this.zip;
   }

   public String getLastDigits() {
      return this.cardNumber.length() > 4 ? this.cardNumber.substring(this.cardNumber.length() - 4) : this.cardNumber;
   }

   public String getEmail() {
      return this.accountEmail == null ? this.email : this.accountEmail;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public String getState() {
      return this.state;
   }

   public Profile() {
      this.firstName = null;
      this.lastName = null;
      this.email = null;
      this.phone = null;
      this.address1 = null;
      this.address2 = null;
      this.state = null;
      this.fullState = null;
      this.city = null;
      this.country = null;
      this.fullCountry = null;
      this.zip = null;
      this.paymentMethod = null;
      this.cardType = null;
      this.cardNumber = null;
      this.expiryYear = null;
      this.expiryMonth = null;
      this.cvv = null;
      this.accountEmail = null;
   }

   public Profile(Profile var1) {
      this.firstName = var1.firstName;
      this.lastName = var1.lastName;
      this.email = var1.email;
      this.phone = var1.phone;
      this.address1 = var1.address1;
      this.address2 = var1.address2;
      this.state = var1.state;
      this.fullState = var1.fullState;
      this.city = var1.city;
      this.country = var1.country;
      this.fullCountry = var1.fullCountry;
      this.zip = var1.zip;
      this.paymentMethod = var1.paymentMethod;
      this.cardType = var1.cardType;
      this.cardNumber = var1.cardNumber;
      this.expiryYear = var1.expiryYear;
      this.expiryMonth = var1.expiryMonth;
      this.cvv = var1.cvv;
      this.accountEmail = var1.accountEmail;
   }

   public String getCardNumber() {
      return this.cardNumber;
   }

   public String getExpiryYear() {
      return this.expiryYear;
   }

   public String getAddress1() {
      return this.address1;
   }

   public String getCountry() {
      return this.country;
   }

   public String getExpiryMonth() {
      return this.expiryMonth;
   }

   public String getCity() {
      return this.city;
   }

   public String getFullCountry() {
      return this.fullCountry;
   }

   public CardType getCardType() {
      return this.cardType;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setAccountPassword(String var1) {
      this.accountPassword = var1;
   }

   public String getAccountPassword() {
      return this.accountPassword;
   }

   public void setAccountEmail(String var1) {
      this.accountEmail = var1;
   }

   public String toString() {
      return "Profile{firstName='" + this.firstName + "', lastName='" + this.lastName + "', email='" + this.email + "', phone='" + this.phone + "', address1='" + this.address1 + "', address2='" + this.address2 + "', state='" + this.state + "', fullState='" + this.fullState + "', city='" + this.city + "', country='" + this.country + "', fullCountry='" + this.fullCountry + "', zip='" + this.zip + "', paymentMethod=" + this.paymentMethod + ", cardType=" + this.cardType + ", cardNumber='" + this.cardNumber + "', expiryYear='" + this.expiryYear + "', expiryMonth='" + this.expiryMonth + "', cvv='" + this.cvv + "', accountEmail='" + this.accountEmail + "'}";
   }

   public String splitCard() {
      String var1 = this.getCardNumber();
      String var2 = " ";
      StringBuilder var3 = new StringBuilder(var1.length() + var2.length() * (var1.length() / 4) + 1);
      int var4 = 0;

      for(String var5 = ""; var4 < var1.length(); var4 += 4) {
         var3.append(var5);
         var5 = var2;
         var3.append(var1, var4, Math.min(var4 + 4, var1.length()));
      }

      return var3.toString();
   }

   public PaymentMethod getPaymentMethod() {
      return this.paymentMethod;
   }

   public String getLastName() {
      return this.lastName;
   }

   public String getAddress2() {
      return this.address2;
   }

   public String getCvv() {
      return this.cvv;
   }

   public Profile(String[] var1) {
      this.firstName = var1[2].replace("random", FirstNames.FIRST_NAMES[ThreadLocalRandom.current().nextInt(FirstNames.FIRST_NAMES.length)]);
      this.lastName = var1[3].replace("random", LastNames.LAST_NAMES[ThreadLocalRandom.current().nextInt(LastNames.LAST_NAMES.length)]);
      String var10001 = var1[4];
      String var10003 = this.firstName.toLowerCase(Locale.ROOT);
      this.email = var10001.replace("random", var10003 + this.lastName.toLowerCase(Locale.ROOT));
      var10001 = var1[5].replace("-", "").replace(" ", "").trim();
      ThreadLocalRandom var2 = ThreadLocalRandom.current();
      this.phone = var10001.replace("random", "" + var2.nextInt(1001000, 9999999));
      this.address1 = var1[6].replace("random", Utils.getStringCharacterOnly(ThreadLocalRandom.current().nextInt(3, 5)));
      this.address2 = var1[7].replace("random", Utils.getStringCharacterOnly(ThreadLocalRandom.current().nextInt(3, 5)));
      this.state = var1[8].toUpperCase();
      var10001 = var1[9].substring(0, 1).toUpperCase();
      this.city = var10001 + var1[9].substring(1).toLowerCase();
      this.zip = var1[10];
      this.country = var1[11].replace("USA", "US").replace("JAPAN", "JP").replace("CANADA", "CA").replace("UK", "United Kingdom").replace("GB", "United Kingdom");
      this.cardNumber = var1[12].replace("-", "").replace(" ", "").trim();
      if (this.cardNumber.length() < 14) {
         System.out.println("WARNING! Card number is invalid -> " + this.cardNumber);
      }

      if (var1[13].length() == 1) {
         this.expiryMonth = "0" + var1[13];
      } else {
         this.expiryMonth = var1[13];
      }

      if (var1[14].length() == 2) {
         this.expiryYear = "20" + var1[14];
      } else {
         this.expiryYear = var1[14];
      }

      this.cvv = var1[15];
      this.paymentMethod = PaymentMethod.detectMethod(this.cardNumber);
      this.cardType = CardType.detect(this.cardNumber);
      if (this.country.equals("United Kingdom")) {
         this.fullState = "";
      } else {
         this.fullState = States.fullStateName(this.state);
         if (this.fullState == null) {
            System.out.println("Please check your state/prefecture -> " + this.state);
         }
      }

      this.fullCountry = Countries.fullCountryName(this.country);
   }

   public Profile copy() {
      return new Profile(this);
   }

   public String getFullState() {
      return this.fullState;
   }

   public String getAccountEmail() {
      return this.accountEmail;
   }
}

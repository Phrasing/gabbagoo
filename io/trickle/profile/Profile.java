/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.CardType
 *  io.trickle.profile.FirstNames
 *  io.trickle.profile.LastNames
 *  io.trickle.profile.PaymentMethod
 *  io.trickle.task.sites.shopify.constants.Countries
 *  io.trickle.task.sites.shopify.constants.States
 *  io.trickle.util.Utils
 */
package io.trickle.profile;

import io.trickle.profile.CardType;
import io.trickle.profile.FirstNames;
import io.trickle.profile.LastNames;
import io.trickle.profile.PaymentMethod;
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
        if (this.cardNumber.length() <= 4) return this.cardNumber;
        return this.cardNumber.substring(this.cardNumber.length() - 4);
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

    public Profile(Profile profile) {
        this.firstName = profile.firstName;
        this.lastName = profile.lastName;
        this.email = profile.email;
        this.phone = profile.phone;
        this.address1 = profile.address1;
        this.address2 = profile.address2;
        this.state = profile.state;
        this.fullState = profile.fullState;
        this.city = profile.city;
        this.country = profile.country;
        this.fullCountry = profile.fullCountry;
        this.zip = profile.zip;
        this.paymentMethod = profile.paymentMethod;
        this.cardType = profile.cardType;
        this.cardNumber = profile.cardNumber;
        this.expiryYear = profile.expiryYear;
        this.expiryMonth = profile.expiryMonth;
        this.cvv = profile.cvv;
        this.accountEmail = profile.accountEmail;
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

    public void setAccountPassword(String string) {
        this.accountPassword = string;
    }

    public String getAccountPassword() {
        return this.accountPassword;
    }

    public void setAccountEmail(String string) {
        this.accountEmail = string;
    }

    public String toString() {
        return "Profile{firstName='" + this.firstName + "', lastName='" + this.lastName + "', email='" + this.email + "', phone='" + this.phone + "', address1='" + this.address1 + "', address2='" + this.address2 + "', state='" + this.state + "', fullState='" + this.fullState + "', city='" + this.city + "', country='" + this.country + "', fullCountry='" + this.fullCountry + "', zip='" + this.zip + "', paymentMethod=" + this.paymentMethod + ", cardType=" + this.cardType + ", cardNumber='" + this.cardNumber + "', expiryYear='" + this.expiryYear + "', expiryMonth='" + this.expiryMonth + "', cvv='" + this.cvv + "', accountEmail='" + this.accountEmail + "'}";
    }

    public String splitCard() {
        String string = this.getCardNumber();
        String string2 = " ";
        StringBuilder stringBuilder = new StringBuilder(string.length() + string2.length() * (string.length() / 4) + 1);
        int n = 0;
        String string3 = "";
        while (n < string.length()) {
            stringBuilder.append(string3);
            string3 = string2;
            stringBuilder.append(string, n, Math.min(n + 4, string.length()));
            n += 4;
        }
        return stringBuilder.toString();
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

    public Profile(String[] stringArray) {
        this.firstName = stringArray[2].replace("random", FirstNames.FIRST_NAMES[ThreadLocalRandom.current().nextInt(FirstNames.FIRST_NAMES.length)]);
        this.lastName = stringArray[3].replace("random", LastNames.LAST_NAMES[ThreadLocalRandom.current().nextInt(LastNames.LAST_NAMES.length)]);
        this.email = stringArray[4].replace("random", this.firstName.toLowerCase(Locale.ROOT) + this.lastName.toLowerCase(Locale.ROOT));
        this.phone = stringArray[5].replace("-", "").replace(" ", "").trim().replace("random", "" + ThreadLocalRandom.current().nextInt(1001000, 9999999));
        this.address1 = stringArray[6].replace("random", Utils.getStringCharacterOnly((int)ThreadLocalRandom.current().nextInt(3, 5)));
        this.address2 = stringArray[7].replace("random", Utils.getStringCharacterOnly((int)ThreadLocalRandom.current().nextInt(3, 5)));
        this.state = stringArray[8].toUpperCase();
        this.city = stringArray[9].substring(0, 1).toUpperCase() + stringArray[9].substring(1).toLowerCase();
        this.zip = stringArray[10];
        this.country = stringArray[11].replace("USA", "US").replace("JAPAN", "JP").replace("CANADA", "CA").replace("UK", "United Kingdom").replace("GB", "United Kingdom");
        this.cardNumber = stringArray[12].replace("-", "").replace(" ", "").trim();
        if (this.cardNumber.length() < 14) {
            System.out.println("WARNING! Card number is invalid -> " + this.cardNumber);
        }
        this.expiryMonth = stringArray[13].length() == 1 ? "0" + stringArray[13] : stringArray[13];
        this.expiryYear = stringArray[14].length() == 2 ? "20" + stringArray[14] : stringArray[14];
        this.cvv = stringArray[15];
        this.paymentMethod = PaymentMethod.detectMethod((String)this.cardNumber);
        this.cardType = CardType.detect((String)this.cardNumber);
        if (this.country.equals("United Kingdom")) {
            this.fullState = "";
        } else {
            this.fullState = States.fullStateName((String)this.state);
            if (this.fullState == null) {
                System.out.println("Please check your state/prefecture -> " + this.state);
            }
        }
        this.fullCountry = Countries.fullCountryName((String)this.country);
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

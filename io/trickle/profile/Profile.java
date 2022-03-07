/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.profile;

import io.trickle.profile.CardType;
import io.trickle.profile.PaymentMethod;
import io.trickle.task.sites.shopify.constants.Countries;
import io.trickle.task.sites.shopify.constants.States;

public class Profile {
    public String email;
    public String address2;
    public PaymentMethod paymentMethod;
    public String lastName;
    public String city;
    public String state;
    public String phone;
    public String fullState;
    public CardType cardType;
    public String cardNumber;
    public String country;
    public String firstName;
    public String accountEmail = null;
    public String address1;
    public String fullCountry;
    public String cvv;
    public String expiryMonth;
    public String zip;
    public String accountPassword = null;
    public String expiryYear;

    public String getLastDigits() {
        if (this.cardNumber.length() <= 4) return this.cardNumber;
        return this.cardNumber.substring(this.cardNumber.length() - 4);
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAddress2() {
        return this.address2;
    }

    public String getFullState() {
        return this.fullState;
    }

    public String getCountry() {
        return this.country;
    }

    public String getExpiryYear() {
        return this.expiryYear;
    }

    public String getAccountEmail() {
        return this.accountEmail;
    }

    public Profile copy() {
        return new Profile(this);
    }

    public String getState() {
        return this.state;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    public String getEmail() {
        String string;
        if (this.accountEmail == null) {
            string = this.email;
            return string;
        }
        string = this.accountEmail;
        return string;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getExpiryMonth() {
        return this.expiryMonth;
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

    public String getFullCountry() {
        return this.fullCountry;
    }

    public String getAddress1() {
        return this.address1;
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

    public String getCvv() {
        return this.cvv;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getCity() {
        return this.city;
    }

    public void setAccountEmail(String string) {
        this.accountEmail = string;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public Profile(String[] stringArray) {
        this.firstName = stringArray[2];
        this.lastName = stringArray[3];
        this.email = stringArray[4];
        this.phone = stringArray[5].replace("-", "").replace(" ", "").trim();
        this.address1 = stringArray[6];
        this.address2 = stringArray[7];
        this.state = stringArray[8].toUpperCase();
        this.city = stringArray[9].substring(0, 1).toUpperCase() + stringArray[9].substring(1).toLowerCase();
        this.zip = stringArray[10];
        this.country = stringArray[11].replace("USA", "US").replace("JAPAN", "JP").replace("CANADA", "CA");
        this.cardNumber = stringArray[12].replace("-", "").replace(" ", "").trim();
        this.expiryMonth = stringArray[13].length() == 1 ? "0" + stringArray[13] : stringArray[13];
        this.expiryYear = stringArray[14].length() == 2 ? "20" + stringArray[14] : stringArray[14];
        this.cvv = stringArray[15];
        this.paymentMethod = PaymentMethod.detectMethod(this.cardNumber);
        this.cardType = CardType.detect(this.cardNumber);
        this.fullState = States.fullStateName(this.state);
        if (this.fullState == null) {
            System.out.println("Please check your state/prefecture -> " + this.state);
        }
        this.fullCountry = Countries.fullCountryName(this.country);
    }

    public String toString() {
        return "Profile{firstName='" + this.firstName + "', lastName='" + this.lastName + "', email='" + this.email + "', phone='" + this.phone + "', address1='" + this.address1 + "', address2='" + this.address2 + "', state='" + this.state + "', fullState='" + this.fullState + "', city='" + this.city + "', country='" + this.country + "', fullCountry='" + this.fullCountry + "', zip='" + this.zip + "', paymentMethod=" + this.paymentMethod + ", cardType=" + this.cardType + ", cardNumber='" + this.cardNumber + "', expiryYear='" + this.expiryYear + "', expiryMonth='" + this.expiryMonth + "', cvv='" + this.cvv + "', accountEmail='" + this.accountEmail + "'}";
    }

    public void setAccountPassword(String string) {
        this.accountPassword = string;
    }

    public String getAccountPassword() {
        return this.accountPassword;
    }

    public String getZip() {
        return this.zip;
    }
}


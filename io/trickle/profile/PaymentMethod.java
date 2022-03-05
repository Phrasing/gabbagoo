/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.profile;

public enum PaymentMethod {
    VISA("VISA"),
    AMEX("AMEX"),
    DISCOVER("DISCOVER"),
    MASTERCARD("MASTERCARD");

    public String method;

    public String getFirstLetterUppercase() {
        return this.method.charAt(0) + this.method.substring(1).toLowerCase();
    }

    public static PaymentMethod detectMethod(String string) {
        switch (string.charAt(0)) {
            case '3': {
                return AMEX;
            }
            case '4': {
                return VISA;
            }
            case '6': {
                return DISCOVER;
            }
        }
        return MASTERCARD;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public PaymentMethod() {
        void var3_1;
        this.method = var3_1;
    }

    public String get() {
        return this.method;
    }
}


/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.webclient;

public class CookieJar$Key
implements Comparable {
    public static String NO_DOMAIN = "";
    public String name;
    public String domain;
    public String path;

    public String toString() {
        return "Key{domain='" + this.domain + "', path='" + this.path + "', name='" + this.name + "'}";
    }

    public int compareTo(CookieJar$Key cookieJar$Key) {
        int n = this.domain.compareTo(cookieJar$Key.domain);
        if (n == 0) {
            n = this.path.compareTo(cookieJar$Key.path);
        }
        if (n != 0) return n;
        return this.name.compareTo(cookieJar$Key.name);
    }

    public int hashCode() {
        int n = 31;
        int n2 = 1;
        n2 = 31 * n2 + (this.domain == null ? 0 : this.domain.hashCode());
        n2 = 31 * n2 + (this.name == null ? 0 : this.name.hashCode());
        return 31 * n2 + (this.path == null ? 0 : this.path.hashCode());
    }

    public CookieJar$Key(String string, String string2, String string3) {
        if (string == null || string.length() == 0) {
            this.domain = "";
        } else {
            while (string.charAt(0) == '.') {
                string = string.substring(1);
            }
            while (string.charAt(string.length() - 1) == '.') {
                string = string.substring(0, string.length() - 1);
            }
            if (string.length() == 0) {
                this.domain = "";
            } else {
                CharSequence[] charSequenceArray = string.split("\\.");
                int n = charSequenceArray.length - 1;
                for (int i = 0; i < charSequenceArray.length / 2; ++i, --n) {
                    String string4 = charSequenceArray[n];
                    charSequenceArray[n] = charSequenceArray[i];
                    charSequenceArray[i] = string4;
                }
                this.domain = String.join((CharSequence)".", charSequenceArray);
            }
        }
        this.path = string2 == null ? "" : string2;
        this.name = string3;
    }

    public int compareTo(Object object) {
        return this.compareTo((CookieJar$Key)object);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CookieJar$Key cookieJar$Key = (CookieJar$Key)object;
        if (this.domain == null ? cookieJar$Key.domain != null : !this.domain.equals(cookieJar$Key.domain)) {
            return false;
        }
        if (this.name == null ? cookieJar$Key.name != null : !this.name.equals(cookieJar$Key.name)) {
            return false;
        }
        if (this.path != null) return this.path.equals(cookieJar$Key.path);
        if (cookieJar$Key.path != null) return false;
        return true;
    }
}


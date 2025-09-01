package uz.pdp.util;

import java.util.Base64;

public class Base64Util {
    private Base64Util() {}

    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}

package com.jotangi.baseutils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class StringUtils {
    public static String base64(final String s) {

        if (s == null) {
            return "";
        }
        byte[] data = new byte[0];
        data = s.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);

    }

}

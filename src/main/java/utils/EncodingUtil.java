package utils;

import java.io.UnsupportedEncodingException;

public class EncodingUtil {
    public static String encode(String s) {
        try {
            return new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}

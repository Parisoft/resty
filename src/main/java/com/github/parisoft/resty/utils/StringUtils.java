package com.github.parisoft.resty.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern TRAILING_SLASHES_PATTERN = Pattern.compile("/*");
    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    public static final String EMPTY_STRING = "";

    public static String removeLeadingSlashes(String string) {
        return TRAILING_SLASHES_PATTERN.matcher(string).replaceFirst(EMPTY_STRING);
    }

    public static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

    public static String[] splitOnSlashes(String string) {
        if (string == null) {
            return new String[]{};
        }

        return SLASH_PATTERN.split(string);
    }

}

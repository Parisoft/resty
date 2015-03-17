package com.github.parisoft.resty.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern TRAILING_SLASHES_PATTERN = Pattern.compile("/*");
    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    public static final String EMPTY_STRING = "";

    public static String removeLeadingSlashes(String string) {
        return TRAILING_SLASHES_PATTERN.matcher(string).replaceFirst(EMPTY_STRING);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static List<String> splitAfterSlashes(String string) {
        if (isEmpty(string)) {
            return Collections.emptyList();
        }

        final Matcher matcher = SLASH_PATTERN.matcher(string);
        final List<Integer> indexes = new ArrayList<>();

        while (matcher.find()) {
            indexes.add(matcher.start());
            indexes.add(matcher.end());
        }

        if (indexes.isEmpty()) {
            return Arrays.asList(string);
        }

        final List<String> split = new ArrayList<>(indexes.size() / 2);

        if (indexes.get(0) > 0) {
            split.add(string.substring(0, indexes.get(0)));
        }

        for (int i = 1; i < indexes.size(); i += 2) {
            final int ini = indexes.get(i);
            final int end = (i == indexes.size() - 1) ? string.length() : indexes.get(i + 1);

            split.add(string.substring(ini, end));
        }

        return split;
    }

    public static String[] splitOnSlashes(String string) {
        if (string == null) {
            return new String[0];
        }

        return SLASH_PATTERN.split(string);
    }

    public static String emptyIfNull(String string) {
        if (string == null) {
            return "";
        }

        return string;
    }
}

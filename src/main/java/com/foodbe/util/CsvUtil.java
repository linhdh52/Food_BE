package com.foodbe.util;

import java.util.*;
import java.util.stream.Collectors;

public final class CsvUtil {
    private CsvUtil() {
    }

    public static String join(List<String> items) {
        if (items == null || items.isEmpty()) return null;
        List<String> cleaned = items.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        return cleaned.isEmpty() ? null : String.join(",", cleaned);
    }

    public static List<String> split(String csv) {
        if (csv == null || csv.trim().isEmpty()) return Collections.emptyList();
        String[] arr = csv.split(",");
        List<String> out = new ArrayList<>(arr.length);
        for (String s : arr) {
            String v = s.trim();
            if (!v.isEmpty()) out.add(v);
        }
        return out;
    }
}

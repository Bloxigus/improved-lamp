package com.github.bloxigus.improvedlamp.utils;

import java.util.List;
import java.util.Locale;

public class ListUtils {
    public static int listIndexOfCaseInsensitive(List<String> list, String toCheck) {
        if (toCheck == null) {
            for (int i = 0; i < list.size(); i++)
                if (list.get(i)==null)
                    return i;
        } else {
            String allLower = toCheck.toLowerCase(Locale.ROOT);
            for (int i = 0; i < list.size(); i++)
                if (allLower.equals(list.get(i).toLowerCase(Locale.ROOT)))
                    return i;
        }
        return -1;
    }
}

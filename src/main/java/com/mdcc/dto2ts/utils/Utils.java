package com.mdcc.dto2ts.utils;

import com.google.common.base.*;

import java.util.*;

public class Utils {

    private Utils() {
    }

    public static String getClassName(String fileName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fileName) + ".model";
    }

    public static String getClassNameFromTsQualifiedName(String name) {
        return Arrays.stream(name.split("\\$"))
            .reduce((a, b) -> b)
            .orElse("");
    }
}

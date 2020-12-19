package com.mdcc.dto2ts.utils;

import com.google.common.base.*;

public class Utils {

    private Utils() {
    }

    public static String getClassName(String fileName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fileName) + ".model";
    }

    public static String getClassNameFromTsQualifiedName(String name) {
        String[] split = name.split("\\$");
        return split[split.length - 1];
    }
}

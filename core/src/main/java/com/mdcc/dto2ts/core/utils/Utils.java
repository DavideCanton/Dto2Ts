package com.mdcc.dto2ts.core.utils;

import com.google.common.base.*;

import java.util.*;

public final class Utils
{
    private Utils()
    {
    }

    public static String getClassName(String fileName)
    {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fileName) + ".model";
    }

    public static String getClassNameFromTsQualifiedName(String name)
    {
        return Arrays.stream(name.split("\\$"))
            .reduce((a, b) -> b)
            .orElse("");
    }

    public static String getVisitorName(String symbol)
    {
        return String.format("I%sVisitor", CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, symbol));
    }

    public static String getVisitorPath(String symbol)
    {
        return String.format("./%s.visitor", symbol);
    }
}

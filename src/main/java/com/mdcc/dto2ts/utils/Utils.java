package com.mdcc.dto2ts.utils;

import com.google.common.base.*;
import cz.habarta.typescript.generator.*;

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

    public static boolean isBasicType(TsType tsType)
    {
        return tsType instanceof TsType.BasicType;
    }

    public static boolean isArrayType(TsType tsType)
    {
        return tsType instanceof TsType.BasicArrayType;
    }

    public static boolean isComplexType(TsType tsType)
    {
        return !isBasicType(tsType) && !isArrayType(tsType);
    }
}

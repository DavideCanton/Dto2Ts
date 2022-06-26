package com.mdcc.dto2ts.core.imports;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ImportNames
{
    public static final String JSON_CLASS = "JsonClass";
    public static final String JSON_PROPERTY = "JsonProperty";
    public static final String JSON_FLAG = "JsonFlag";
    public static final String JSON_DATE_ISO = "JsonDateISO";
    public static final String JSON_COMPLEX_PROPERTY = "JsonComplexProperty";
    public static final String JSON_ARRAY_OF_COMPLEX_PROPERTY = "JsonArrayOfComplexProperty";
    public static final String JSON_ARRAY = "JsonArray";
    public static final String SERIALIZE_FN = "SerializeFn";
    public static final String JSON_LOCALIZABLE_PROPERTY = "JsonLocalizableProperty";
    public static final String I_LOCALIZABLE_PROPERTY = "ILocalizableProperty";
    public static final String DOMAINS = "Domains";

    private static final Map<String, String> libraryImports = new HashMap<>();

    static
    {
        for (String s : Arrays.asList(JSON_PROPERTY, JSON_COMPLEX_PROPERTY, JSON_ARRAY, JSON_ARRAY_OF_COMPLEX_PROPERTY, JSON_CLASS, SERIALIZE_FN))
        {
            libraryImports.put(s, "at-json");
        }

        for (String s : Arrays.asList(JSON_FLAG, JSON_DATE_ISO, JSON_LOCALIZABLE_PROPERTY, I_LOCALIZABLE_PROPERTY, DOMAINS))
        {
            libraryImports.put(s, "creapp-common-lib");
        }
    }

    private ImportNames()
    {
    }

    public static String getLibraryImport(String symbol)
    {
        return libraryImports.get(symbol);
    }
}

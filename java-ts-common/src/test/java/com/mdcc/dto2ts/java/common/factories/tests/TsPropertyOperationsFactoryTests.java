package com.mdcc.dto2ts.java.common.factories.tests;

import com.mdcc.dto2ts.java.common.factories.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import lombok.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

public class TsPropertyOperationsFactoryTests
{
    private static final Map<String, TsType> types = new HashMap<>();

    @BeforeAll
    public static void setup()
    {
        val string = TsType.BasicType.String;
        val arrayOfString = new TsType.BasicArrayType(string);
        val complex = new TsType.GenericReferenceType(new Symbol("complex"));
        Function<TsType, TsType.NullableType> makeNullable = TsType.NullableType::new;

        types.put("string", string);
        types.put("arrayOfString", arrayOfString);
        types.put("nullableString", makeNullable.apply(string));
        types.put("nullableArrayOfString", makeNullable.apply(arrayOfString));
        types.put("complex", complex);
        types.put("nullableComplex", makeNullable.apply(complex));
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
        "string,string,getBasicType",
        "string,nullableString,getBasicType",
        ",nullableArrayOfString,getBasicType",
        ",arrayOfString,getBasicType",
        ",complex,getBasicType",
        ",nullableComplex,getBasicType",
        "arrayOfString,arrayOfString,getArrayType",
        "arrayOfString,nullableArrayOfString,getArrayType",
        ",nullableString,getArrayType",
        ",string,getArrayType",
        ",complex,getArrayType",
        ",nullableComplex,getArrayType",
        ",string,getComplexType",
        ",nullableString,getComplexType",
        ",arrayOfString,getComplexType",
        ",nullableArrayOfString,getComplexType",
        "complex,complex,getComplexType",
        "complex,nullableComplex,getComplexType",
    })
    public void testGetClassName(String expected, String name, String methodName)
    {
        if (!types.containsKey(expected) && expected != null)
            throw new IllegalArgumentException("Invalid key: " + expected);
        if (!types.containsKey(name))
            throw new IllegalArgumentException("Invalid key: " + name);

        Optional<TsType> expectedOpt = Optional.ofNullable(expected).map(types::get);
        val method = TsPropertyOperationsFactory.class.getMethod(methodName, TsType.class);

        assertEquals(
            expectedOpt,
            method.invoke(null, types.get(name))
        );
    }
}

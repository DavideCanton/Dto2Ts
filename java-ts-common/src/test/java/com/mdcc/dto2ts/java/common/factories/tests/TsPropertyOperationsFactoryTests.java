package com.mdcc.dto2ts.java.common.factories.tests;

import com.mdcc.dto2ts.java.common.factories.*;
import cz.habarta.typescript.generator.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TsPropertyOperationsFactoryTests
{
    private final TsType string = TsType.BasicType.String;
    private final TsType arrayOfString = new TsType.BasicArrayType(string);
    private final TsType nullableString = new TsType.NullableType(string);
    private final TsType nullableArrayOfString = new TsType.NullableType(arrayOfString);

    @Test
    public void getBasicTypeWithBasicType()
    {
        assertEquals(
            Optional.of(string),
            TsPropertyOperationsFactory.getBasicType(string)
        );
    }

    @Test
    public void getBasicTypeWithBasicNullableType()
    {
        assertEquals(
            Optional.of(string),
            TsPropertyOperationsFactory.getBasicType(nullableString)
        );
    }

    @Test
    public void getBasicTypeWithNonBasicNullableType()
    {
        assertEquals(
            Optional.empty(),
            TsPropertyOperationsFactory.getBasicType(nullableArrayOfString)
        );
    }

    @Test
    public void getBasicTypeWithNonBasicType()
    {
        assertEquals(
            Optional.empty(),
            TsPropertyOperationsFactory.getBasicType(arrayOfString)
        );
    }

    @Test
    public void getArrayTypeWithArrayType()
    {
        assertEquals(
            Optional.of(arrayOfString),
            TsPropertyOperationsFactory.getArrayType(arrayOfString)
        );
    }

    @Test
    public void getArrayTypeWithArrayNullableType()
    {
        assertEquals(
            Optional.of(arrayOfString),
            TsPropertyOperationsFactory.getArrayType(nullableArrayOfString)
        );
    }

    @Test
    public void getArrayTypeWithNonArrayNullableType()
    {
        assertEquals(
            Optional.empty(),
            TsPropertyOperationsFactory.getArrayType(nullableString)
        );
    }

    @Test
    public void getArrayTypeWithNonArrayType()
    {
        assertEquals(
            Optional.empty(),
            TsPropertyOperationsFactory.getArrayType(string)
        );
    }
}

package com.mdcc.dto2ts.core.context.types;

import lombok.*;

@Getter
public class BasicType implements TsType
{
    private final String name;

    public static final String STRING = "string";
    public static final String BOOLEAN = "boolean";
    public static final String NUMBER = "number";
    public static final String DATE = "Date";

    protected BasicType(String name)
    {
        this.name = name;
    }

    public static BasicType string()
    {
        return new BasicType(STRING);
    }

    public static BasicType bool()
    {
        return new BasicType(BOOLEAN);
    }

    public static BasicType number()
    {
        return new BasicType(NUMBER);
    }

    public static BasicType date()
    {
        return new BasicType(DATE);
    }

    public boolean isString()
    {
        return this.getName().equals(STRING);
    }

    public boolean isBoolean()
    {
        return this.getName().equals(BOOLEAN);
    }

    public boolean isNumber()
    {
        return this.getName().equals(NUMBER);
    }

    public boolean isDate()
    {
        return this.getName().equals(DATE);
    }
}

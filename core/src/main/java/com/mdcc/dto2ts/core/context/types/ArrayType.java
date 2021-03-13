package com.mdcc.dto2ts.core.context.types;

import lombok.*;

@Getter
public class ArrayType implements TsType
{
    private final TsType arrayElementType;

    public ArrayType(TsType arrayElementType)
    {
        this.arrayElementType = arrayElementType;
    }
}

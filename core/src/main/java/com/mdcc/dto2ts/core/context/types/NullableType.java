package com.mdcc.dto2ts.core.context.types;

import lombok.*;

@Getter
@Setter
public class NullableType extends TsType
{
    private TsType type;

    public NullableType(TsType type)
    {
        super(type.getName());
        this.type = type;
    }
}

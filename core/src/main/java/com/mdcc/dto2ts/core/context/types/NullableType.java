package com.mdcc.dto2ts.core.context.types;

import lombok.*;

import java.util.*;

@Getter
@Setter
public class NullableType implements TsType
{
    @NonNull
    private TsType type;

    public NullableType(TsType type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullableType that = (NullableType) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), type);
    }
}

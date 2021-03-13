package com.mdcc.dto2ts.core.context.types;

import lombok.*;

import java.util.*;

@Getter
@Setter
@With
public class GenericBasicType extends BasicType
{
    private List<TsType> genericTypes = new ArrayList<>();

    public GenericBasicType(String name)
    {
        super(name);
    }

    public GenericBasicType(String name, TsType type)
    {
        this(name);
        this.genericTypes.add(type);
    }

    public GenericBasicType(String name, List<TsType> types)
    {
        this(name);
        this.genericTypes.addAll(types);
    }
}

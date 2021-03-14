package com.mdcc.dto2ts.core.context;

public interface PropertyRefTransformer
{
    PropertyRef makeNullable(PropertyRef propertyRef);

    PropertyRef makeBoolean(PropertyRef propertyRef);

    PropertyRef makeString(PropertyRef propertyRef);

    PropertyRef makeDomain(PropertyRef propertyRef);
}

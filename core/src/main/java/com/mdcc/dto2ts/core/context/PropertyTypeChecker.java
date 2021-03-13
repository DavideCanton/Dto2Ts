package com.mdcc.dto2ts.core.context;

public interface PropertyTypeChecker
{
    boolean isArrayType(PropertyRef propertyRef);
    boolean isComplexType(PropertyRef propertyRef);
    boolean isBasicType(PropertyRef propertyRef);
    boolean isString(PropertyRef propertyRef);
}

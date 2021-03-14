package com.mdcc.dto2ts.java.main.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.*;

import static com.mdcc.dto2ts.java.main.factories.TsPropertyOperationsFactory.*;

public class TsPropertyTypeChecker implements PropertyTypeChecker
{
    @Override
    public boolean isArrayType(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.isArrayType(getProperty(propertyRef).getTsType());
    }

    @Override
    public boolean isComplexType(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.isComplexType(getProperty(propertyRef).getTsType());
    }

    @Override
    public boolean isBasicType(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.isBasicType(getProperty(propertyRef).getTsType());
    }

    @Override
    public boolean isString(PropertyRef propertyRef)
    {
        return getProperty(propertyRef).getTsType().equals(TsType.String);
    }
}

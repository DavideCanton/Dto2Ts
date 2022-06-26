package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.context.PropertyTypeChecker;
import cz.habarta.typescript.generator.TsType;

import static com.mdcc.dto2ts.java.common.factories.TsPropertyOperationsFactory.getProperty;

public class TsPropertyTypeChecker implements PropertyTypeChecker
{
    @Override
    public boolean isArrayType(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.getArrayType(getProperty(propertyRef).getTsType()).isPresent();
    }

    @Override
    public boolean isComplexType(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.getComplexType(getProperty(propertyRef).getTsType()).isPresent();
    }

    @Override
    public boolean isBasicType(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.getBasicType(getProperty(propertyRef).getTsType()).isPresent();
    }

    @Override
    public boolean isString(PropertyRef propertyRef)
    {
        return getProperty(propertyRef).getTsType().equals(TsType.String);
    }
}

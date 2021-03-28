package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;

import static com.mdcc.dto2ts.core.imports.ImportNames.*;
import static com.mdcc.dto2ts.java.common.factories.TsPropertyOperationsFactory.*;

public class TsPropertyRefTransformer implements PropertyRefTransformer
{
    @Override
    public PropertyRef makeNullable(PropertyRef propertyRef)
    {
        TsPropertyModel property = getProperty(propertyRef);
        return new TsPropertyRef(
            property.withTsType(new TsType.NullableType(property.getTsType()))
        );
    }

    @Override
    public PropertyRef makeBoolean(PropertyRef propertyRef)
    {
        TsPropertyModel property = getProperty(propertyRef);
        return new TsPropertyRef(
            property.withTsType(TsType.BasicType.Boolean)
        );
    }

    @Override
    public PropertyRef makeString(PropertyRef propertyRef)
    {
        TsPropertyModel property = getProperty(propertyRef);
        return new TsPropertyRef(
            property.withTsType(TsType.BasicType.String)
        );
    }

    @Override
    public PropertyRef makeDomain(PropertyRef propertyRef)
    {
        TsPropertyModel property = getProperty(propertyRef);
        return new TsPropertyRef(
            new TsPropertyModel(
                property.name,
                new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String),
                TsModifierFlags.None,
                true,
                null
            )
        );
    }
}

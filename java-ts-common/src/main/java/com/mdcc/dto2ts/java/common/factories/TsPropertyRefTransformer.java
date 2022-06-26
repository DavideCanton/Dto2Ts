package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.context.PropertyRefTransformer;
import cz.habarta.typescript.generator.TsType;
import cz.habarta.typescript.generator.emitter.TsModifierFlags;
import cz.habarta.typescript.generator.emitter.TsPropertyModel;

import static com.mdcc.dto2ts.core.imports.ImportNames.I_LOCALIZABLE_PROPERTY;
import static com.mdcc.dto2ts.java.common.factories.TsPropertyOperationsFactory.getProperty;

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

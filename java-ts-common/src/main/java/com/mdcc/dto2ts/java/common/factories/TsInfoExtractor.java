package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.DecoratorRef;
import com.mdcc.dto2ts.core.context.InfoExtractor;
import com.mdcc.dto2ts.core.context.PropertyRef;
import cz.habarta.typescript.generator.TsType;

import java.util.Optional;

public class TsInfoExtractor implements InfoExtractor
{
    @Override
    public String getPropertyName(PropertyRef propertyRef)
    {
        return TsPropertyOperationsFactory.getProperty(propertyRef).name;
    }

    @Override
    public Optional<String> getPropertyTypeName(PropertyRef propertyRef)
    {
        TsType type = TsPropertyOperationsFactory.getProperty(propertyRef).getTsType();

        return Optional.of(type)
            .filter(t -> t instanceof TsType.BasicType)
            .map(TsType.BasicType.class::cast)
            .map(p -> p.name);
    }

    @Override
    public String getDecoratorIdentifier(DecoratorRef decoratorRef)
    {
        return TsPropertyOperationsFactory.getDecorator(decoratorRef).getIdentifierReference().getIdentifier();
    }
}

package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.*;

import java.util.*;

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

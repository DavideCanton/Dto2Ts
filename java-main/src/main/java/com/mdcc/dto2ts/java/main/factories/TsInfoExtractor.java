package com.mdcc.dto2ts.java.main.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.*;

import java.util.*;

import static com.mdcc.dto2ts.java.main.factories.TsPropertyOperationsFactory.*;

public class TsInfoExtractor implements InfoExtractor
{
    @Override
    public String getPropertyName(PropertyRef propertyRef)
    {
        return getProperty(propertyRef).name;
    }

    @Override
    public Optional<String> getPropertyTypeName(PropertyRef propertyRef)
    {
        TsType type = getProperty(propertyRef).getTsType();

        return Optional.of(type)
            .filter(t -> t instanceof TsType.BasicType)
            .map(TsType.BasicType.class::cast)
            .map(p -> p.name);
    }

    @Override
    public String getDecoratorIdentifier(DecoratorRef decoratorRef)
    {
        return getDecorator(decoratorRef).getIdentifierReference().getIdentifier();
    }
}

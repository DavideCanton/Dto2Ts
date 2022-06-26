package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.DecoratorRef;
import com.mdcc.dto2ts.core.context.PropertyConstructor;
import com.mdcc.dto2ts.core.context.PropertyRef;

import java.util.List;
import java.util.stream.Collectors;

public class TsPropertyConstructor implements PropertyConstructor
{
    @Override
    public Object buildPropertyWithDecorators(PropertyRef propertyRef, List<DecoratorRef> decorators)
    {
        return TsPropertyOperationsFactory.getProperty(propertyRef)
            .withDecorators(
                decorators.stream()
                    .map(TsPropertyOperationsFactory::getDecorator)
                    .collect(Collectors.toList())
            );
    }
}

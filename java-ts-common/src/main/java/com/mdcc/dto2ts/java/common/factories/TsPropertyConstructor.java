package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;

import java.util.*;
import java.util.stream.*;

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

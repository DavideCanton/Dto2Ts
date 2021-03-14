package com.mdcc.dto2ts.java.main.factories;

import com.mdcc.dto2ts.core.context.*;

import java.util.*;
import java.util.stream.*;

import static com.mdcc.dto2ts.java.main.factories.TsPropertyOperationsFactory.*;

public class TsPropertyConstructor implements PropertyConstructor
{
    @Override
    public Object buildPropertyWithDecorators(PropertyRef propertyRef, List<DecoratorRef> decorators)
    {
        return getProperty(propertyRef)
            .withDecorators(
                decorators.stream()
                    .map(TsPropertyOperationsFactory::getDecorator)
                    .collect(Collectors.toList())
            );
    }
}

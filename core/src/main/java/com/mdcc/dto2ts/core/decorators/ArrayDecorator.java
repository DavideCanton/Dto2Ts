package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.PropertyContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ArrayDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context)
            .filter(p -> p.getPropertyOperationsFactory().createPropertyTypeChecker().isArrayType(p.getPropertyRef()))
            .map(p -> context.addDecorator(p.getPropertyOperationsFactory().createDecoratorBuilder().buildArrayDecorator(p.getPropertyRef())));
    }
}

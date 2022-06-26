package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.PropertyContext;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ComplexDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        val factory = context.getPropertyOperationsFactory();
        return Optional.of(context)
            .filter(c -> factory.createPropertyTypeChecker().isComplexType(c.getPropertyRef()))
            .map(c -> c.addDecorator(factory.createDecoratorBuilder().buildComplexDecorator(c.getPropertyRef())));
    }
}

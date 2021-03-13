package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class ComplexDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context)
            .filter(p -> p.getPropertyOperationsFactory().createPropertyTypeChecker().isComplexType(p.getPropertyRef()))
            .map(p -> context.addDecorator(p.getPropertyOperationsFactory().createDecoratorBuilder().buildComplexDecorator(p.getPropertyRef())));
    }
}

package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;
import org.springframework.stereotype.*;

import java.util.*;


@Component
public class SimpleDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context)
            .filter(p -> p.getPropertyOperationsFactory().createPropertyTypeChecker().isBasicType(p.getPropertyRef()))
            .flatMap(p -> p.getPropertyOperationsFactory().createDecoratorBuilder().buildSimpleDecorator(p.getPropertyRef()))
            .map(context::addDecorator);
    }
}

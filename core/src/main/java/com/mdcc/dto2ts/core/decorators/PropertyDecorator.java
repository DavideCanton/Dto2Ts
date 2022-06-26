package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.ContextTransformer;
import com.mdcc.dto2ts.core.context.PropertyContext;

import java.util.Optional;

@FunctionalInterface
public interface PropertyDecorator extends ContextTransformer
{
    Optional<PropertyContext> decorateProperty(PropertyContext context);

    default Optional<PropertyContext> transformContext(PropertyContext context)
    {
        return this.decorateProperty(context);
    }
}

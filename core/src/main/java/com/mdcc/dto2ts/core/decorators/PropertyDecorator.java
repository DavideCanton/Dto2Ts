package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;

import java.util.*;

@FunctionalInterface
public interface PropertyDecorator extends ContextTransformer
{
    Optional<PropertyContext> decorateProperty(PropertyContext context);

    default Optional<PropertyContext> transformContext(PropertyContext context)
    {
        return this.decorateProperty(context);
    }
}

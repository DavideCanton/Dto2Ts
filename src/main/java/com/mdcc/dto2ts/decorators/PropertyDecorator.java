package com.mdcc.dto2ts.decorators;

import com.mdcc.dto2ts.context.*;

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

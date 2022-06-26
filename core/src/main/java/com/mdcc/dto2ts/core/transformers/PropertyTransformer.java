package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.ContextTransformer;
import com.mdcc.dto2ts.core.context.PropertyContext;

import java.util.Optional;

@FunctionalInterface
public interface PropertyTransformer extends ContextTransformer
{
    Optional<PropertyContext> transformProperty(PropertyContext context);

    default Optional<PropertyContext> transformContext(PropertyContext context)
    {
        return this.transformProperty(context);
    }
}

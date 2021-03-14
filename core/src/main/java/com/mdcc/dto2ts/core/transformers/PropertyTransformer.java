package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.*;

import java.util.*;

@FunctionalInterface
public interface PropertyTransformer extends ContextTransformer
{
    Optional<PropertyContext> transformProperty(PropertyContext context);

    default Optional<PropertyContext> transformContext(PropertyContext context)
    {
        return this.transformProperty(context);
    }
}

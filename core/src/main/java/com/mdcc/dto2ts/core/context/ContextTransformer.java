package com.mdcc.dto2ts.core.context;

import java.util.Optional;

@FunctionalInterface
public interface ContextTransformer
{
    Optional<PropertyContext> transformContext(PropertyContext context);
}

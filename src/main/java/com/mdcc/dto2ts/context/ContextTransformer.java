package com.mdcc.dto2ts.context;

import java.util.*;

@FunctionalInterface
public interface ContextTransformer
{
    Optional<PropertyContext> transformContext(PropertyContext context);
}

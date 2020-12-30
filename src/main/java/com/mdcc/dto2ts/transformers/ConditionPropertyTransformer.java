package com.mdcc.dto2ts.transformers;

import com.mdcc.dto2ts.context.*;

import java.util.*;

public abstract class ConditionPropertyTransformer implements PropertyTransformer
{
    protected abstract boolean canTransform(PropertyContext context);

    protected abstract PropertyContext doTransform(PropertyContext context);

    @Override
    public Optional<PropertyContext> transformProperty(PropertyContext context)
    {
        return Optional.of(context)
            .filter(this::canTransform)
            .map(this::doTransform);
    }
}

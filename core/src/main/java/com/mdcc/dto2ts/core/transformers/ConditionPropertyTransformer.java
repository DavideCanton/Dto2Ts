package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.PropertyContext;

import java.util.Optional;

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

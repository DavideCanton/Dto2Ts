package com.mdcc.dto2ts.core.context;

import java.util.*;
import java.util.function.*;

public abstract class CachingPropertyOperationsFactory extends PropertyOperationsFactory
{
    private final Map<Supplier<?>, Object> cache = new HashMap<>();

    @Override
    public DecoratorBuilder createDecoratorBuilder()
    {
        return cache(this::innerCreateDecoratorBuilder);
    }

    @Override
    public InfoExtractor createInfoExtractor()
    {
        return cache(this::innerCreateInfoExtractor);
    }

    @Override
    public PropertyConstructor createPropertyConstructor()
    {
        return cache(this::innerCreatePropertyConstructor);
    }

    @Override
    public PropertyRefTransformer createPropertyRefTransformer()
    {
        return cache(this::innerCreatePropertyRefTransformer);
    }

    @Override
    public PropertyTypeChecker createPropertyTypeChecker()
    {
        return cache(this::innerCreatePropertyTypeChecker);
    }

    @SuppressWarnings("unchecked")
    private <T> T cache(Supplier<T> fn)
    {
        return (T) cache.computeIfAbsent(fn, Supplier::get);
    }

    protected abstract PropertyTypeChecker innerCreatePropertyTypeChecker();

    protected abstract PropertyRefTransformer innerCreatePropertyRefTransformer();

    protected abstract PropertyConstructor innerCreatePropertyConstructor();

    protected abstract InfoExtractor innerCreateInfoExtractor();

    protected abstract DecoratorBuilder innerCreateDecoratorBuilder();
}

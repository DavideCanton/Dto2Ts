package com.mdcc.dto2ts.core.context;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter(AccessLevel.PRIVATE)
public abstract class CachingPropertyOperationsFactory extends PropertyOperationsFactory
{
    private DecoratorBuilder decoratorBuilder;
    private InfoExtractor infoExtractor;
    private PropertyConstructor propertyConstructor;
    private PropertyRefTransformer propertyRefTransformer;
    private PropertyTypeChecker propertyTypeChecker;

    @Override
    public DecoratorBuilder createDecoratorBuilder()
    {
        return cache(this::innerCreateDecoratorBuilder, this::getDecoratorBuilder, this::setDecoratorBuilder);
    }

    @Override
    public InfoExtractor createInfoExtractor()
    {
        return cache(this::innerCreateInfoExtractor, this::getInfoExtractor, this::setInfoExtractor);
    }

    @Override
    public PropertyConstructor createPropertyConstructor()
    {
        return cache(this::innerCreatePropertyConstructor, this::getPropertyConstructor, this::setPropertyConstructor);
    }

    @Override
    public PropertyRefTransformer createPropertyRefTransformer()
    {
        return cache(this::innerCreatePropertyRefTransformer, this::getPropertyRefTransformer, this::setPropertyRefTransformer);
    }

    @Override
    public PropertyTypeChecker createPropertyTypeChecker()
    {
        return cache(this::innerCreatePropertyTypeChecker, this::getPropertyTypeChecker, this::setPropertyTypeChecker);
    }

    private <T> T cache(Supplier<T> fn, Supplier<T> getter, Consumer<T> setter)
    {
        var value = getter.get();
        if (value != null)
            return value;
        value = fn.get();
        setter.accept(value);
        return value;
    }

    protected abstract PropertyTypeChecker innerCreatePropertyTypeChecker();

    protected abstract PropertyRefTransformer innerCreatePropertyRefTransformer();

    protected abstract PropertyConstructor innerCreatePropertyConstructor();

    protected abstract InfoExtractor innerCreateInfoExtractor();

    protected abstract DecoratorBuilder innerCreateDecoratorBuilder();
}

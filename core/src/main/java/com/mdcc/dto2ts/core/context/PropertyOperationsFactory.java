package com.mdcc.dto2ts.core.context;

public abstract class PropertyOperationsFactory
{
    public abstract DecoratorBuilder createDecoratorBuilder();

    public abstract InfoExtractor createInfoExtractor();

    public abstract PropertyConstructor createPropertyConstructor();

    public abstract PropertyRefTransformer createPropertyRefTransformer();

    public abstract PropertyTypeChecker createPropertyTypeChecker();
}

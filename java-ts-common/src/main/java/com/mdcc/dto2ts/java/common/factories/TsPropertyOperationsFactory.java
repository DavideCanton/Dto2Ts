package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;

public class TsPropertyOperationsFactory extends CachingPropertyOperationsFactory
{
    public static TsPropertyModel getProperty(PropertyRef propertyRef)
    {
        return getValue(propertyRef, TsPropertyModel.class);
    }

    public static TsDecorator getDecorator(DecoratorRef decoratorRef)
    {
        return getValue(decoratorRef, TsDecorator.class);
    }

    private static <T> T getValue(GenericRef ref, Class<? extends T> clazz)
    {
        return clazz.cast(ref.getUnderlyingValue());
    }

    public static boolean isBasicType(TsType tsType)
    {
        return tsType instanceof TsType.BasicType;
    }

    public static boolean isArrayType(TsType tsType)
    {
        return tsType instanceof TsType.BasicArrayType;
    }

    public static boolean isComplexType(TsType tsType)
    {
        return !isBasicType(tsType) && !isArrayType(tsType);
    }

    @Override
    protected PropertyTypeChecker innerCreatePropertyTypeChecker()
    {
        return new TsPropertyTypeChecker();
    }

    @Override
    protected PropertyRefTransformer innerCreatePropertyRefTransformer()
    {
        return new TsPropertyRefTransformer();
    }

    @Override
    protected PropertyConstructor innerCreatePropertyConstructor()
    {
        return new TsPropertyConstructor();
    }

    @Override
    protected InfoExtractor innerCreateInfoExtractor()
    {
        return new TsInfoExtractor();
    }

    @Override
    protected DecoratorBuilder innerCreateDecoratorBuilder()
    {
        return new TsDecoratorBuilder();
    }

}

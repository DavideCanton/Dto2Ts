package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;
import cyclops.control.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;

import java.util.*;

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

    public static Optional<TsType> getBasicType(TsType tsType)
    {
        return getType(tsType, TsType.BasicType.class);
    }

    public static Optional<TsType> getArrayType(TsType tsType)
    {
        return getType(tsType, TsType.BasicArrayType.class);
    }

    public static Optional<TsType> getComplexType(TsType tsType)
    {
        return Option.fromOptional(getBasicType(tsType))
            .toEither(null)
            .swap()
            .flatMap(__ ->
                Option.fromOptional(getArrayType(tsType))
                    .toEither(null)
                    .swap()
            )
            .flatMap(__ ->
                Option.fromOptional(getType(tsType, null))
                    .toEither(null)
            )
            .toOptional();
    }

    private static Optional<TsType> getType(TsType type, Class<?> targetClass)
    {
        return Optional.of(type)
            .map(t -> t instanceof TsType.NullableType ? ((TsType.NullableType) t).type : t)
            .filter(t -> targetClass == null || targetClass.isInstance(t));
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

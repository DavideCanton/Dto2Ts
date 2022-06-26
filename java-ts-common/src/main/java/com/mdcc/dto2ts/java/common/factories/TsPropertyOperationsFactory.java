package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.CachingPropertyOperationsFactory;
import com.mdcc.dto2ts.core.context.DecoratorBuilder;
import com.mdcc.dto2ts.core.context.DecoratorRef;
import com.mdcc.dto2ts.core.context.GenericRef;
import com.mdcc.dto2ts.core.context.InfoExtractor;
import com.mdcc.dto2ts.core.context.PropertyConstructor;
import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.context.PropertyRefTransformer;
import com.mdcc.dto2ts.core.context.PropertyTypeChecker;
import cyclops.control.Option;
import cz.habarta.typescript.generator.TsType;
import cz.habarta.typescript.generator.emitter.TsDecorator;
import cz.habarta.typescript.generator.emitter.TsPropertyModel;

import java.util.Optional;

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

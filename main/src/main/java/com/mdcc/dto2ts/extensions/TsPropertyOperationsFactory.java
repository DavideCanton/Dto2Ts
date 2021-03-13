package com.mdcc.dto2ts.extensions;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.utils.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;

import java.util.*;
import java.util.stream.*;

import static com.mdcc.dto2ts.core.imports.ImportNames.*;

public class TsPropertyOperationsFactory extends CachingPropertyOperationsFactory
{
    private TsPropertyModel getProperty(PropertyRef propertyRef)
    {
        return getValue(propertyRef, TsPropertyModel.class);
    }

    private TsDecorator getDecorator(DecoratorRef decoratorRef)
    {
        return getValue(decoratorRef, TsDecorator.class);
    }

    private <T> T getValue(GenericRef ref, Class<? extends T> clazz)
    {
        return clazz.cast(ref.getUnderlyingValue());
    }

    private static boolean isBasicType(TsType tsType)
    {
        return tsType instanceof TsType.BasicType;
    }

    private static boolean isArrayType(TsType tsType)
    {
        return tsType instanceof TsType.BasicArrayType;
    }

    private static boolean isComplexType(TsType tsType)
    {
        return !isBasicType(tsType) && !isArrayType(tsType);
    }

    @Override
    protected PropertyTypeChecker innerCreatePropertyTypeChecker()
    {
        return new PropertyTypeChecker()
        {
            @Override
            public boolean isArrayType(PropertyRef propertyRef)
            {
                return TsPropertyOperationsFactory.isArrayType(getProperty(propertyRef).getTsType());
            }

            @Override
            public boolean isComplexType(PropertyRef propertyRef)
            {
                return TsPropertyOperationsFactory.isComplexType(getProperty(propertyRef).getTsType());
            }

            @Override
            public boolean isBasicType(PropertyRef propertyRef)
            {
                return TsPropertyOperationsFactory.isBasicType(getProperty(propertyRef).getTsType());
            }

            @Override
            public boolean isString(PropertyRef propertyRef)
            {
                return getProperty(propertyRef).getTsType().equals(TsType.String);
            }
        };
    }

    @Override
    protected PropertyRefTransformer innerCreatePropertyRefTransformer()
    {
        return new PropertyRefTransformer()
        {
            @Override
            public PropertyRef makeNullable(PropertyRef propertyRef)
            {
                TsPropertyModel property = getProperty(propertyRef);
                return new TsPropertyRef(
                    property.withTsType(new TsType.NullableType(property.getTsType()))
                );
            }

            @Override
            public PropertyRef makeBoolean(PropertyRef propertyRef)
            {
                TsPropertyModel property = getProperty(propertyRef);
                return new TsPropertyRef(
                    property.withTsType(TsType.BasicType.Boolean)
                );
            }

            @Override
            public PropertyRef makeString(PropertyRef propertyRef)
            {
                TsPropertyModel property = getProperty(propertyRef);
                return new TsPropertyRef(
                    property.withTsType(TsType.BasicType.String)
                );
            }

            @Override
            public PropertyRef makeDomain(PropertyRef propertyRef)
            {
                TsPropertyModel property = getProperty(propertyRef);
                return new TsPropertyRef(
                    new TsPropertyModel(
                        property.name,
                        new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String),
                        TsModifierFlags.None,
                        true,
                        null
                    )
                );
            }
        };
    }

    @SuppressWarnings("Convert2Lambda")
    @Override
    protected PropertyConstructor innerCreatePropertyConstructor()
    {
        return new PropertyConstructor()
        {
            @Override
            public Object buildPropertyWithDecorators(PropertyRef propertyRef, List<DecoratorRef> decorators)
            {
                return TsPropertyOperationsFactory.this.getProperty(propertyRef)
                    .withDecorators(
                        decorators.stream()
                            .map(TsPropertyOperationsFactory.this::getDecorator)
                            .collect(Collectors.toList())
                    );
            }
        };
    }

    @Override
    protected InfoExtractor innerCreateInfoExtractor()
    {
        return new InfoExtractor()
        {
            @Override
            public String getPropertyName(PropertyRef propertyRef)
            {
                return getProperty(propertyRef).name;
            }

            @Override
            public Optional<String> getPropertyTypeName(PropertyRef propertyRef)
            {
                TsType type = getProperty(propertyRef).getTsType();

                return Optional.of(type)
                    .filter(t -> t instanceof TsType.BasicType)
                    .map(TsType.BasicType.class::cast)
                    .map(p -> p.name);
            }

            @Override
            public String getDecoratorIdentifier(DecoratorRef decoratorRef)
            {
                return getDecorator(decoratorRef).getIdentifierReference().getIdentifier();
            }
        };
    }

    @Override
    protected DecoratorBuilder innerCreateDecoratorBuilder()
    {
        return new DecoratorBuilder()
        {


            @Override
            public DecoratorRef buildArrayDecorator(PropertyRef propertyRef)
            {
                TsType element = ((TsType.BasicArrayType) getProperty(propertyRef).getTsType()).elementType;
                String[] split = element.toString().split("\\$");

                TsDecorator decorator;
                if (isBasicType(element))
                    decorator = new TsDecorator(
                        new TsIdentifierReference(JSON_ARRAY),
                        Collections.emptyList()
                    );
                else
                    decorator = new TsDecorator(
                        new TsIdentifierReference(JSON_ARRAY_OF_COMPLEX_PROPERTY),
                        Collections.singletonList(
                            new TsIdentifierReference(split[split.length - 1])
                        )
                    );
                return new TsDecoratorRef(decorator);
            }

            @Override
            public DecoratorRef buildComplexDecorator(PropertyRef propertyRef)
            {
                TsType type = getProperty(propertyRef).tsType;
                String name = Utils.getClassNameFromTsQualifiedName(type.toString());

                TsDecorator decorator = new TsDecorator(
                    new TsIdentifierReference(JSON_COMPLEX_PROPERTY),
                    Collections.singletonList(new TsIdentifierReference(name))
                );
                return new TsDecoratorRef(decorator);
            }

            @Override
            public DecoratorRef buildDomainDecorator(PropertyRef propertyRef, String domain)
            {
                TsDecorator decorator = new TsDecorator(
                    new TsIdentifierReference(JSON_LOCALIZABLE_PROPERTY),
                    Collections.singletonList(
                        new TsMemberExpression(
                            new TsIdentifierReference(DOMAINS),
                            domain
                        )
                    )
                );
                return new TsDecoratorRef(decorator);
            }

            @Override
            public Optional<DecoratorRef> buildSimpleDecorator(PropertyRef propertyRef)
            {
                return Optional.of(getProperty(propertyRef))
                    .map(TsProperty::getTsType)
                    .map(TsType.BasicType.class::cast)
                    .map(type -> type.name)
                    .flatMap(type ->
                    {
                        String ret = null;
                        switch (type)
                        {
                            case "string":
                            case "number":
                                ret = JSON_PROPERTY;
                                break;
                            case "boolean":
                                ret = JSON_FLAG;
                                break;
                            case "Date":
                                ret = JSON_DATE_ISO;
                                break;
                            default:
                                break;

                        }
                        return Optional.ofNullable(ret);
                    })
                    .map(tsIdentifierReference -> new TsDecorator(
                        new TsIdentifierReference(tsIdentifierReference),
                        Collections.emptyList()
                    ))
                    .map(TsDecoratorRef::new);
            }

        };
    }
}

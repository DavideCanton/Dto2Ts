package com.mdcc.dto2ts.java.main.factories;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.utils.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;

import java.util.*;

import static com.mdcc.dto2ts.core.imports.ImportNames.*;
import static com.mdcc.dto2ts.java.main.factories.TsPropertyOperationsFactory.*;

public class TsDecoratorBuilder implements DecoratorBuilder
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

}

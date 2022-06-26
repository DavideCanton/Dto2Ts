package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.DecoratorBuilder;
import com.mdcc.dto2ts.core.context.DecoratorRef;
import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.utils.Utils;
import cz.habarta.typescript.generator.TsProperty;
import cz.habarta.typescript.generator.TsType;
import cz.habarta.typescript.generator.emitter.TsDecorator;
import cz.habarta.typescript.generator.emitter.TsIdentifierReference;
import cz.habarta.typescript.generator.emitter.TsMemberExpression;

import java.util.Collections;
import java.util.Optional;

import static com.mdcc.dto2ts.core.imports.ImportNames.DOMAINS;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_ARRAY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_ARRAY_OF_COMPLEX_PROPERTY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_COMPLEX_PROPERTY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_DATE_ISO;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_FLAG;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_LOCALIZABLE_PROPERTY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_PROPERTY;
import static com.mdcc.dto2ts.java.common.factories.TsPropertyOperationsFactory.getBasicType;
import static com.mdcc.dto2ts.java.common.factories.TsPropertyOperationsFactory.getProperty;

public class TsDecoratorBuilder implements DecoratorBuilder
{
    @Override
    public DecoratorRef buildArrayDecorator(PropertyRef propertyRef)
    {
        TsType element = ((TsType.BasicArrayType) getProperty(propertyRef).getTsType()).elementType;
        String[] split = element.toString().split("\\$");

        TsDecorator decorator;
        if (getBasicType(element).isPresent())
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
            .flatMap(TsPropertyOperationsFactory::getBasicType)
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

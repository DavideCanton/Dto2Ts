package com.mdcc.dto2ts.decorators;

import com.mdcc.dto2ts.context.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.imports.ImportNames.JSON_ARRAY;
import static com.mdcc.dto2ts.imports.ImportNames.JSON_ARRAY_OF_COMPLEX_PROPERTY;
import static com.mdcc.dto2ts.utils.Utils.isArrayType;
import static com.mdcc.dto2ts.utils.Utils.isBasicType;

@Component
public class ArrayDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context.getPropertyModel())
            .filter(p -> isArrayType(p.getTsType()))
            .map(this::getArrayElementType)
            .map(element -> context.addDecorator(buildArrayDecorator(element)));
    }

    private TsType getArrayElementType(TsPropertyModel property)
    {
        return ((TsType.BasicArrayType) property.getTsType()).elementType;
    }

    private TsDecorator buildArrayDecorator(TsType element)
    {
        String[] split = element.toString().split("\\$");

        if (isBasicType(element))
            return new TsDecorator(
                new TsIdentifierReference(JSON_ARRAY),
                Collections.emptyList());
        else
            return new TsDecorator(
                new TsIdentifierReference(JSON_ARRAY_OF_COMPLEX_PROPERTY),
                Collections.singletonList(
                    new TsIdentifierReference(split[split.length - 1])
                )
            );
    }
}

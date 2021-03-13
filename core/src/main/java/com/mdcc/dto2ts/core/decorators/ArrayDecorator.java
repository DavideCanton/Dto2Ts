package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import com.mdcc.dto2ts.core.utils.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_ARRAY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_ARRAY_OF_COMPLEX_PROPERTY;

@Component
public class ArrayDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context.getPropertyModel())
            .filter(p -> Utils.isArrayType(p.getTsType()))
            .map(this::getArrayElementType)
            .map(element -> context.addDecorator(buildArrayDecorator(element)));
    }

    private TsType getArrayElementType(PropertyModel property)
    {
        return ((ArrayType) property.getTsType()).getArrayElementType();
    }

    private DecoratorModel buildArrayDecorator(TsType element)
    {
        String[] split = element.toString().split("\\$");

        if (Utils.isBasicType(element))
            return new DecoratorModel(
                JSON_ARRAY,
                Collections.emptyList());
        else
            return new DecoratorModel(
                JSON_ARRAY_OF_COMPLEX_PROPERTY,
                Collections.singletonList(
                    split[split.length - 1]
                )
            );
    }
}

package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import com.mdcc.dto2ts.core.imports.*;
import com.mdcc.dto2ts.core.utils.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class ComplexDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context.getPropertyModel())
            .filter(p -> Utils.isComplexType(p.getTsType()))
            .map(PropertyModel::getTsType)
            .map(this::buildComplexDecorator)
            .map(context::addDecorator);
    }

    private DecoratorModel buildComplexDecorator(TsType type)
    {
        //TODO check
        String name = Utils.getClassNameFromTsQualifiedName(type.toString());

        return new DecoratorModel(
            ImportNames.JSON_COMPLEX_PROPERTY,
            Collections.singletonList(name)
        );
    }

}

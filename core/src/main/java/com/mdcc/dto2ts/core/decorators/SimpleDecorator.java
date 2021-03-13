package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import com.mdcc.dto2ts.core.imports.*;
import com.mdcc.dto2ts.core.utils.*;
import org.springframework.stereotype.*;

import java.util.*;


@Component
public class SimpleDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context.getPropertyModel())
            .filter(p -> Utils.isBasicType(p.getTsType()))
            .map(PropertyModel::getTsType)
            .map(BasicType.class::cast)
            .flatMap(this::buildSimpleDecorator)
            .map(context::addDecorator);
    }


    private Optional<DecoratorModel> buildSimpleDecorator(BasicType t)
    {
        return Optional.of(t)
            .map(BasicType::getName)
            .flatMap(type ->
            {
                String ret = null;
                switch (type)
                {
                    case "string":
                    case "number":
                        ret = ImportNames.JSON_PROPERTY;
                        break;
                    case "boolean":
                        ret = ImportNames.JSON_FLAG;
                        break;
                    case "Date":
                        ret = ImportNames.JSON_DATE_ISO;
                        break;
                    default:
                        break;

                }
                return Optional.ofNullable(ret);
            })
            .map(tsIdentifierReference -> new DecoratorModel(
                tsIdentifierReference,
                Collections.emptyList()
            ));
    }
}

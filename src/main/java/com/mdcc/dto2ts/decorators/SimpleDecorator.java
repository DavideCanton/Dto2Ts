package com.mdcc.dto2ts.decorators;

import com.mdcc.dto2ts.context.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;
import static com.mdcc.dto2ts.utils.Utils.isBasicType;

@Component
public class SimpleDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context.getPropertyModel())
            .filter(p -> isBasicType(p.getTsType()))
            .map(p -> (TsType.BasicType) p.getTsType())
            .flatMap(this::buildSimpleDecorator)
            .map(context::addDecorator);
    }


    private Optional<TsDecorator> buildSimpleDecorator(TsType.BasicType t)
    {
        return Optional.of(t)
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
            ));
    }
}

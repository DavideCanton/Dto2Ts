package com.mdcc.dto2ts.decorators;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.utils.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.imports.ImportNames.JSON_COMPLEX_PROPERTY;
import static com.mdcc.dto2ts.utils.Utils.isComplexType;

@Component
public class ComplexDecorator implements PropertyDecorator
{
    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context.getPropertyModel())
            .filter(p -> isComplexType(p.getTsType()))
            .map(TsProperty::getTsType)
            .map(this::buildComplexDecorator)
            .map(context::addDecorator);
    }

    private TsDecorator buildComplexDecorator(TsType type)
    {
        String name = Utils.getClassNameFromTsQualifiedName(type.toString());

        return new TsDecorator(
            new TsIdentifierReference(JSON_COMPLEX_PROPERTY),
            Collections.singletonList(new TsIdentifierReference(name))
        );
    }

}

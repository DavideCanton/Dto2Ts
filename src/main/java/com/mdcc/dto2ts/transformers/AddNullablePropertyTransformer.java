package com.mdcc.dto2ts.transformers;

import com.mdcc.dto2ts.context.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

import static com.mdcc.dto2ts.imports.ImportNames.JSON_COMPLEX_PROPERTY;
import static com.mdcc.dto2ts.imports.ImportNames.JSON_DATE_ISO;

@Component
@TransformAfterDecorate
public class AddNullablePropertyTransformer extends ConditionPropertyTransformer
{
    @Override
    protected boolean canTransform(PropertyContext context)
    {
        return context.getPropertyModel()
            .getDecorators()
            .stream()
            .map(TsDecorator::getIdentifierReference)
            .map(TsIdentifierReference::getIdentifier)
            .anyMatch(identifier -> identifier.equals(JSON_DATE_ISO) || identifier.equals(JSON_COMPLEX_PROPERTY));
    }

    @Override
    protected PropertyContext doTransform(PropertyContext context)
    {
        return context.withPropertyModel(
            context.getPropertyModel()
                .withTsType(new TsType.NullableType(context.getPropertyModel().getTsType()))
        );
    }
}

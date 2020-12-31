package com.mdcc.dto2ts.transformers;

import com.mdcc.dto2ts.context.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;

@Component
@TransformAfterDecorate
public class AddNullablePropertyTransformer extends ConditionPropertyTransformer
{
    private final Set<String> nullablePropertyDecorators = new HashSet<>(Arrays.asList(JSON_DATE_ISO, JSON_COMPLEX_PROPERTY, JSON_LOCALIZABLE_PROPERTY));

    @Override
    protected boolean canTransform(PropertyContext context)
    {
        return context
            .getDecorators()
            .stream()
            .map(TsDecorator::getIdentifierReference)
            .map(TsIdentifierReference::getIdentifier)
            .anyMatch(nullablePropertyDecorators::contains);
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

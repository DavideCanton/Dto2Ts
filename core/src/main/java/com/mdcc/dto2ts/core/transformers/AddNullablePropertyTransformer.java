package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.core.imports.ImportNames.*;

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
            .map(DecoratorModel::getIdentifier)
            .anyMatch(nullablePropertyDecorators::contains);
    }

    @Override
    protected PropertyContext doTransform(PropertyContext context)
    {
        return context.withPropertyModel(
            context.getPropertyModel()
                .withTsType(new NullableType(context.getPropertyModel().getTsType()))
        );
    }
}

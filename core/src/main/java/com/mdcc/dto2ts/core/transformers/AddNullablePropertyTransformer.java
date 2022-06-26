package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.PropertyContext;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_COMPLEX_PROPERTY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_DATE_ISO;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_LOCALIZABLE_PROPERTY;

@Component
@TransformAfterDecorate
public class AddNullablePropertyTransformer extends ConditionPropertyTransformer
{
    private final Set<String> nullablePropertyDecorators = new HashSet<>(Arrays.asList(JSON_DATE_ISO, JSON_COMPLEX_PROPERTY, JSON_LOCALIZABLE_PROPERTY));

    @Override
    protected boolean canTransform(PropertyContext context)
    {
        val factory = context.getPropertyOperationsFactory();
        val extractor = factory.createInfoExtractor();

        return context
            .getDecorators()
            .stream()
            .anyMatch(d -> nullablePropertyDecorators.contains(extractor.getDecoratorIdentifier(d)));
    }

    @Override
    protected PropertyContext doTransform(PropertyContext context)
    {
        return context.withTransformedProperty(
            (op, p) -> op.createPropertyRefTransformer().makeNullable(p)
        );
    }
}

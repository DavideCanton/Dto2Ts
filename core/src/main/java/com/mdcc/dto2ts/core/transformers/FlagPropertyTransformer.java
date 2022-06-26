package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.PropertyContext;
import org.springframework.stereotype.Component;

@Component
@TransformBeforeDecorate
public class FlagPropertyTransformer extends ConditionPropertyTransformer
{

    @Override
    public boolean canTransform(PropertyContext context)
    {
        return context.getPropertyOperationsFactory()
            .createInfoExtractor()
            .getPropertyName(context.getPropertyRef()).startsWith("flg");
    }

    @Override
    public PropertyContext doTransform(PropertyContext context)
    {
        return context.withTransformedProperty((op, p) -> op.createPropertyRefTransformer().makeBoolean(p));
    }
}

package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
@TransformBeforeDecorate
public class UUIDPropertyTransformer extends ConditionPropertyTransformer
{
    @Autowired
    private Arguments arguments;

    @Override
    public boolean canTransform(PropertyContext context)
    {
        return context.getPropertyOperationsFactory()
            .createInfoExtractor()
            .getPropertyName(context.getPropertyRef()).startsWith(arguments.getUidPrefix());
    }

    @Override
    public PropertyContext doTransform(PropertyContext context)
    {
        return context.withTransformedProperty((op, p) -> {
            val transformer = op.createPropertyRefTransformer();
            return transformer.makeNullable(transformer.makeString(p));
        });
    }
}

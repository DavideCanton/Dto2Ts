package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import org.springframework.stereotype.*;

@Component
@TransformBeforeDecorate
public class FlagPropertyTransformer extends ConditionPropertyTransformer
{

    @Override
    public boolean canTransform(PropertyContext context)
    {
        return context.getPropertyModel().getName().startsWith("flg");
    }

    @Override
    public PropertyContext doTransform(PropertyContext context)
    {
        return context.withTransformedProperty(c -> c.getPropertyModel().withTsType(new BooleanType()));
    }
}

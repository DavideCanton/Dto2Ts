package com.mdcc.dto2ts.transformers;

import com.mdcc.dto2ts.context.*;
import cz.habarta.typescript.generator.*;
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
        return context.withTransformedProperty(c -> c.getPropertyModel().withTsType(TsType.Boolean));
    }
}

package com.mdcc.dto2ts.transformers;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.core.*;
import cz.habarta.typescript.generator.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
@TransformBeforeDecorate
public class UUIDPropertyTransformer extends ConditionPropertyTransformer
{
    @Autowired
    private Arguments arguments;

    @Override
    protected boolean canTransform(PropertyContext context)
    {
        return context.getPropertyModel().getName().startsWith(arguments.getUidPrefix());
    }

    @Override
    protected PropertyContext doTransform(PropertyContext context)
    {
        return context.withTransformedProperty(c -> c.getPropertyModel().withTsType(TsType.String));
    }
}

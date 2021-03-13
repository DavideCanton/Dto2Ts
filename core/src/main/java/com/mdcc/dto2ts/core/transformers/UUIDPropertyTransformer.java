package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
@TransformBeforeDecorate
public class UUIDPropertyTransformer extends ConditionPropertyTransformer
{
    @Autowired
    private IArguments arguments;

    @Override
    protected boolean canTransform(PropertyContext context)
    {
        return context.getPropertyModel().getName().startsWith(arguments.getUidPrefix());
    }

    @Override
    protected PropertyContext doTransform(PropertyContext context)
    {
        return context.withTransformedProperty(c -> c.getPropertyModel().withTsType(BasicType.string()));
    }
}

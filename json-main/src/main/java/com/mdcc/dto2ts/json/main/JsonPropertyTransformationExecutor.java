package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.core.context.*;
import org.springframework.stereotype.*;

@Component
public class JsonPropertyTransformationExecutor extends DefaultPropertyTransformationExecutor<Object, JsonTransformationExecutorContext>
{
    @Override
    protected PropertyContext buildContext(Object property, JsonTransformationExecutorContext context)
    {
        return PropertyContext.builder()
            .propertyOperationsFactory(new JsonOperationsFactory())
            .className(context.getClassName())
            // .propertyRef(new TsPropertyRef(propertyModel))
            .build();
    }

    @Override
    protected Object unwrapContext(PropertyContext property)
    {
        return null;
    }
}

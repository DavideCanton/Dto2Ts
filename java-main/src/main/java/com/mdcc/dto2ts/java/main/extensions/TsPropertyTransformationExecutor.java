package com.mdcc.dto2ts.java.main.extensions;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.java.main.factories.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

@Component
public class TsPropertyTransformationExecutor extends DefaultPropertyTransformationExecutor<TsPropertyModel, TransformationExecutorContext>
{
    @Override
    protected PropertyContext buildContext(TsPropertyModel propertyModel, TransformationExecutorContext context)
    {
        return PropertyContext.builder()
            .className(context.getClassName())
            .propertyRef(new TsPropertyRef(propertyModel))
            .propertyOperationsFactory(new TsPropertyOperationsFactory())
            .build();
    }

    @Override
    protected TsPropertyModel unwrapContext(PropertyContext property)
    {
        return (TsPropertyModel) property.getUnderlyingProperty();
    }
}

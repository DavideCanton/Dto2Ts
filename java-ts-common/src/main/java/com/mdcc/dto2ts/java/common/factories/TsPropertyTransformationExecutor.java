package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.DefaultPropertyTransformationExecutor;
import com.mdcc.dto2ts.core.context.PropertyContext;
import cz.habarta.typescript.generator.emitter.TsPropertyModel;
import org.springframework.stereotype.Component;

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

    @Override
    public TransformationExecutorContext createContext(Object... params)
    {
        String className = (String) params[0];
        return new TransformationExecutorContext(className);
    }
}

package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.PropertyRef;
import cz.habarta.typescript.generator.emitter.TsPropertyModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TsPropertyRef implements PropertyRef
{
    private TsPropertyModel propertyModel;

    @Override
    public TsPropertyModel getUnderlyingValue()
    {
        return propertyModel;
    }
}

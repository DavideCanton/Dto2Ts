package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.emitter.*;
import lombok.*;

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

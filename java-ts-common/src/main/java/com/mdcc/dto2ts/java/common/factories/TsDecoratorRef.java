package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.DecoratorRef;
import cz.habarta.typescript.generator.emitter.TsDecorator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TsDecoratorRef implements DecoratorRef
{
    private TsDecorator decorator;

    @Override
    public Object getUnderlyingValue()
    {
        return decorator;
    }
}

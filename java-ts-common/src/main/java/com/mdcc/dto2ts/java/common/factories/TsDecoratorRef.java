package com.mdcc.dto2ts.java.common.factories;

import com.mdcc.dto2ts.core.context.*;
import cz.habarta.typescript.generator.emitter.*;
import lombok.*;

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

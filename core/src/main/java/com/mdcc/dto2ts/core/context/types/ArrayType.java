package com.mdcc.dto2ts.core.context.types;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@With
public class ArrayType extends TsType
{
    private TsType arrayElementType;
}

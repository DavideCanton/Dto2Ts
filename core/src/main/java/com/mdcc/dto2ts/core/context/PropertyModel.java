package com.mdcc.dto2ts.core.context;

import com.mdcc.dto2ts.core.context.types.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyModel
{
    private TsType tsType;
    private List<DecoratorModel> decorators;
    private String name;
}

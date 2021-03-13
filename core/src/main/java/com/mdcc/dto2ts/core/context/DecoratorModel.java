package com.mdcc.dto2ts.core.context;

import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DecoratorModel
{
    private String identifier;
    private List<String> parameters;
}

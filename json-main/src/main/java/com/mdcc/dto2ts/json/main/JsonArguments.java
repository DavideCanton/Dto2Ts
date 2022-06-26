package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.core.context.Arguments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter(AccessLevel.PROTECTED)
@Component
public class JsonArguments extends Arguments
{
    @Value("${json}")
    private String json;
    @Value("${rootModel:}")
    private String rootModel;
}

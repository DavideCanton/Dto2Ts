package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.core.context.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.*;

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

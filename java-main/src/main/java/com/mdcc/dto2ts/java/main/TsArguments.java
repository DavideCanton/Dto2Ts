package com.mdcc.dto2ts.java.main;

import com.mdcc.dto2ts.core.context.Arguments;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TsArguments extends Arguments
{
    @Value("${pattern}")
    private String pattern;
}

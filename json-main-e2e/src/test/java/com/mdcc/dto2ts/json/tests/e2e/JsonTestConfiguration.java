package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.*;
import org.springframework.context.annotation.*;

@Configuration
public class JsonTestConfiguration
{
    @Bean
    @Primary
    public JsonArguments getArguments()
    {
        return new SettableJsonArguments();
    }
}

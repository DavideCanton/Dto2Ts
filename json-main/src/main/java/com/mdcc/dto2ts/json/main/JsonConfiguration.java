package com.mdcc.dto2ts.json.main;

import org.springframework.context.annotation.*;

@Configuration
public class JsonConfiguration
{
    @Bean
    @Primary
    public JsonArguments getJsonArguments()
    {
        return new JsonArguments();
    }
}

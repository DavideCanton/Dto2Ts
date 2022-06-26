package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.JsonArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

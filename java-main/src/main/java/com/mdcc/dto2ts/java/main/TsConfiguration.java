package com.mdcc.dto2ts.java.main;

import cz.habarta.typescript.generator.*;
import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
public class TsConfiguration
{
    @Bean
    public TypeScriptGenerator getTypeScriptGenerator(Settings settings)
    {
        return new TypeScriptGenerator(settings);
    }

    @Bean
    public Input getInput(Settings settings, TsArguments arguments)
    {
        Input.Parameters params = new Input.Parameters();
        params.classNamePatterns = Collections.singletonList(arguments.getPattern());
        return Input.from(params);
    }
}

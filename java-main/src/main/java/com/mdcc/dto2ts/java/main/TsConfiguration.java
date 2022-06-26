package com.mdcc.dto2ts.java.main;

import cz.habarta.typescript.generator.Input;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.TypeScriptGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

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

package com.mdcc.dto2ts.json.main;

import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.context.annotation.*;

@Configuration
public class JsonConfiguration
{
    @Bean
    public Emitter getEmitter(Settings settings)
    {
        return new Emitter(settings);
    }
}

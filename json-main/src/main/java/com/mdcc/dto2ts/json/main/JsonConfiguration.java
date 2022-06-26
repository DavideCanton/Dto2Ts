package com.mdcc.dto2ts.json.main;

import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.emitter.Emitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration
{
    @Bean
    public Emitter getEmitter(Settings settings)
    {
        return new Emitter(settings);
    }
}

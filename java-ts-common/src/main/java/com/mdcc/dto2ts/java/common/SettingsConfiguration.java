package com.mdcc.dto2ts.java.common;

import cz.habarta.typescript.generator.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
public class SettingsConfiguration
{
    @Autowired
    private ClassRenamer classRenamer;

    @Autowired(required = false)
    @TsExtension
    private Optional<List<Extension>> extensionList;

    @Bean
    public Settings getSettings()
    {
        Settings settings = new Settings();
        settings.jsonLibrary = JsonLibrary.jackson2;
        settings.outputKind = TypeScriptOutputKind.module;
        settings.mapClasses = ClassMapping.asClasses;
        settings.outputFileType = TypeScriptFileType.implementationFile;
        settings.mapEnum = EnumMapping.asEnum;
        settings.noFileComment = true;
        settings.noEslintDisable = true;
        settings.noTslintDisable = true;
        settings.customTypeNamingFunctionImpl = classRenamer;
        this.extensionList.ifPresent(settings.extensions::addAll);
        return settings;
    }
}

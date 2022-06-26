package com.mdcc.dto2ts.java.common;

import cz.habarta.typescript.generator.ClassMapping;
import cz.habarta.typescript.generator.EnumMapping;
import cz.habarta.typescript.generator.Extension;
import cz.habarta.typescript.generator.JsonLibrary;
import cz.habarta.typescript.generator.NullabilityDefinition;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.TypeScriptFileType;
import cz.habarta.typescript.generator.TypeScriptOutputKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

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
        settings.nullabilityDefinition = NullabilityDefinition.nullInlineUnion;
        settings.customTypeNamingFunctionImpl = classRenamer;
        this.extensionList.ifPresent(settings.extensions::addAll);
        return settings;
    }
}

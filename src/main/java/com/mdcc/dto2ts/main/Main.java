package com.mdcc.dto2ts.main;

import com.mdcc.dto2ts.extensions.*;
import cz.habarta.typescript.generator.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Input.Parameters params = new Input.Parameters();
        params.classNamePatterns = Collections.singletonList(args[0]);
        Input input = Input.from(params);

        Settings settings = new Settings();
        settings.jsonLibrary = JsonLibrary.jackson2;
        settings.outputKind = TypeScriptOutputKind.module;
        settings.mapClasses = ClassMapping.asClasses;
        settings.outputFileType = TypeScriptFileType.implementationFile;
        settings.mapEnum = EnumMapping.asEnum;
        settings.noFileComment = true;
        settings.noEslintDisable = true;
        settings.noTslintDisable = true;
        settings.extensions.add(new ClassNameDecoratorExtension());

        TypeScriptGenerator typeScriptGenerator = new TypeScriptGenerator(settings);
        String s = typeScriptGenerator.generateTypeScript(input);
        System.out.println(s);
    }
}

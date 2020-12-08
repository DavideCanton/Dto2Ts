package com.mdcc.dto2ts.main;

import com.google.common.base.CaseFormat;
import com.mdcc.dto2ts.extensions.ClassNameDecoratorExtension;
import cyclops.control.Try;
import cz.habarta.typescript.generator.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        splitTypeScriptClasses(s);
    }

    private static void splitTypeScriptClasses(String typeScriptClasses) {
        Pattern p = Pattern.compile("export class (\\w+)", Pattern.MULTILINE);
        Arrays.stream(typeScriptClasses.trim().split("}"))
                .forEach(classCode -> {
                    Matcher m = p.matcher(classCode);
                    if (m.find()) {
                        String className = m.group(1);
                        createTypeScriptFile(classCode.trim() + "\n}", className);
                    }
                });
    }

    private static void createTypeScriptFile(String code, String fileName) {

        Try.<String, Exception>withCatch(() -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fileName))
                .mapOrCatch(fileNameKebabCase -> new FileWriter("./target/" + fileNameKebabCase + ".model.ts"), Exception.class)
                .mapOrCatch(PrintWriter::new, Exception.class)
                .peek(printWriter -> printWriter.print(code))
                .peek(PrintWriter::close)
                .onFail(t -> System.out.println("Errore creazione file" + fileName));
    }


}

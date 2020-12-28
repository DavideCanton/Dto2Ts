package com.mdcc.dto2ts.core;

import com.mdcc.dto2ts.extensions.*;
import com.mdcc.dto2ts.utils.*;
import cyclops.control.*;
import cyclops.data.tuple.*;
import cyclops.reactive.*;
import cz.habarta.typescript.generator.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

@Getter
@Component
public class Dto2TsGenerator {
    private Input input;
    private TypeScriptGenerator generator;
    private String outputFolder;
    @Autowired
    private Arguments arguments;
    @Autowired
    private ClassNameDecoratorExtension extension;

    public void init() {
        this.input = buildInput(arguments.getPattern());
        this.generator = buildGenerator(extension);
        this.outputFolder = arguments.getOutputFolder();
    }

    public Try<String, Throwable> generate() {
        return this.extension.init()
            .mapOrCatch(__ -> this.generator.generateTypeScript(this.input), Throwable.class);
    }

    public Try<Void, Throwable> splitTypeScriptClasses(String typeScriptClasses) {
        Pattern p = Pattern.compile("export class (\\w+)", Pattern.MULTILINE);

        return ReactiveSeq.fromStream(Arrays.stream(typeScriptClasses.trim().split("@JsonClass")))
            .map(s -> s.replace("[];", "[] = [];"))
            .map(s -> "@JsonClass" + s)
            .map(s -> Tuple2.of(s, p.matcher(s)))
            .filter(t -> t._2().find())
            .onEmptyError(() -> new Exception("Code not found"))
            .reduce(
                Try.success(null),
                (first, current) ->
                    first.flatMapOrCatch(__ ->
                            createTypeScriptFile(current._1().trim(), current._2().group(1)),
                        Throwable.class
                    )
            );
    }

    private Try<Void, Throwable> createTypeScriptFile(String code, String className) {
        return Try.withResources(
            () -> new PrintWriter(new FileWriter(new File(this.outputFolder, Utils.getClassName(className) + ".ts"))),
            pw -> {
                writeFile(code, className, pw);
                return null;
            },
            Throwable.class
        );
    }

    private void writeFile(String code, String className, PrintWriter pw) {
        String imports = generateClassImport(className);
        pw.print(imports);
        pw.println();
        pw.print(code);
    }

    private String generateClassImport(String className) {
        return extension
            .getImportHandler()
            .getImportsFor(className)
            .reduce(
                new StringBuilder(),
                (sb, tuple) -> sb.append(String.format(
                    "import { %s } from '%s';%n",
                    String.join(", ", tuple._2()),
                    tuple._1()
                ))
            )
            .toString();
    }

    @NotNull
    private static Input buildInput(String pattern) {
        Input.Parameters params = new Input.Parameters();
        params.classNamePatterns = Collections.singletonList(pattern);
        return Input.from(params);
    }

    @NotNull
    private static TypeScriptGenerator buildGenerator(Extension extension) {
        Settings settings = new Settings();
        settings.jsonLibrary = JsonLibrary.jackson2;
        settings.outputKind = TypeScriptOutputKind.module;
        settings.mapClasses = ClassMapping.asClasses;
        settings.outputFileType = TypeScriptFileType.implementationFile;
        settings.mapEnum = EnumMapping.asEnum;
        settings.noFileComment = true;
        settings.noEslintDisable = true;
        settings.noTslintDisable = true;
        settings.extensions.add(extension);

        return new TypeScriptGenerator(settings);
    }
}

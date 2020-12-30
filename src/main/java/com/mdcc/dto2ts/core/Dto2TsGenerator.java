package com.mdcc.dto2ts.core;

import com.mdcc.dto2ts.extensions.*;
import com.mdcc.dto2ts.imports.*;
import com.mdcc.dto2ts.utils.*;
import cyclops.control.*;
import cyclops.data.tuple.*;
import cyclops.reactive.*;
import cz.habarta.typescript.generator.*;
import lombok.extern.slf4j.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

@Component
@Slf4j
public class Dto2TsGenerator {
    private String outputFolder;
    @Autowired
    private Arguments arguments;
    @Autowired
    private ClassNameDecoratorExtension extension;

    private Input input;
    private TypeScriptGenerator generator;

    @PostConstruct()
    public void init() {
        input = this.buildInput();
        generator = this.buildGenerator();
    }

    public Try<String, Throwable> generate() {
        this.outputFolder = arguments.getOutputFolder();

        return this.extension.init()
            .mapOrCatch(
                __ -> this.generator.generateTypeScript(this.input),
                Throwable.class
            );
    }

    public Try<Integer, Throwable> splitTypeScriptClasses(String typeScriptClasses) {
        Pattern pattern = Pattern.compile("export class (\\w+)", Pattern.MULTILINE);
        val decorator = String.format("@%s", ImportNames.JSON_CLASS);

        return ReactiveSeq.fromStream(Arrays.stream(typeScriptClasses.trim().split(decorator)))
            .map(s -> s.replace("[];", "[] = [];"))
            .map(s -> decorator + s)
            .map(s -> Tuple2.of(s, pattern.matcher(s)))
            .filter(t -> t._2().find())
            .reduce(
                Try.success(0),
                (first, current) ->
                    first.flatMapOrCatch(n ->
                            createTypeScriptFile(current._1().trim(), current._2().group(1))
                                .map(__ -> n + 1),
                        Throwable.class
                    )
            )
            .filter(n -> n > 0, __ -> new IllegalStateException("Code not found"));
    }

    private Try<Boolean, Throwable> createTypeScriptFile(String code, String className) {
        val file = new File(this.outputFolder, Utils.getClassName(className) + ".ts");
        val path = Try.withCatch(file::getCanonicalPath, IOException.class)
            .get()
            .orElse("<<INVALID FILE>>");

        return Try.withResources(
            () -> new PrintWriter(new FileWriter(file)),
            pw -> writeFile(code, className, pw),
            Throwable.class
        )
            .peek(__ -> log.info("Written {}", path))
            .onFail(__ -> log.error("Error in writing {}", path));
    }

    private boolean writeFile(String code, String className, PrintWriter pw) {
        String imports = generateClassImport(className);
        pw.print(imports);
        pw.println();
        pw.print(code);
        return true;
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
    private Input buildInput() {
        Input.Parameters params = new Input.Parameters();
        params.classNamePatterns = Collections.singletonList(arguments.getPattern());
        return Input.from(params);
    }

    @NotNull
    private TypeScriptGenerator buildGenerator() {
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

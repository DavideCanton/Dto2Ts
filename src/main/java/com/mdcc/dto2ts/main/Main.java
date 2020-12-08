package com.mdcc.dto2ts.main;

import com.google.common.base.*;
import com.mdcc.dto2ts.extensions.*;
import cyclops.control.*;
import cyclops.data.tuple.*;
import cz.habarta.typescript.generator.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Try.success(args)
                .mapOrCatch(a -> Tuple2.of(buildInput(a[0]), buildGenerator()))
                .mapOrCatch(t -> t._2().generateTypeScript(t._1()))
                .flatMapOrCatch(Main::splitTypeScriptClasses)
                .onFail(t -> System.err.println("Got exception: " + t.toString()))
        ;
    }

    @NotNull
    private static Input buildInput(String pattern) {
        Input.Parameters params = new Input.Parameters();
        params.classNamePatterns = Collections.singletonList(pattern);
        return Input.from(params);
    }

    @NotNull
    private static TypeScriptGenerator buildGenerator() {
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

        return new TypeScriptGenerator(settings);
    }

    private static Try<Void, Throwable> splitTypeScriptClasses(String typeScriptClasses) {
        Pattern p = Pattern.compile("export class (\\w+)", Pattern.MULTILINE);
        return streamReduce(
                Arrays.stream(typeScriptClasses.trim().split("}"))
                        .map(p::matcher)
                        .filter(Matcher::find),
                Try.success(null),
                (first, current) -> combineLazy(
                        first,
                        () -> {
                            String code = current.group(0);
                            String className = current.group(1);
                            return Try.success(className)
                                    .map(fn -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fn))
                                    .map(fileNameKebabCase -> String.format("./target/%s.model.ts", fileNameKebabCase))
                                    .flatMapOrCatch(fn -> createTypeScriptFile(code.trim() + "\n}", fn));
                        },
                        (a, b) -> a
                ));
    }

    private static Try<Void, Throwable> createTypeScriptFile(String code, String fileName) {
        return Try.withResources(
                () -> new PrintWriter(new FileWriter(fileName)),
                pw -> {
                    pw.print(code);
                    return null;
                },
                IOException.class
        );
    }

    private static <T, U> U streamReduce(Stream<T> stream, U identity, BiFunction<U, T, U> combiner) {
        return stream.reduce(identity, combiner, (a, b) -> a);
    }

    private static <T, X extends Throwable> Try<T, X> combineLazy(Try<T, X> t1, Supplier<Try<T, X>> t2, BinaryOperator<T> func) {
        return t1.flatMapOrCatch(v -> t2.get().mapOrCatch(v2 -> func.apply(v, v2)));
    }
}

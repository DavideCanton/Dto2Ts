package com.mdcc.dto2ts.java.main;

import com.mdcc.dto2ts.core.imports.*;
import com.mdcc.dto2ts.core.utils.*;
import cyclops.control.*;
import cyclops.data.tuple.*;
import cyclops.reactive.*;
import cz.habarta.typescript.generator.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.regex.*;
import java.util.stream.*;

@Component
@Slf4j
public class Dto2TsGenerator
{
    @Autowired
    private TsArguments arguments;
    @Autowired
    private TypeScriptGenerator generator;
    @Autowired
    private CodeWriteUtils codeWriteUtils;
    @Autowired
    private Input input;

    public Try<String, Throwable> generate()
    {
        return Try.withCatch(() -> this.generator.generateTypeScript(this.input), Throwable.class);
    }

    public Try<Integer, Throwable> splitTypeScriptClasses(String typeScriptClasses)
    {
        Pattern pattern = Pattern.compile("export (class|interface) (\\w+)", Pattern.MULTILINE);
        String decorator = String.format("@%s\\(\\)", ImportNames.JSON_CLASS);

        return ReactiveSeq.fromStream(Stream.of(typeScriptClasses.trim().split(decorator)))
            .map(codeWriteUtils::setDefaultForArrays)
            .map(s -> Tuple2.of(s, pattern.matcher(s)))
            .filter(t -> t._2().find())
            .map(t -> t.map1(s -> "class".equals(t._2().group(1)) ? String.format("@%s()", ImportNames.JSON_CLASS) + s : s))
            .reduce(
                Try.success(0),
                (first, current) ->
                    first.flatMapOrCatch(n ->
                            createTypeScriptFile(current._1().trim(), current._2().group(1), current._2().group(2))
                                .map(__ -> n + 1),
                        Throwable.class
                    )
            )
            .filter(n -> n > 0, __ -> new IllegalStateException("Code not found"));
    }

    private Try<Boolean, Throwable> createTypeScriptFile(String code, String type, String className)
    {
        File file = new File(this.arguments.getOutputFolder(), "class".equals(type)
            ? Utils.getClassName(className) + ".ts"
            : arguments.getVisitorName() + ".visitor.ts");

        String path = Try.withCatch(file::getCanonicalPath, IOException.class)
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

    private boolean writeFile(String code, String className, PrintWriter pw)
    {
        String imports = codeWriteUtils.generateClassImport(className);
        pw.print(imports);
        pw.println();
        pw.print(code);
        return true;
    }
}

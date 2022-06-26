package com.mdcc.dto2ts.java.main;

import com.mdcc.dto2ts.core.context.Arguments;
import com.mdcc.dto2ts.core.domains.DomainHandler;
import cyclops.control.Try;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.codehaus.plexus.util.ExceptionUtils;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@SpringBootApplication
@Slf4j
@ComponentScan(
    basePackages = "com.mdcc.dto2ts"
)
public class Main implements CommandLineRunner
{
    @Autowired
    private Dto2TsGenerator generator;
    @Autowired
    private Arguments arguments;
    @Autowired
    private DomainHandler domainHandler;

    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }

    private Try<Boolean, Throwable> writeDomainFile()
    {
        val file = new File(arguments.getOutputFolder(), "domains.txt");
        val path = Try.withCatch(file::getCanonicalPath, IOException.class)
            .fold(__ -> __, __ -> "<<INVALID FILE>>");

        return Try.withResources(
                () -> new PrintWriter(new FileWriter(file)),
                w ->
                {
                    domainHandler.getDomainsUsed().forEach(w::println);
                    return true;
                },
                Throwable.class
            )
            .peek(__ -> log.info("Written domain file at {}", path))
            .onFail(__ -> log.error("Error in writing domain file at {}", path));
    }

    @Override
    public void run(String... args)
    {
        arguments.isValid()
            .flatMap(__ ->
                this.createOutputFolderOrClean()
                    .flatMapOrCatch(___ -> generator.generate(), Throwable.class)
                    .flatMapOrCatch(generator::splitTypeScriptClasses, Throwable.class)
                    .flatMapOrCatch(___ -> writeDomainFile(), Throwable.class)
                    .toEither()
                    .mapLeft(t -> Collections.singletonList(getFullStackTrace(t)))
            )
            .peek(__ -> log.info("Finished!"))
            .peekLeft(s -> log.error("Error: {}", s));
    }

    private Try<Void, Throwable> createOutputFolderOrClean()
    {
        return Try.success(new File(arguments.getOutputFolder()))
            .flatMapOrCatch(dir ->
            {
                if (dir.isDirectory())
                    return Try.runWithCatch(() -> FileUtils.cleanDirectory(dir));
                else if (!dir.exists())
                    return Try.runWithCatch(() -> {
                        if (!dir.mkdirs())
                            throw new IOException("Directory creation failure.");
                    });
                else
                    return Try.failure(new Exception("There is already a file named as the output directory"));
            });
    }

    @NotNull
    private String getFullStackTrace(Throwable t)
    {
        return ExceptionUtils.getFullStackTrace(t);
    }
}

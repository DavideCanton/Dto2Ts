package com.mdcc.dto2ts;

import com.mdcc.dto2ts.main.*;
import com.mdcc.dto2ts.domains.*;
import cyclops.control.*;
import dto2ts.main.*;
import lombok.extern.slf4j.*;
import lombok.*;
import org.codehaus.plexus.util.*;
import org.jetbrains.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import java.io.*;
import java.util.*;

@SpringBootApplication
@Slf4j
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
                generator.generate()
                    .flatMapOrCatch(generator::splitTypeScriptClasses, Throwable.class)
                    .flatMapOrCatch(___ -> writeDomainFile(), Throwable.class)
                    .toEither()
                    .mapLeft(t -> Collections.singletonList(getFullStackTrace(t)))
            )
            .peek(__ -> log.info("Finished!"))
            .peekLeft(s -> log.error("Error: {}", s));
    }

    @NotNull
    private String getFullStackTrace(Throwable t)
    {
        return ExceptionUtils.getFullStackTrace(t);
    }
}

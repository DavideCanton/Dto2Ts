package com.mdcc.dto2ts;

import com.mdcc.dto2ts.core.*;
import com.mdcc.dto2ts.domains.*;
import cyclops.control.*;
import lombok.extern.slf4j.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import java.io.*;

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
            w -> {
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
        generator.generate()
            .flatMapOrCatch(generator::splitTypeScriptClasses, Throwable.class)
            .flatMapOrCatch(__ -> writeDomainFile(), Throwable.class)
            .peek(__ -> log.info("Finished!"))
            .onFail(t -> log.error("Error", t));
    }
}
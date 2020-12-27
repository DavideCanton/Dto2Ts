package com.mdcc.dto2ts;

import com.beust.jcommander.*;
import com.mdcc.dto2ts.core.*;
import cyclops.control.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import java.io.*;
import java.util.*;

@SpringBootApplication
@EnableAutoConfiguration
public class Main implements CommandLineRunner {
    @Autowired
    private Dto2TsGenerator generator;
    @Autowired
    private Arguments arguments;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private static Try<Void, Throwable> writeDomainFile(Arguments arguments, Collection<String> domainsUsed) {
        return Try.withResources(
            () -> new PrintWriter(new FileWriter(new File(arguments.getOutputFolder(), "domains.txt"))),
            w -> {
                domainsUsed.forEach(w::println);
                return null;
            },
            Throwable.class
        );
    }

    private JCommander parseArgs(String[] argv) {
        val jc = JCommander.newBuilder()
            .addObject(arguments)
            .build();
        jc.parse(argv);
        return jc;
    }

    @Override
    public void run(String... args) {
        Try.success(args)
            .mapOrCatch(this::parseArgs, Throwable.class)
            .flatMapOrCatch(jc -> {
                if (arguments.isHelp()) {
                    jc.usage();
                    return Try.success(null);
                }

                generator.init();

                return Try.success(generator)
                    .flatMapOrCatch(g ->
                        g.generate().flatMapOrCatch(g::splitTypeScriptClasses).map(__ -> g), Throwable.class
                    )
                    .flatMapOrCatch(g ->
                        writeDomainFile(g.getArguments(), g.getExtension().getDomainHandler().getDomainsUsed())
                    )
                    .peek(__ -> logger.info("Finished!"));
            })
            .onFail(t -> logger.error("Error", t));
    }
}

package com.mdcc.dto2ts.main;

import com.beust.jcommander.*;
import cyclops.control.*;
import lombok.*;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Try.success(args)
            .mapOrCatch(Main::parseArgs, Throwable.class)
            .flatMapOrCatch(jc -> {
                val arguments = (Arguments) jc.getObjects().get(0);
                if (arguments.isHelp()) {
                    jc.usage();
                    return Try.success(null);
                }

                return Try.success(arguments)
                    .mapOrCatch(Dto2TsGenerator::new, Throwable.class)
                    .flatMapOrCatch(g ->
                        g.generate().flatMapOrCatch(g::splitTypeScriptClasses).map(__ -> g), Throwable.class
                    )
                    .flatMapOrCatch(g ->
                        writeDomainFile(g.getArguments(), g.getExtension().getDomainHandler().getDomainsUsed())
                    )
                    .peek(__ -> System.out.println("Finished!"));
            })
            .onFail(Throwable::printStackTrace);
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

    private static JCommander parseArgs(String[] argv) {
        val jc = JCommander.newBuilder()
            .addObject(new Arguments())
            .build();
        jc.parse(argv);
        return jc;
    }
}

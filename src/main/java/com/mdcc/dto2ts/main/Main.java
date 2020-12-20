package com.mdcc.dto2ts.main;

import com.beust.jcommander.*;
import cyclops.control.*;

public class Main {
    public static void main(String[] args) {
        Try.success(args)
            .mapOrCatch(Main::parseArgs, Throwable.class)
            .mapOrCatch(Dto2TsGenerator::new, Throwable.class)
            .flatMapOrCatch(g ->
                g.generate().flatMapOrCatch(g::splitTypeScriptClasses), Throwable.class
            )
            .onFail(Throwable::printStackTrace)
            .peek(__ -> System.out.println("Finished!"))
        ;
    }

    private static Arguments parseArgs(String[] strings) {
        Arguments args = new Arguments();
        JCommander.newBuilder()
            .addObject(args)
            .build()
            .parse(strings);
        return args;
    }
}

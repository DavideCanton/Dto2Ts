package com.mdcc.dto2ts.main;

import cyclops.control.*;

public class Main {
    public static void main(String[] args) {
        Try.success(args)
            .mapOrCatch(Main::parseArgs, Throwable.class)
            .mapOrCatch(Dto2TsGenerator::new, Throwable.class)
            .flatMapOrCatch(g ->
                g.generate().flatMapOrCatch(g::splitTypeScriptClasses), Throwable.class
            )
            .onFail(t -> System.err.println("Got exception: " + t.toString()))
            .peek(__ -> System.out.println("Finished!"))
        ;
    }

    private static Arguments parseArgs(String[] strings) {
        return new Arguments()
            .withPattern(strings[0])
            .withVisitableName("Pippo")
            .withVisitablePath("./pippo")
            .withVisitorName("IVisitor")
            .withVisitorPath("./pippo")
            .withOutputFolder(strings.length > 1 ? strings[1] : "./target");
    }
}

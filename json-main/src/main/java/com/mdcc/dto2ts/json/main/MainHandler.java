package com.mdcc.dto2ts.json.main;

import cyclops.control.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class MainHandler
{
    @Autowired
    private JsonArguments arguments;
    @Autowired
    private Settings settings;

    public Try<Void, Throwable> generate()
    {
        Emitter emitter = new Emitter(settings);

        return Try.withResources(
            () -> new PrintWriter(new FileWriter(arguments.getOutputFolder() + "/" + "xxx.ts")),
            writer -> {
                TsModel model = new TsModel()
                    .withBeans(Collections.singletonList(createFakeBean()));

                emitter.emit(
                    model,
                    writer,
                    "aaa",
                    true,
                    true,
                    0
                );
                return null;
            }
        );
    }

    private TsBeanModel createFakeBean()
    {
        return new TsBeanModel(
            TsBeanModel.class,
            null,
            true,
            new Symbol("MySymbol"),
            Collections.emptyList(),
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.singletonList(createFakeProperty()),
            null,
            Collections.emptyList(),
            null
        ).withDecorators(
            Collections.singletonList(createFakeDecorator())
        );
    }

    private TsPropertyModel createFakeProperty()
    {
        return new TsPropertyModel(
            "property",
            TsType.BasicType.String,
            Collections.singletonList(createFakeDecorator()),
            TsModifierFlags.None,
            true,
            null
        );
    }

    private TsDecorator createFakeDecorator()
    {
        return new TsDecorator(
            new TsIdentifierReference("abc"),
            null
        );
    }

}

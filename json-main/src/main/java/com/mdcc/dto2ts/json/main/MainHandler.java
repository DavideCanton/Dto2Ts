package com.mdcc.dto2ts.json.main;

import cyclops.control.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import io.swagger.models.*;
import io.swagger.parser.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

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
        Swagger swagger = new SwaggerParser().read(arguments.getJson());
        return Try.success(null);
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

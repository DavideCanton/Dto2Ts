package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.java.common.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import io.swagger.models.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class ModelGenerator
{
    @Autowired
    private ClassRenamer renamer;

    public TsBeanModel createBean(Model model)
    {
        String title = renamer.getName(null, model.getTitle());

        return new TsBeanModel(
            TsBeanModel.class,
            null,
            true,
            new Symbol(title),
            Collections.emptyList(),
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            null,
            Collections.emptyList(),
            null
        ).withDecorators(
            Collections.emptyList()
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

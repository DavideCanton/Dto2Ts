package com.mdcc.dto2ts.java.main.extensions;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.java.common.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import org.jetbrains.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Component
@TsExtension
public class ClassNameDecoratorExtension extends Extension
{
    @Autowired
    private Arguments args;
    @Autowired
    private CodeDecorationUtils codeDecorationUtils;

    @Override
    public EmitterExtensionFeatures getFeatures()
    {
        final EmitterExtensionFeatures features = new EmitterExtensionFeatures();
        features.generatesRuntimeCode = true;
        return features;
    }

    @Override
    public List<TransformerDefinition> getTransformers()
    {
        List<TransformerDefinition> transformerDefinitions = new ArrayList<>();

        transformerDefinitions.add(new TransformerDefinition(ModelCompiler.TransformationPhase.BeforeEnums, (symbolTable, model) ->
            model.withBeans(getBeans(model)))
        );
        return transformerDefinitions;
    }

    @NotNull
    private List<TsBeanModel> getBeans(TsModel model)
    {
        List<TsBeanModel> beans = model.getBeans().stream()
            .map(codeDecorationUtils::decorateClass)
            .collect(Collectors.toList());

        if (args.isCreateVisitor())
            beans.add(codeDecorationUtils.createVisitorBean());

        return beans;
    }
}
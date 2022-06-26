package com.mdcc.dto2ts.java.main.extensions;

import com.mdcc.dto2ts.core.context.Arguments;
import com.mdcc.dto2ts.java.common.CodeDecorationUtils;
import com.mdcc.dto2ts.java.common.TsExtension;
import cz.habarta.typescript.generator.Extension;
import cz.habarta.typescript.generator.compiler.ModelCompiler;
import cz.habarta.typescript.generator.emitter.EmitterExtensionFeatures;
import cz.habarta.typescript.generator.emitter.TsBeanModel;
import cz.habarta.typescript.generator.emitter.TsModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
package org.example.main;

import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class ClassNameDecoratorExtension extends Extension {

    @Override
    public EmitterExtensionFeatures getFeatures() {
        final EmitterExtensionFeatures features = new EmitterExtensionFeatures();
        features.generatesRuntimeCode = true;
        return features;
    }

    @Override
    public List<TransformerDefinition> getTransformers() {
        return Collections.singletonList(
                new TransformerDefinition(ModelCompiler.TransformationPhase.BeforeEnums, (symbolTable, model) ->
                        model.withBeans(model.getBeans().stream()
                                .map(ClassNameDecoratorExtension.this::decorateClass)
                                .collect(Collectors.toList())
                        ))
        );
    }

    private TsBeanModel decorateClass(TsBeanModel bean) {
        if (!bean.isClass())
            return bean;

        return bean
                .withDecorators(Collections.singletonList(new TsDecorator(
                        new TsIdentifierReference("JsonClass"),
                        Collections.emptyList()
                )))
                .withProperties(bean.getProperties().stream()
                        .map(ClassNameDecoratorExtension.this::decorateProperty)
                        .collect(Collectors.toList())
                );
    }

    private TsPropertyModel decorateProperty(TsPropertyModel property) {
        List<TsDecorator> decorators = Collections.singletonList(buildPropertyDecorator(property));
        return property
                .withDecorators(decorators);
    }

    @NotNull
    private TsDecorator buildPropertyDecorator(TsPropertyModel property) {
        if (property.tsType instanceof TsType.BasicArrayType) {
            TsType element = ((TsType.BasicArrayType) property.tsType).elementType;
            return buildArrayDecorator(property, element);
        } else
            return buildSimpleDecorator(property);
    }

    private TsDecorator buildArrayDecorator(TsPropertyModel property, TsType element) {
        String[] split = element.toString().split("\\$");

        return new TsDecorator(
                new TsIdentifierReference("JsonArrayOfComplexType"),
                Collections.singletonList(
                        new TsIdentifierReference(split[split.length - 1])
                )
        );
    }

    private TsDecorator buildSimpleDecorator(TsPropertyModel property) {
        return new TsDecorator(
                new TsIdentifierReference("JsonProperty"),
                Collections.emptyList()
        );
    }

}
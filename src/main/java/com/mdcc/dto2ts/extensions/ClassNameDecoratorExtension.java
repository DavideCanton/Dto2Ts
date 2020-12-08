package com.mdcc.dto2ts.extensions;

import cz.habarta.typescript.generator.Extension;
import cz.habarta.typescript.generator.TsType;
import cz.habarta.typescript.generator.compiler.ModelCompiler;
import cz.habarta.typescript.generator.emitter.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClassNameDecoratorExtension extends Extension {

    public static final String JSON_PROPERTY = "JsonProperty";
    public static final String JSON_FLAG = "JsonFlag";
    public static final String JSON_DATE_ISO = "JsonDateISO";
    public static final String JSON_COMPLEX_PROPERTY = "JsonComplexProperty";
    public static final String JSON_ARRAY_OF_COMPLEX_TYPE = "JsonArrayOfComplexType";
    public static final String JSON_ARRAY = "JsonArray";

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
        } else if (property.tsType instanceof TsType.BasicType)
            return buildSimpleDecorator(property);
        else
            return buildComplexDecorator(property, property.tsType);
    }

    private TsDecorator buildArrayDecorator(TsPropertyModel property, TsType element) {
        String[] split = element.toString().split("\\$");

        if (((TsType.BasicArrayType) property.tsType).elementType instanceof TsType.BasicType) {
            return new TsDecorator(
                    new TsIdentifierReference(JSON_ARRAY),
                    Collections.emptyList());
        }
        return new TsDecorator(
                new TsIdentifierReference(JSON_ARRAY_OF_COMPLEX_TYPE),
                Collections.singletonList(
                        new TsIdentifierReference(split[split.length - 1])
                )
        );
    }

    private TsDecorator buildSimpleDecorator(TsPropertyModel property) {

        String tsIdentifierReference = null;
        switch (((TsType.BasicType) property.tsType).name) {
            case "string":
            case "number":
                tsIdentifierReference = JSON_PROPERTY;
                break;
            case "boolean":
                tsIdentifierReference = JSON_FLAG;
                break;
            case "Date":
                tsIdentifierReference = JSON_DATE_ISO;
                break;
        }
        return new TsDecorator(
                new TsIdentifierReference(tsIdentifierReference),
                Collections.emptyList()
        );
    }

    private TsDecorator buildComplexDecorator(TsPropertyModel property, TsType element) {
        String[] split = element.toString().split("\\$");

        return new TsDecorator(
                new TsIdentifierReference(JSON_COMPLEX_PROPERTY),
                Collections.singletonList(new TsIdentifierReference(split[split.length - 1]))
        );
    }

}
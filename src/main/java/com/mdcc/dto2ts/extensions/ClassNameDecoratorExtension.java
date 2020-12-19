package com.mdcc.dto2ts.extensions;

import com.mdcc.dto2ts.imports.*;
import com.mdcc.dto2ts.utils.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;

public class ClassNameDecoratorExtension extends Extension {
    private final ImportHandler importHandler = new ImportHandler();

    public ImportHandler getImportHandler() {
        return importHandler;
    }

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

        String className = Utils.getClassNameFromTsQualifiedName(bean.getName().getSimpleName());
        importHandler.registerClassLibraryImport(className, JSON_CLASS);

        return bean
                .withDecorators(Collections.singletonList(new TsDecorator(
                        new TsIdentifierReference(JSON_CLASS),
                        Collections.emptyList()
                )))
                .withProperties(bean.getProperties().stream()
                        .map(property -> ClassNameDecoratorExtension.this.decorateProperty(property, className))
                        .collect(Collectors.toList())
                );
    }

    private TsPropertyModel decorateProperty(TsPropertyModel property, String simpleName) {
        List<TsDecorator> decorators = Collections.singletonList(buildPropertyDecorator(property));
        decorators.forEach(decorator -> putImport(decorator, simpleName));
        return property
                .withDecorators(decorators);
    }

    private void putImport(TsDecorator decorator, String simpleName) {
        String decoratorName = decorator.getIdentifierReference().getIdentifier();
        importHandler.registerClassLibraryImport(simpleName, decoratorName);

        decorator.getArguments()
                .stream()
                .findFirst()
                .filter(a -> a instanceof TsIdentifierReference)
                .map(a -> ((TsIdentifierReference) a).getIdentifier())
                .ifPresent(argument -> importHandler.registerOtherClassImport(simpleName, argument));
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
                new TsIdentifierReference(JSON_ARRAY_OF_COMPLEX_PROPERTY),
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
        String name = Utils.getClassNameFromTsQualifiedName(element.toString());

        return new TsDecorator(
                new TsIdentifierReference(JSON_COMPLEX_PROPERTY),
                Collections.singletonList(new TsIdentifierReference(name))
        );
    }

}
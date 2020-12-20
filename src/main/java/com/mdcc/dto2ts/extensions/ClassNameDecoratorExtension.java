package com.mdcc.dto2ts.extensions;

import com.mdcc.dto2ts.domains.*;
import com.mdcc.dto2ts.imports.*;
import com.mdcc.dto2ts.main.*;
import com.mdcc.dto2ts.utils.*;
import cyclops.control.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import lombok.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;

@Getter
public class ClassNameDecoratorExtension extends Extension {
    private final ImportHandler importHandler = new ImportHandler();
    private final DomainHandler domainHandler;
    private final Arguments args;

    public ClassNameDecoratorExtension(Arguments args) {
        this.args = args;
        this.domainHandler = new DomainHandler(args.getThreshold());
    }

    public Try<Void, Throwable> init() {
        return this.domainHandler.loadPropertiesFrom(args.getDomainFile())
            .mapFailure(Function.identity());
    }

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
        importHandler.registerClassLibraryImport(className, SERIALIZE_FN);
        importHandler.registerExternalImport(className, args.getVisitableName(), args.getVisitablePath());
        importHandler.registerExternalImport(className, args.getVisitorName(), args.getVisitorPath());
        bean.getImplementsList().add(new TsType.BasicType(args.getVisitableName()));

        return bean
            .withDecorators(Collections.singletonList(new TsDecorator(
                new TsIdentifierReference(JSON_CLASS),
                Collections.emptyList()
            )))
            .withProperties(Stream.concat(bean.getProperties().stream()
                    .map(property -> decorateProperty(property, className))
                    .map(this::transformPropertyType),
                Stream.of(buildSerializeProperty()))
                .collect(Collectors.toList())
            )
            .withMethods(Collections.singletonList(new TsMethodModel(
                "accept",
                TsModifierFlags.None,
                Collections.emptyList(),
                Collections.singletonList(new TsParameterModel("visitor", new TsType.BasicType(args.getVisitorName()))),
                TsType.Void,
                Collections.singletonList(
                    new TsExpressionStatement(
                        new TsCallExpression(
                            new TsMemberExpression(new TsIdentifierReference("visitor"), "visit" + className),
                            new TsThisExpression()
                        )
                    )
                ),
                null
            )));
    }

    private TsPropertyModel transformPropertyType(TsPropertyModel property) {
        return property
            .getDecorators()
            .stream()
            .map(TsDecorator::getIdentifierReference)
            .map(TsIdentifierReference::getIdentifier)
            .filter(identifier -> identifier.equals(JSON_DATE_ISO) || identifier.equals(JSON_COMPLEX_PROPERTY))
            .findFirst()
            .map(__ -> property.withTsType(new TsType.NullableType(property.tsType)))
            .orElse(property);
    }


    private TsPropertyModel buildSerializeProperty() {
        return new TsPropertyModel("serialize",
            new TsType.BasicType(SERIALIZE_FN),
            TsModifierFlags.None,
            true,
            null
        );
    }

    private TsPropertyModel decorateProperty(TsPropertyModel property, String simpleName) {
        if (property.tsType.equals(TsType.String) &&
            property.name.startsWith(args.getDomainPrefix())) {

            val domain = domainHandler.isDomain(property.name.substring(args.getDomainPrefix().length()));

            if (domain.isPresent()) {
                importHandler.registerClassLibraryImport(simpleName, JSON_LOCALIZABLE_PROPERTY);
                importHandler.registerClassLibraryImport(simpleName, I_LOCALIZABLE_PROPERTY);
                domainHandler.registerUsedDomain(domain.get());
                return buildDomainProperty(property);
            }
        }

        List<TsDecorator> decorators = Collections.singletonList(buildPropertyDecorator(property));
        decorators.forEach(decorator -> putImport(decorator, simpleName));
        return property
            .withDecorators(decorators);
    }

    private TsPropertyModel buildDomainProperty(TsPropertyModel property) {
        return new TsPropertyModel(
            property.name,
            new TsType.NullableType(new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String)),
            TsModifierFlags.None,
            true,
            null
        ).withDecorators(
            Collections.singletonList(new TsDecorator(
                new TsIdentifierReference(JSON_LOCALIZABLE_PROPERTY),
                Collections.emptyList()
            ))
        );
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
        } else if (isBasicType(property.tsType))
            return buildSimpleDecorator(property);
        else
            return buildComplexDecorator(property.tsType);
    }

    private boolean isBasicType(TsType tsType) {
        return tsType instanceof TsType.BasicType;
    }

    private TsDecorator buildArrayDecorator(TsPropertyModel property, TsType element) {
        String[] split = element.toString().split("\\$");

        if (isBasicType(((TsType.BasicArrayType) property.tsType).elementType)) {
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
        String type = ((TsType.BasicType) property.tsType).name;
        switch (type) {
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
            default:
                break;
        }
        return new TsDecorator(
            new TsIdentifierReference(tsIdentifierReference),
            Collections.emptyList()
        );
    }

    private TsDecorator buildComplexDecorator(TsType element) {
        String name = Utils.getClassNameFromTsQualifiedName(element.toString());

        return new TsDecorator(
            new TsIdentifierReference(JSON_COMPLEX_PROPERTY),
            Collections.singletonList(new TsIdentifierReference(name))
        );
    }

}
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

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;

@Getter
public class ClassNameDecoratorExtension extends Extension
{
    private final ImportHandler importHandler = new ImportHandler();
    private final DomainHandler domainHandler;
    private final Arguments args;
    private final PropertyTransformer propertyTransformer = new PropertyTransformer();

    public ClassNameDecoratorExtension(Arguments args)
    {
        this.args = args;
        this.domainHandler = new DomainHandler(args.getThreshold());
    }

    public Try<Void, Throwable> init()
    {
        return Try.withResources(
            () -> new FileReader(args.getDomainFile()),
            this.domainHandler::loadPropertiesFrom,
            IOException.class
        )
            .flatMap(t -> t)
            .mapFailure(Function.identity());
    }

    public ImportHandler getImportHandler()
    {
        return importHandler;
    }

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
        return Collections.singletonList(
            new TransformerDefinition(ModelCompiler.TransformationPhase.BeforeEnums, (symbolTable, model) ->
                model.withBeans(model.getBeans().stream()
                    .map(ClassNameDecoratorExtension.this::decorateClass)
                    .collect(Collectors.toList())
                ))
        );
    }

    private TsBeanModel decorateClass(TsBeanModel bean)
    {
        if (!bean.isClass())
            return bean;

        String className = Utils.getClassNameFromTsQualifiedName(bean.getName().getSimpleName());
        importHandler.registerClassLibraryImport(className, JSON_CLASS);
        importHandler.registerClassLibraryImport(className, SERIALIZE_FN);
        importHandler.registerExternalImport(className, args.getVisitableName(), args.getVisitablePath());
        importHandler.registerExternalImport(className, args.getVisitorName(), args.getVisitorPath());
        bean.getImplementsList().add(new TsType.GenericBasicType(args.getVisitableName(), new TsType.BasicType(args.getVisitorName())));

        return bean
            .withDecorators(Collections.singletonList(new TsDecorator(
                new TsIdentifierReference(JSON_CLASS),
                Collections.emptyList()
            )))
            .withProperties(
                Stream.concat(
                    bean.getProperties().stream()
                        .map(propertyTransformer::transformPropertyTypeBeforeDecorate)
                        .map(property -> decorateProperty(property, className))
                        .map(propertyTransformer::transformPropertyTypeAfterDecorate),
                    Stream.of(propertyTransformer.buildSerializeProperty())
                )
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


    private TsPropertyModel decorateProperty(TsPropertyModel property, String simpleName)
    {
        if (property.tsType.equals(TsType.String) &&
            property.name.startsWith(args.getDomainPrefix()))
        {
            val domain = domainHandler.findDomain(property.name.substring(args.getDomainPrefix().length()));

            if (domain.isPresent())
            {
                importHandler.registerClassLibraryImport(simpleName, JSON_LOCALIZABLE_PROPERTY);
                importHandler.registerClassLibraryImport(simpleName, I_LOCALIZABLE_PROPERTY);
                importHandler.registerClassLibraryImport(simpleName, DOMAINS);
                domainHandler.registerUsedDomain(domain.get());
                return propertyTransformer.buildDomainProperty(property, domain.get());
            }
        }

        List<TsDecorator> decorators = new ArrayList<>();
        buildPropertyDecorator(property).ifPresent(decorators::add);

        decorators.forEach(decorator -> putImport(decorator, simpleName));
        return property
            .withDecorators(decorators);
    }

    private void putImport(TsDecorator decorator, String simpleName)
    {
        String decoratorName = decorator.getIdentifierReference().getIdentifier();
        importHandler.registerClassLibraryImport(simpleName, decoratorName);

        decorator.getArguments()
            .stream()
            .findFirst()
            .filter(a -> a instanceof TsIdentifierReference)
            .map(a -> ((TsIdentifierReference) a).getIdentifier())
            .ifPresent(argument -> importHandler.registerOtherClassImport(simpleName, argument));
    }

    private Optional<TsDecorator> buildPropertyDecorator(TsPropertyModel property)
    {
        if (property.tsType instanceof TsType.BasicArrayType)
        {
            TsType element = ((TsType.BasicArrayType) property.tsType).elementType;
            return Optional.of(buildArrayDecorator(property, element));
        }
        else if (isBasicType(property.tsType))
            return buildSimpleDecorator(property);
        else
            return Optional.of(buildComplexDecorator(property.tsType));
    }

    private boolean isBasicType(TsType tsType)
    {
        return tsType instanceof TsType.BasicType;
    }

    private TsDecorator buildArrayDecorator(TsPropertyModel property, TsType element)
    {
        String[] split = element.toString().split("\\$");

        if (isBasicType(((TsType.BasicArrayType) property.tsType).elementType))
            return new TsDecorator(
                new TsIdentifierReference(JSON_ARRAY),
                Collections.emptyList());
        else
            return new TsDecorator(
                new TsIdentifierReference(JSON_ARRAY_OF_COMPLEX_PROPERTY),
                Collections.singletonList(
                    new TsIdentifierReference(split[split.length - 1])
                )
            );
    }

    private Optional<TsDecorator> buildSimpleDecorator(TsPropertyModel property)
    {
        return Optional.of(((TsType.BasicType) property.tsType).name)
            .flatMap(type ->
            {
                String ret = null;
                switch (type)
                {
                    case "string":
                    case "number":
                        ret = JSON_PROPERTY;
                        break;
                    case "boolean":
                        ret = JSON_FLAG;
                        break;
                    case "Date":
                        ret = JSON_DATE_ISO;
                        break;
                    default:
                        break;

                }
                return Optional.ofNullable(ret);
            })
            .map(tsIdentifierReference -> new TsDecorator(
                new TsIdentifierReference(tsIdentifierReference),
                Collections.emptyList()
            ));
    }

    private TsDecorator buildComplexDecorator(TsType element)
    {
        String name = Utils.getClassNameFromTsQualifiedName(element.toString());

        return new TsDecorator(
            new TsIdentifierReference(JSON_COMPLEX_PROPERTY),
            Collections.singletonList(new TsIdentifierReference(name))
        );
    }

}
package com.mdcc.dto2ts.extensions;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.core.*;
import com.mdcc.dto2ts.decorators.*;
import com.mdcc.dto2ts.imports.*;
import com.mdcc.dto2ts.transformers.*;
import com.mdcc.dto2ts.utils.*;
import com.mdcc.dto2ts.visitor.*;
import cyclops.data.tuple.*;
import cyclops.reactive.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.util.*;
import java.util.stream.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;

@Component
public class ClassNameDecoratorExtension extends Extension
{
    @Autowired
    private ImportHandler importHandler;
    @Autowired
    private Arguments args;
    @Autowired
    private VisitorContext visitorContext;
    @Autowired
    private ClassRenamer classRenamer;

    @Autowired
    @TransformAfterDecorate
    private List<PropertyTransformer> afterDecoratePropertyTransformers;

    @Autowired
    @TransformBeforeDecorate
    private List<PropertyTransformer> beforeDecoratePropertyTransformers;

    @Autowired
    private List<PropertyDecorator> propertyDecorators;

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
            model.withBeans(getBeans(model))
        ));
        return transformerDefinitions;
    }

    @NotNull
    private List<TsBeanModel> getBeans(TsModel model)
    {
        List<TsBeanModel> beans = model.getBeans().stream()
            .map(ClassNameDecoratorExtension.this::decorateClass)
            .collect(Collectors.toList());

        if (args.isCreateVisitor())
        {
            beans.add(new TsBeanModel(null,
                TsBeanCategory.Data,
                false,
                new Symbol(Utils.getVisitorName(args.getVisitorName())),
                Collections.emptyList(),
                null,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                null,
                visitorContext.getVisitedClasses().stream().map(this::buildVisitMethod).collect(Collectors.toList()),
                null)
                .withDecorators(buildClassDecorators()));
        }
        return beans;
    }

    private TsMethodModel buildVisitMethod(String className)
    {
        String visitorVariableName = StringUtils.uncapitalize(className);
        String visitMethodName = "visit" + className;

        return new TsMethodModel(
            visitMethodName,
            TsModifierFlags.None,
            Collections.emptyList(),
            Collections.singletonList(
                new TsParameterModel(
                    visitorVariableName,
                    new TsType.BasicType(className)
                )
            ),
            TsType.Void,
            null,
            null
        );
    }

    private TsBeanModel decorateClass(TsBeanModel bean)
    {
        if (!bean.isClass())
            return bean;

        String className = Utils.getClassNameFromTsQualifiedName(bean.getName().getSimpleName());

        registerDefaultImports(className);
        visitorContext.addClass(classRenamer.getName(bean));

        if (args.isCreateVisitor())
            implementVisitableInterface(bean);

        return bean
            .withDecorators(buildClassDecorators())
            .withProperties(
                Stream.concat(
                    bean.getProperties().stream()
                        .map(p -> buildPropertyContext(p, className))
                        .map(c -> applyWhileNull(beforeDecoratePropertyTransformers, c))
                        .map(c -> applyWhileNull(propertyDecorators, c))
                        .peek(c -> c.getDecorators().forEach(decorator -> putImport(decorator, c.getClassName())))
                        .map(c -> applyWhileNull(afterDecoratePropertyTransformers, c))
                        .map(PropertyContext::getPropertyWithDecorators),
                    Stream.of(buildSerializeProperty())
                ).collect(Collectors.toList())
            )
            .withMethods(buildMethods(bean));
    }

    private void implementVisitableInterface(TsBeanModel bean)
    {
        bean.getImplementsList()
            .add(
                new TsType.GenericBasicType(
                    args.getVisitableName(),
                    new TsType.BasicType(Utils.getVisitorName(args.getVisitorName()))
                )
            );
    }

    private void registerDefaultImports(String className)
    {
        importHandler.registerClassLibraryImport(className, JSON_CLASS);
        importHandler.registerClassLibraryImport(className, SERIALIZE_FN);

        if (args.isCreateVisitor())
        {
            importHandler.registerExternalImport(className, args.getVisitableName(), args.getVisitablePath());
            importHandler.registerExternalImport(
                className,
                Utils.getVisitorName(args.getVisitorName()),
                Utils.getVisitorPath(args.getVisitorName()));
        }
    }

    @NotNull
    private List<TsDecorator> buildClassDecorators()
    {
        return Collections.singletonList(buildJsonClassDecorator());
    }

    @NotNull
    private TsDecorator buildJsonClassDecorator()
    {
        return new TsDecorator(
            new TsIdentifierReference(JSON_CLASS),
            Collections.emptyList()
        );
    }

    @NotNull
    private List<TsMethodModel> buildMethods(TsBeanModel bean)
    {
        val list = new ArrayList<TsMethodModel>();

        if (args.isCreateVisitor()) list.add(buildAcceptMethod(bean));

        return list;
    }

    @NotNull
    private TsMethodModel buildAcceptMethod(TsBeanModel bean)
    {
        String visitorVariableName = "visitor";
        String visitMethodName = "visit";

        return new TsMethodModel(
            "accept",
            TsModifierFlags.None,
            Collections.emptyList(),
            Collections.singletonList(
                new TsParameterModel(
                    visitorVariableName,
                    new TsType.BasicType(Utils.getVisitorName(args.getVisitorName()))
                )
            ),
            TsType.Void,
            Collections.singletonList(
                new TsExpressionStatement(
                    new TsCallExpression(
                        new TsMemberExpression(
                            new TsIdentifierReference(visitorVariableName),
                            visitMethodName + classRenamer.getName(bean)
                        ),
                        new TsThisExpression()
                    )
                )
            ),
            null
        );
    }

    private <T extends ContextTransformer> PropertyContext applyWhileNull(List<T> list, PropertyContext startingValue)
    {
        return ReactiveSeq.fromList(list)
            .reduce(
                Tuple2.of(false, startingValue),
                (acc, t) -> Boolean.TRUE.equals(acc._1()) ? acc : (
                    t.transformContext(startingValue)
                        .map(c -> Tuple2.of(true, c))
                        .orElse(acc)
                )
            )
            ._2();
    }

    private PropertyContext buildPropertyContext(TsPropertyModel propertyModel, String className)
    {
        return PropertyContext.builder()
            .className(className)
            .propertyModel(propertyModel)
            .build();
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

    private TsPropertyModel buildSerializeProperty()
    {
        return new TsPropertyModel(
            "serialize",
            new TsType.BasicType(SERIALIZE_FN),
            TsModifierFlags.None,
            true,
            null
        );
    }

}
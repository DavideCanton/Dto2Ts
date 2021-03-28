package com.mdcc.dto2ts.java.common;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.imports.*;
import com.mdcc.dto2ts.core.utils.*;
import com.mdcc.dto2ts.core.visitor.*;
import com.mdcc.dto2ts.java.common.factories.*;
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

import static com.mdcc.dto2ts.core.imports.ImportNames.*;

@Component
public class CodeDecorationUtils
{
    @Autowired
    private Arguments args;
    @Autowired
    private ClassRenamer classRenamer;
    @Autowired
    private PropertyTransformationExecutor<TsPropertyModel, TransformationExecutorContext> transformationExecutor;
    @Autowired
    private VisitorContext visitorContext;
    @Autowired
    private ImportHandler importHandler;

    public TsBeanModel createVisitorBean()
    {
        return new TsBeanModel(null,
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
            .withDecorators(buildClassDecorators()
            );
    }

    public TsMethodModel buildVisitMethod(String className)
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

    public TsBeanModel decorateClass(TsBeanModel bean)
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
                    transformationExecutor.transformProperties(
                        bean.getProperties(),
                        transformationExecutor.createContext(className)
                    ).stream()
                        .peek(c -> c.getDecorators().forEach(decorator -> putImport(decorator, className))),
                    Stream.of(buildSerializeProperty())
                ).collect(Collectors.toList())
            )
            .withMethods(buildMethods(bean));
    }

    public void implementVisitableInterface(TsBeanModel bean)
    {
        bean.getImplementsList()
            .add(
                new TsType.GenericBasicType(
                    args.getVisitableName(),
                    new TsType.BasicType(Utils.getVisitorName(args.getVisitorName()))
                )
            );
    }

    public void registerDefaultImports(String className)
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
    public List<TsMethodModel> buildMethods(TsBeanModel bean)
    {
        val list = new ArrayList<TsMethodModel>();

        if (args.isCreateVisitor()) list.add(buildAcceptMethod(bean));

        return list;
    }

    @NotNull
    public List<TsDecorator> buildClassDecorators()
    {
        return Collections.singletonList(buildJsonClassDecorator());
    }

    @NotNull
    public TsDecorator buildJsonClassDecorator()
    {
        return new TsDecorator(
            new TsIdentifierReference(JSON_CLASS),
            Collections.emptyList()
        );
    }

    @NotNull
    public TsMethodModel buildAcceptMethod(TsBeanModel bean)
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

    public void putImport(TsDecorator decorator, String simpleName)
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

    public TsPropertyModel buildSerializeProperty()
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

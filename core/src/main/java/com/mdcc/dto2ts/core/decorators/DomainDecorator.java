package com.mdcc.dto2ts.core.decorators;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.domains.*;
import com.mdcc.dto2ts.core.imports.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.core.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.core.context.ContextConstants.DOMAIN_KEY;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DomainDecorator implements PropertyDecorator
{
    @Autowired
    private ImportHandler importHandler;
    @Autowired
    private DomainHandler domainHandler;

    @Override
    public Optional<PropertyContext> decorateProperty(PropertyContext context)
    {
        return Optional.of(context)
            .filter(this::isLocalizeProperty)
            .map(this::buildDomainDecorator)
            .map(context::addDecorator)
            .map(this::registerImports);
    }

    private DecoratorRef buildDomainDecorator(PropertyContext context)
    {
        return context.getPropertyOperationsFactory()
            .createDecoratorBuilder()
            .buildDomainDecorator(
                context.getPropertyRef(),
                this.getDomainFromContext(context)
            );
    }

    private String getDomainFromContext(PropertyContext context)
    {
        return context.getExtendedProperty(DOMAIN_KEY)
            .map(Object::toString)
            .orElse("");
    }

    private boolean isLocalizeProperty(PropertyContext context)
    {
        PropertyOperationsFactory op = context.getPropertyOperationsFactory();
        InfoExtractor i = op.createInfoExtractor();
        PropertyTypeChecker p = op.createPropertyTypeChecker();

        return p.isBasicType(context.getPropertyRef())
            && i.getPropertyTypeName(context.getPropertyRef()).orElse("").equals(ImportNames.I_LOCALIZABLE_PROPERTY);
    }

    private PropertyContext registerImports(PropertyContext info)
    {
        importHandler.registerClassLibraryImport(info.getClassName(), ImportNames.JSON_LOCALIZABLE_PROPERTY);
        importHandler.registerClassLibraryImport(info.getClassName(), ImportNames.DOMAINS);
        domainHandler.registerUsedDomain(getDomainFromContext(info));
        return info;
    }
}

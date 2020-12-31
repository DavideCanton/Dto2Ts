package com.mdcc.dto2ts.decorators;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.domains.*;
import com.mdcc.dto2ts.imports.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.core.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.context.ContextConstants.DOMAIN_KEY;
import static com.mdcc.dto2ts.imports.ImportNames.*;

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

    private TsDecorator buildDomainDecorator(PropertyContext context)
    {
        return new TsDecorator(
            new TsIdentifierReference(JSON_LOCALIZABLE_PROPERTY),
            Collections.singletonList(
                new TsMemberExpression(
                    new TsIdentifierReference(DOMAINS),
                    getDomainFromContext(context)
                )
            )
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
        return context.getPropertyModel().getTsType() instanceof TsType.GenericBasicType
            && ((TsType.GenericBasicType) context.getPropertyModel().getTsType()).name.equals(I_LOCALIZABLE_PROPERTY);
    }

    private PropertyContext registerImports(PropertyContext info)
    {
        importHandler.registerClassLibraryImport(info.getClassName(), JSON_LOCALIZABLE_PROPERTY);
        importHandler.registerClassLibraryImport(info.getClassName(), DOMAINS);
        domainHandler.registerUsedDomain(getDomainFromContext(info));
        return info;
    }
}

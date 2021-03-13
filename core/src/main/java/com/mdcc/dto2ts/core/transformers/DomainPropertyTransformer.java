package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import com.mdcc.dto2ts.core.domains.*;
import com.mdcc.dto2ts.core.imports.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.core.context.ContextConstants.DOMAIN_KEY;
import static com.mdcc.dto2ts.core.imports.ImportNames.I_LOCALIZABLE_PROPERTY;

@Component
@TransformBeforeDecorate
public class DomainPropertyTransformer implements PropertyTransformer
{
    @Autowired
    private IArguments args;

    @Autowired
    private DomainHandler domainHandler;

    @Autowired
    private ImportHandler importHandler;


    private Optional<String> getDomain(PropertyModel property)
    {
        if (property.getTsType() instanceof BasicType &&
            ((BasicType) property.getTsType()).isString() &&
            property.getName().startsWith(args.getDomainPrefix()))
        {
            return domainHandler.findDomain(property.getName().substring(args.getDomainPrefix().length()));
        }

        return Optional.empty();
    }

    public Optional<PropertyContext> transformProperty(PropertyContext context)
    {
        return getDomain(context.getPropertyModel())
            .map(domain -> context.withExtendedProperty(DOMAIN_KEY, domain))
            .map(newContext -> {
                registerImports(newContext);
                newContext.setPropertyModel(buildProperty(newContext));
                return newContext;
            });
    }

    private PropertyModel buildProperty(PropertyContext context)
    {
        return new PropertyModel(
            new GenericBasicType(I_LOCALIZABLE_PROPERTY, BasicType.string()),
            Collections.emptyList(),
            context.getPropertyModel().getName()
        );
    }

    private void registerImports(PropertyContext info)
    {
        importHandler.registerClassLibraryImport(info.getClassName(), I_LOCALIZABLE_PROPERTY);
    }

}

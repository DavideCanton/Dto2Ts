package com.mdcc.dto2ts.transformers;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.core.*;
import com.mdcc.dto2ts.domains.*;
import com.mdcc.dto2ts.imports.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.context.ContextConstants.DOMAIN_KEY;
import static com.mdcc.dto2ts.imports.ImportNames.I_LOCALIZABLE_PROPERTY;

@Component
@TransformBeforeDecorate
public class DomainPropertyTransformer implements PropertyTransformer
{
    @Autowired
    private Arguments args;

    @Autowired
    private DomainHandler domainHandler;

    @Autowired
    private ImportHandler importHandler;


    private Optional<String> getDomain(TsPropertyModel property)
    {
        if (property.tsType.equals(TsType.String) &&
            property.name.startsWith(args.getDomainPrefix()))
        {
            return domainHandler.findDomain(property.name.substring(args.getDomainPrefix().length()));
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

    private TsPropertyModel buildProperty(PropertyContext context)
    {
        return new TsPropertyModel(
            context.getPropertyModel().name,
            new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String),
            TsModifierFlags.None,
            true,
            null
        );
    }

    private void registerImports(PropertyContext info)
    {
        importHandler.registerClassLibraryImport(info.getClassName(), I_LOCALIZABLE_PROPERTY);
    }

}

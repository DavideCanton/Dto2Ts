package com.mdcc.dto2ts.core.transformers;

import com.mdcc.dto2ts.core.context.Arguments;
import com.mdcc.dto2ts.core.context.InfoExtractor;
import com.mdcc.dto2ts.core.context.PropertyContext;
import com.mdcc.dto2ts.core.context.PropertyTypeChecker;
import com.mdcc.dto2ts.core.domains.DomainHandler;
import com.mdcc.dto2ts.core.imports.ImportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mdcc.dto2ts.core.context.ContextConstants.DOMAIN_KEY;
import static com.mdcc.dto2ts.core.imports.ImportNames.I_LOCALIZABLE_PROPERTY;

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


    private Optional<String> getDomain(PropertyContext context)
    {
        InfoExtractor infoExtractor = context.getPropertyOperationsFactory().createInfoExtractor();
        PropertyTypeChecker propertyTypeChecker = context.getPropertyOperationsFactory().createPropertyTypeChecker();

        String propertyName = infoExtractor.getPropertyName(context.getPropertyRef());

        if (propertyTypeChecker.isString(context.getPropertyRef()) &&
            propertyName.startsWith(args.getDomainPrefix()))
        {
            return domainHandler.findDomain(propertyName.substring(args.getDomainPrefix().length()));
        }

        return Optional.empty();
    }

    public Optional<PropertyContext> transformProperty(PropertyContext context)
    {
        return getDomain(context)
            .map(domain -> {
                PropertyContext newContext = buildProperty(
                    context.withExtendedProperty(DOMAIN_KEY, domain)
                );
                registerImports(newContext);
                return newContext;
            });
    }

    private PropertyContext buildProperty(PropertyContext context)
    {
        return context.withTransformedProperty((pc, p) -> pc.createPropertyRefTransformer().makeDomain(p));
    }

    private void registerImports(PropertyContext info)
    {
        importHandler.registerClassLibraryImport(info.getClassName(), I_LOCALIZABLE_PROPERTY);
    }

}

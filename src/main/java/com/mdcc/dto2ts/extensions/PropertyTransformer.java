package com.mdcc.dto2ts.extensions;

import com.mdcc.dto2ts.core.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;

@Component
public class PropertyTransformer {
    @Autowired
    private Arguments args;

    public TsPropertyModel buildFlagProperty(TsPropertyModel property) {
        return property.withTsType(TsType.Boolean);
    }

    public TsPropertyModel buildUUIDProperty(TsPropertyModel property) {
        return property.withTsType(TsType.String);
    }

    public TsPropertyModel buildSerializeProperty() {
        return new TsPropertyModel(
            "serialize",
            new TsType.BasicType(SERIALIZE_FN),
            TsModifierFlags.None,
            true,
            null
        );
    }

    private boolean isUUID(TsPropertyModel propertyModel) {
        return propertyModel.name.startsWith(args.getUidPrefix());
    }

    public TsPropertyModel buildDomainProperty(TsPropertyModel property, String domain) {
        return new TsPropertyModel(
            property.name,
            new TsType.NullableType(new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String)),
            TsModifierFlags.None,
            true,
            null
        ).withDecorators(
            Collections.singletonList(new TsDecorator(
                new TsIdentifierReference(JSON_LOCALIZABLE_PROPERTY),
                Collections.singletonList(
                    new TsMemberExpression(
                        new TsIdentifierReference(DOMAINS),
                        domain
                    )
                )
            ))
        );
    }

    public TsPropertyModel transformPropertyTypeBeforeDecorate(TsPropertyModel property) {
        if (isUUID(property))
            property = buildUUIDProperty(property);
        else if (property.getName().startsWith("flg"))
            property = buildFlagProperty(property);

        return property;
    }

    public TsPropertyModel transformPropertyTypeAfterDecorate(TsPropertyModel property) {
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
}

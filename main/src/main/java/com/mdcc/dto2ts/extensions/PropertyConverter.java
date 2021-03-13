package com.mdcc.dto2ts.extensions;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.context.types.*;
import cz.habarta.typescript.generator.emitter.*;
import org.springframework.stereotype.*;

import java.util.stream.*;

@Component
public class PropertyConverter
{

    public PropertyModel toPropertyModel(TsPropertyModel propertyModel)
    {
        return PropertyModel.builder()
            .tsType(this.convert(propertyModel.getTsType()))
            .name(propertyModel.getName())
            .decorators(propertyModel.getDecorators()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList())
            )
            .build();
    }

    private DecoratorModel convert(TsDecorator decorator)
    {
        return DecoratorModel.builder()
            .identifier(decorator.getIdentifierReference().getIdentifier())
            .parameters(decorator.getArguments()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList())
            )
            .build();
    }

    private TsType convert(cz.habarta.typescript.generator.TsType tsType)
    {
        if (tsType.equals(cz.habarta.typescript.generator.TsType.BasicType.String))
            return BasicType.string();
        if (tsType.equals(cz.habarta.typescript.generator.TsType.BasicType.Number))
            return BasicType.number();
        if (tsType.equals(cz.habarta.typescript.generator.TsType.BasicType.Boolean))
            return BasicType.bool();
        if (tsType.equals(cz.habarta.typescript.generator.TsType.BasicType.Date))
            return BasicType.date();
        if (tsType instanceof cz.habarta.typescript.generator.TsType.BasicArrayType)
        {
            cz.habarta.typescript.generator.TsType.BasicArrayType a = (cz.habarta.typescript.generator.TsType.BasicArrayType) tsType;
            return new ArrayType(this.convert(a.elementType));
        }
        return null;
    }


    public TsPropertyModel toTsPropertyModel(PropertyModel p)
    {
        return null;
    }
}

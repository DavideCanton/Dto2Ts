package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.core.utils.*;
import com.mdcc.dto2ts.java.common.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.compiler.*;
import cz.habarta.typescript.generator.emitter.*;
import io.swagger.models.*;
import io.swagger.models.properties.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Component
public class ModelGenerator
{
    @Autowired
    private ClassRenamer renamer;
    @Autowired
    private CodeDecorationUtils utils;
    @Autowired
    private Settings settings;

    public TsBeanModel createBean(Model model)
    {
        val title = renamer.getName(null, model.getTitle());

        val bean = utils.decorateClass(new TsBeanModel(
            TsBeanModel.class,
            null,
            true,
            new Symbol(title),
            new LinkedList<>(),
            null,
            new LinkedList<>(),
            new LinkedList<>(),
            generateProperties(model.getProperties()),
            null,
            new LinkedList<>(),
            null
        ));

        return bean.withProperties(
            bean.getProperties()
                .stream()
                .map(p -> p.withTsType(transformNullability(p.getTsType())))
                .collect(Collectors.toList())
        );
    }


    private List<TsPropertyModel> generateProperties(Map<String, Property> properties)
    {
        return properties.entrySet()
            .stream()
            .flatMap(e -> StreamUtils.toStream(createProperty(e.getKey(), e.getValue())))
            .collect(Collectors.toList());
    }

    private Optional<TsPropertyModel> createProperty(String name, Property property)
    {
        return getType(property)
            .map(type -> new TsPropertyModel(
                name,
                type,
                new LinkedList<>(),
                TsModifierFlags.None,
                true,
                null
            ));
    }

    private Optional<TsType> getType(Property p)
    {
        if (p instanceof ArrayProperty)
            return getType(((ArrayProperty) p).getItems())
                .map(TsType.BasicArrayType::new);
        else if (p instanceof RefProperty)
            return Optional.of(new TsType.ReferenceType(new Symbol(((RefProperty) p).getSimpleRef())));
        else
        {
            switch (p.getType())
            {
                case "string":
                    return Optional.of(
                        "date-time".equals(p.getFormat()) ?
                            TsType.BasicType.Date :
                            TsType.BasicType.String
                    );
                case "number":
                    return Optional.of(TsType.BasicType.Number);
            }
        }
        return Optional.empty();
    }

    private TsType transformNullability(TsType tsType)
    {
        val nullabilityDefinition = settings.nullabilityDefinition;

        if (tsType instanceof TsType.NullableType)
        {
            final TsType.NullableType nullableType = (TsType.NullableType) tsType;
            if (nullabilityDefinition.isInline())
                return new TsType.UnionType(nullableType.type).add(nullabilityDefinition.getTypes());
        }
        return tsType;
    }
}

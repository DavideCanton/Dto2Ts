package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.java.common.ClassRenamer;
import com.mdcc.dto2ts.java.common.CodeDecorationUtils;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.TsType;
import cz.habarta.typescript.generator.compiler.Symbol;
import cz.habarta.typescript.generator.emitter.TsBeanModel;
import cz.habarta.typescript.generator.emitter.TsModifierFlags;
import cz.habarta.typescript.generator.emitter.TsPropertyModel;
import io.swagger.models.Model;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            .map(e -> createProperty(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    private TsPropertyModel createProperty(String name, Property property)
    {
        val type = getType(property);
        return new TsPropertyModel(
            name,
            type,
            new LinkedList<>(),
            TsModifierFlags.None,
            true,
            type.equals(TsType.Any) ?
                Collections.singletonList("untraslated type: " + property.getType()) :
                null
        );
    }

    private TsType getType(Property p)
    {
        if (p instanceof ArrayProperty)
            return new TsType.BasicArrayType(getType(((ArrayProperty) p).getItems()));
        else if (p instanceof RefProperty)
        {
            String name = ((RefProperty) p).getSimpleRef();
            String renamed = this.renamer.getName(null, name);
            return new TsType.ReferenceType(new Symbol(renamed));
        }
        else
        {
            switch (p.getType())
            {
                case "string":
                    return "date-time".equals(p.getFormat()) ?
                        TsType.BasicType.Date :
                        TsType.BasicType.String;
                case "number":
                case "integer":
                    return TsType.BasicType.Number;
            }
        }
        return TsType.Any;
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

package com.mdcc.dto2ts.context;


import cz.habarta.typescript.generator.emitter.*;
import lombok.*;

import java.util.*;
import java.util.function.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyContext
{
    @With
    private String className;
    @With
    private TsPropertyModel propertyModel;
    @With
    @Builder.Default
    private List<TsDecorator> decorators = new ArrayList<>();
    @Builder.Default
    private Map<String, Object> extendedProperties = new HashMap<>();

    public PropertyContext withTransformedProperty(Function<PropertyContext, TsPropertyModel> transformer)
    {
        return this.copy()
            .withPropertyModel(transformer.apply(this));
    }

    public PropertyContext addDecorator(TsDecorator decorator)
    {
        List<TsDecorator> newDecorators = new ArrayList<>(this.getDecorators());
        newDecorators.add(decorator);
        return this.withDecorators(newDecorators);
    }

    public PropertyContext addExtendedProperty(String key, Object value)
    {
        PropertyContext copy = this.copy();
        copy.extendedProperties.put(key, value);
        return copy;
    }

    public Optional<Object> getExtendedProperty(String key)
    {
        return Optional.ofNullable(this.extendedProperties.get(key));
    }

    public PropertyContext copy()
    {
        return PropertyContext.builder()
            .className(this.className)
            .propertyModel(this.propertyModel)
            .extendedProperties(new HashMap<>(this.extendedProperties))
            .decorators(new ArrayList<>(this.decorators))
            .build();
    }
}
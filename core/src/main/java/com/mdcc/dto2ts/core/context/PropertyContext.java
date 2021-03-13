package com.mdcc.dto2ts.core.context;


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
    private PropertyModel propertyModel;
    @With
    @Builder.Default
    private List<DecoratorModel> decorators = new ArrayList<>();
    @Builder.Default
    private Map<String, Object> extendedProperties = new HashMap<>();

    public PropertyContext withTransformedProperty(Function<PropertyContext, PropertyModel> transformer)
    {
        return this.copy()
            .withPropertyModel(transformer.apply(this));
    }

    public PropertyContext addDecorator(DecoratorModel decorator)
    {
        List<DecoratorModel> newDecorators = new ArrayList<>(this.getDecorators());
        newDecorators.add(decorator);
        return this.withDecorators(newDecorators);
    }

    public PropertyContext withExtendedProperty(String key, Object value)
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

    public PropertyModel getPropertyWithDecorators()
    {
        return propertyModel
            .withDecorators(decorators);
    }
}
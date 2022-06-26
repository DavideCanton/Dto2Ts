package com.mdcc.dto2ts.core.context;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyContext
{
    @With
    private String className;
    @With
    private PropertyRef propertyRef;
    @With
    private PropertyOperationsFactory propertyOperationsFactory;
    @With
    @Builder.Default
    private List<DecoratorRef> decorators = new LinkedList<>();
    @Builder.Default
    private Map<String, Object> extendedProperties = new HashMap<>();

    public PropertyContext withTransformedProperty(BiFunction<PropertyOperationsFactory, PropertyRef, PropertyRef> transformer)
    {
        return this.copy()
            .withPropertyRef(transformer.apply(this.propertyOperationsFactory, this.propertyRef));
    }

    public PropertyContext addDecorator(DecoratorRef decorator)
    {
        List<DecoratorRef> newDecorators = new ArrayList<>(this.getDecorators());
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
            .propertyRef(this.propertyRef)
            .propertyOperationsFactory(this.propertyOperationsFactory)
            .decorators(this.decorators)
            .extendedProperties(new HashMap<>(this.extendedProperties))
            .build();
    }

    public Object getUnderlyingProperty()
    {
        return propertyOperationsFactory.createPropertyConstructor()
            .buildPropertyWithDecorators(propertyRef, decorators);
    }
}
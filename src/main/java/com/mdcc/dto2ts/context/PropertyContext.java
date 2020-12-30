package com.mdcc.dto2ts.context;


import cz.habarta.typescript.generator.emitter.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;
import java.util.function.*;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PropertyContext
{
    private String className;
    private TsPropertyModel propertyModel;
    @Builder.Default
    private List<TsDecorator> decorators = new ArrayList<>();

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

    public PropertyContext copy()
    {
        return PropertyContext.builder()
            .className(this.className)
            .propertyModel(this.propertyModel)
            .build();
    }
}
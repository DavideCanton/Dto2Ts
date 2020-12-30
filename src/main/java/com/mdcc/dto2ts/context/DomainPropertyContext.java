package com.mdcc.dto2ts.context;

import lombok.*;
import lombok.experimental.*;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DomainPropertyContext extends PropertyContext
{
    private String domain;

    public DomainPropertyContext(PropertyContext context)
    {
        this.setPropertyModel(context.getPropertyModel());
        this.setClassName(context.getClassName());
    }

    @Override
    public DomainPropertyContext copy()
    {
        return DomainPropertyContext.builder()
            .propertyModel(this.getPropertyModel())
            .className(this.getClassName())
            .domain(this.domain)
            .build();
    }
}

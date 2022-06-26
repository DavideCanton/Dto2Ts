package com.mdcc.dto2ts.core.context;

import java.util.Optional;

public interface InfoExtractor
{
    String getPropertyName(PropertyRef propertyRef);
    Optional<String> getPropertyTypeName(PropertyRef propertyRef);

    String getDecoratorIdentifier(DecoratorRef decoratorRef);
}

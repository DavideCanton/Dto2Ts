package com.mdcc.dto2ts.core.context;

import java.util.*;

public interface DecoratorBuilder
{
    DecoratorRef buildArrayDecorator(PropertyRef propertyRef);

    DecoratorRef buildComplexDecorator(PropertyRef propertyRef);

    DecoratorRef buildDomainDecorator(PropertyRef propertyRef, String domain);

    Optional<DecoratorRef> buildSimpleDecorator(PropertyRef propertyRef);
}

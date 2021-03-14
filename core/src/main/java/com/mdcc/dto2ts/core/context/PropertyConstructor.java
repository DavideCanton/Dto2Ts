package com.mdcc.dto2ts.core.context;

import java.util.*;

public interface PropertyConstructor
{
    Object buildPropertyWithDecorators(PropertyRef propertyRef, List<DecoratorRef> decorators);
}

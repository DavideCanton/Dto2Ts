package com.mdcc.dto2ts.core.context;

import java.util.*;

public interface PropertyTransformationExecutor<P, C>
{
    List<P> transformProperties(List<P> properties, C context);
}

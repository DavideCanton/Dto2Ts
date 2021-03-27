package com.mdcc.dto2ts.json.main;

import com.mdcc.dto2ts.core.context.*;

public class JsonOperationsFactory extends CachingPropertyOperationsFactory
{
    @Override
    protected PropertyTypeChecker innerCreatePropertyTypeChecker()
    {
        return null;
    }

    @Override
    protected PropertyRefTransformer innerCreatePropertyRefTransformer()
    {
        return null;
    }

    @Override
    protected PropertyConstructor innerCreatePropertyConstructor()
    {
        return null;
    }

    @Override
    protected InfoExtractor innerCreateInfoExtractor()
    {
        return null;
    }

    @Override
    protected DecoratorBuilder innerCreateDecoratorBuilder()
    {
        return null;
    }
}

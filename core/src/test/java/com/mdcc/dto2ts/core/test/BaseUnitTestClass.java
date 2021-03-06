package com.mdcc.dto2ts.core.test;

import com.mdcc.dto2ts.core.context.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

public abstract class BaseUnitTestClass
{
    @Mock
    protected PropertyOperationsFactory factory;

    @Mock
    protected DecoratorBuilder decoratorBuilder;
    @Mock
    protected InfoExtractor infoExtractor;
    @Mock
    protected PropertyConstructor propertyConstructor;
    @Mock
    protected PropertyRefTransformer propertyRefTransformer;
    @Mock
    protected PropertyTypeChecker propertyTypeChecker;


    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        lenient().when(factory.createDecoratorBuilder()).thenReturn(decoratorBuilder);
        lenient().when(factory.createInfoExtractor()).thenReturn(infoExtractor);
        lenient().when(factory.createPropertyConstructor()).thenReturn(propertyConstructor);
        lenient().when(factory.createPropertyRefTransformer()).thenReturn(propertyRefTransformer);
        lenient().when(factory.createPropertyTypeChecker()).thenReturn(propertyTypeChecker);
    }
}

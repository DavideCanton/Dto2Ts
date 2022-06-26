package com.mdcc.dto2ts.core.test;

import com.mdcc.dto2ts.core.context.DecoratorBuilder;
import com.mdcc.dto2ts.core.context.InfoExtractor;
import com.mdcc.dto2ts.core.context.PropertyConstructor;
import com.mdcc.dto2ts.core.context.PropertyOperationsFactory;
import com.mdcc.dto2ts.core.context.PropertyRefTransformer;
import com.mdcc.dto2ts.core.context.PropertyTypeChecker;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.lenient;

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

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);

        lenient().when(factory.createDecoratorBuilder()).thenReturn(decoratorBuilder);
        lenient().when(factory.createInfoExtractor()).thenReturn(infoExtractor);
        lenient().when(factory.createPropertyConstructor()).thenReturn(propertyConstructor);
        lenient().when(factory.createPropertyRefTransformer()).thenReturn(propertyRefTransformer);
        lenient().when(factory.createPropertyTypeChecker()).thenReturn(propertyTypeChecker);
    }
}

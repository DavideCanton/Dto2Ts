package com.mdcc.dto2ts.core.test.transformers;

import com.mdcc.dto2ts.core.context.PropertyContext;
import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.test.BaseUnitTestClass;
import com.mdcc.dto2ts.core.transformers.FlagPropertyTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlagPropertyTransformerTest extends BaseUnitTestClass
{
    @InjectMocks
    private FlagPropertyTransformer transformer;

    @Test
    public void testShouldTransformCorrectly()
    {
        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");
        PropertyRef propertyRef2 = mock(PropertyRef.class, "propertyRef2");

        when(infoExtractor.getPropertyName(propertyRef)).thenReturn("flgproperty");
        when(propertyRefTransformer.makeBoolean(propertyRef)).thenReturn(propertyRef2);

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertTrue(result.isPresent());

        PropertyRef resultProperty = result.get().getPropertyRef();

        assertEquals(resultProperty, propertyRef2);
    }

    @Test
    public void testShouldNotTransformIfNotStartingWithPrefix()
    {
        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");

        when(infoExtractor.getPropertyName(propertyRef)).thenReturn("property");

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());
    }
}

package com.mdcc.dto2ts.core.test.transformers;

import com.mdcc.dto2ts.core.context.Arguments;
import com.mdcc.dto2ts.core.context.PropertyContext;
import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.domains.DomainHandler;
import com.mdcc.dto2ts.core.imports.ImportHandler;
import com.mdcc.dto2ts.core.test.BaseUnitTestClass;
import com.mdcc.dto2ts.core.transformers.DomainPropertyTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mdcc.dto2ts.core.imports.ImportNames.I_LOCALIZABLE_PROPERTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DomainPropertyTransformerTest extends BaseUnitTestClass
{
    @Mock
    private DomainHandler domainHandler;

    @Mock
    private ImportHandler importHandler;

    @Mock
    private Arguments arguments;

    @InjectMocks
    private DomainPropertyTransformer transformer;

    @BeforeEach
    public void localSetup()
    {
        lenient().when(arguments.getDomainPrefix()).thenReturn("cod");
    }

    @Test
    public void testShouldTransformCorrectly()
    {
        String domain = "miotipo";
        when(domainHandler.findDomain(domain)).thenReturn(Optional.of(domain));

        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");
        PropertyRef propertyRef2 = mock(PropertyRef.class, "propertyRef2");

        when(infoExtractor.getPropertyName(propertyRef)).thenReturn("cod" + domain);
        when(propertyTypeChecker.isString(propertyRef)).thenReturn(true);
        when(propertyRefTransformer.makeDomain(propertyRef)).thenReturn(propertyRef2);

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertTrue(result.isPresent());

        PropertyRef resultProperty = result.get().getPropertyRef();

        assertEquals(resultProperty, propertyRef2);

        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), I_LOCALIZABLE_PROPERTY);
    }

    @Test
    public void testShouldNotTransformIfDomainNotFound()
    {
        String domain = "miotipo";
        when(domainHandler.findDomain(domain)).thenReturn(Optional.empty());

        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");

        when(infoExtractor.getPropertyName(propertyRef)).thenReturn("cod" + domain);
        when(propertyTypeChecker.isString(propertyRef)).thenReturn(true);

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }

    @Test
    public void testShouldNotTransformIfNotString()
    {
        String domain = "miotipo";

        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");

        when(infoExtractor.getPropertyName(propertyRef)).thenReturn("cod" + domain);
        when(propertyTypeChecker.isString(propertyRef)).thenReturn(false);

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }

    @Test
    public void testShouldNotTransformIfNotStartingWithPrefix()
    {
        String domain = "miotipo";

        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");

        when(infoExtractor.getPropertyName(propertyRef)).thenReturn(domain);
        when(propertyTypeChecker.isString(propertyRef)).thenReturn(true);

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }
}

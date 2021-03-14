package com.mdcc.dto2ts.core.test.transformers;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.domains.*;
import com.mdcc.dto2ts.core.imports.*;
import com.mdcc.dto2ts.core.test.*;
import com.mdcc.dto2ts.core.transformers.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import java.util.*;

import static com.mdcc.dto2ts.core.imports.ImportNames.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DomainPropertyTransformerTest extends BaseUnitTestClass
{
    @Mock
    private DomainHandler domainHandler;

    @Mock
    private ImportHandler importHandler;

    @Mock
    private IArguments arguments;

    @InjectMocks
    private DomainPropertyTransformer transformer;

    @Before
    public void localSetup()
    {
        when(arguments.getDomainPrefix()).thenReturn("cod");
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

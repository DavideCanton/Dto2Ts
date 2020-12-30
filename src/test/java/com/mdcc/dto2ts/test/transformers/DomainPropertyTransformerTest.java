package com.mdcc.dto2ts.test.transformers;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.core.*;
import com.mdcc.dto2ts.domains.*;
import com.mdcc.dto2ts.imports.*;
import com.mdcc.dto2ts.test.*;
import com.mdcc.dto2ts.transformers.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import java.util.*;

import static com.mdcc.dto2ts.imports.ImportNames.*;
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
    private Arguments arguments;

    @InjectMocks
    private DomainPropertyTransformer transformer;

    @Before
    public void setup()
    {
        when(arguments.getDomainPrefix()).thenReturn("cod");
    }

    @Test
    public void testShouldTransformCorrectly()
    {
        String domain = "miotipo";
        when(domainHandler.findDomain(domain)).thenReturn(Optional.of(domain));

        TsPropertyModel property = new TsPropertyModel(
            "cod" + domain,
            TsType.String,
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertTrue(result.isPresent());

        TsPropertyModel resultProperty = result.get().getPropertyModel();

        assertEquals(property.getName(), resultProperty.getName());
        assertEquals(
            new TsType.NullableType(new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String)),
            resultProperty.getTsType()
        );

        List<TsDecorator> decorators = resultProperty.getDecorators();
        assertEquals(1, decorators.size());

        TsDecorator d = decorators.get(0);
        assertEquals(1, d.getArguments().size());
        assertEquals(JSON_LOCALIZABLE_PROPERTY, d.getIdentifierReference().getIdentifier());

        TsMemberExpression expr = (TsMemberExpression) d.getArguments().get(0);
        assertEquals(domain, expr.getIdentifierName());
        assertEquals(DOMAINS, ((TsIdentifierReference) expr.getExpression()).getIdentifier());

        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), JSON_LOCALIZABLE_PROPERTY);
        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), I_LOCALIZABLE_PROPERTY);
        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), DOMAINS);
        verify(domainHandler, times(1)).registerUsedDomain(domain);
    }

    @Test
    public void testShouldNotTransformIfDomainNotFound()
    {
        when(domainHandler.findDomain("miotipo")).thenReturn(Optional.empty());

        TsPropertyModel property = new TsPropertyModel(
            "codmiotipo",
            TsType.String,
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }

    @Test
    public void testShouldNotTransformIfNotString()
    {
        TsPropertyModel property = new TsPropertyModel(
            "codmiotipo",
            TsType.Number,
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }

    @Test
    public void testShouldNotTransformIfNotStartingWithPrefix()
    {
        TsPropertyModel property = new TsPropertyModel(
            "desmiotipo",
            TsType.String,
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build();

        Optional<PropertyContext> result = transformer.transformProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }
}

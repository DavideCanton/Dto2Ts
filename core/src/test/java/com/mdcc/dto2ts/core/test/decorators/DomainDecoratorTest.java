package com.mdcc.dto2ts.core.test.decorators;

import com.mdcc.dto2ts.core.context.*;
import com.mdcc.dto2ts.core.decorators.*;
import com.mdcc.dto2ts.core.domains.*;
import com.mdcc.dto2ts.core.imports.*;
import com.mdcc.dto2ts.core.test.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static com.mdcc.dto2ts.core.context.ContextConstants.*;
import static com.mdcc.dto2ts.core.imports.ImportNames.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DomainDecoratorTest extends BaseUnitTestClass
{
    @Mock
    private DomainHandler domainHandler;

    @Mock
    private ImportHandler importHandler;

    @InjectMocks
    private DomainDecorator domainDecorator;

    @Test
    public void testShouldTransformCorrectly()
    {
        String domain = "miotipo";

        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");
        DecoratorRef decoratorRef = mock(DecoratorRef.class, "decoratorRef");

        when(propertyTypeChecker.isBasicType(propertyRef)).thenReturn(true);
        when(infoExtractor.getPropertyTypeName(propertyRef)).thenReturn(Optional.of(I_LOCALIZABLE_PROPERTY));
        when(decoratorBuilder.buildDomainDecorator(propertyRef, domain)).thenReturn(decoratorRef);

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build()
            .withExtendedProperty(DOMAIN_KEY, domain);

        Optional<PropertyContext> result = domainDecorator.decorateProperty(ctx);

        assertTrue(result.isPresent());

        List<DecoratorRef> decorators = result.get().getDecorators();
        assertEquals(1, decorators.size());

        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), JSON_LOCALIZABLE_PROPERTY);
        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), DOMAINS);
        verify(domainHandler, times(1)).registerUsedDomain(domain);
    }

    @Test
    public void testShouldNotTransformIfNotLocalizable()
    {
        String domain = "miotipo";

        PropertyRef propertyRef = mock(PropertyRef.class, "propertyRef");

        when(propertyTypeChecker.isBasicType(propertyRef)).thenReturn(true);
        when(infoExtractor.getPropertyTypeName(propertyRef)).thenReturn(Optional.of("String"));

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyRef(propertyRef)
            .propertyOperationsFactory(factory)
            .build()
            .withExtendedProperty(DOMAIN_KEY, domain);

        Optional<PropertyContext> result = domainDecorator.decorateProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }
}

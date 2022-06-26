package com.mdcc.dto2ts.core.test.decorators;

import com.mdcc.dto2ts.core.context.DecoratorRef;
import com.mdcc.dto2ts.core.context.PropertyContext;
import com.mdcc.dto2ts.core.context.PropertyRef;
import com.mdcc.dto2ts.core.decorators.DomainDecorator;
import com.mdcc.dto2ts.core.domains.DomainHandler;
import com.mdcc.dto2ts.core.imports.ImportHandler;
import com.mdcc.dto2ts.core.test.BaseUnitTestClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.mdcc.dto2ts.core.context.ContextConstants.DOMAIN_KEY;
import static com.mdcc.dto2ts.core.imports.ImportNames.DOMAINS;
import static com.mdcc.dto2ts.core.imports.ImportNames.I_LOCALIZABLE_PROPERTY;
import static com.mdcc.dto2ts.core.imports.ImportNames.JSON_LOCALIZABLE_PROPERTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

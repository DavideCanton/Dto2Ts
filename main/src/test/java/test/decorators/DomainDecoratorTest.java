package test.decorators;

import com.mdcc.dto2ts.context.*;
import com.mdcc.dto2ts.core.decorators.decorators.*;
import com.mdcc.dto2ts.domains.*;
import com.mdcc.dto2ts.imports.*;
import com.mdcc.dto2ts.test.*;
import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.emitter.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;
import test.*;

import java.util.*;

import static com.mdcc.dto2ts.context.ContextConstants.DOMAIN_KEY;
import static com.mdcc.dto2ts.imports.ImportNames.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

        TsPropertyModel property = new TsPropertyModel(
            "cod" + domain,
            new TsType.GenericBasicType(I_LOCALIZABLE_PROPERTY, TsType.String),
            TsModifierFlags.None,
            true,
            null
        );

        PropertyContext ctx = PropertyContext.builder()
            .className("className")
            .propertyModel(property)
            .build()
            .withExtendedProperty(DOMAIN_KEY, domain);

        Optional<PropertyContext> result = domainDecorator.decorateProperty(ctx);

        assertTrue(result.isPresent());

        List<TsDecorator> decorators = result.get().getDecorators();
        assertEquals(1, decorators.size());

        TsDecorator d = decorators.get(0);
        assertEquals(1, d.getArguments().size());
        assertEquals(JSON_LOCALIZABLE_PROPERTY, d.getIdentifierReference().getIdentifier());

        TsMemberExpression expr = (TsMemberExpression) d.getArguments().get(0);
        assertEquals(domain, expr.getIdentifierName());
        assertEquals(DOMAINS, ((TsIdentifierReference) expr.getExpression()).getIdentifier());

        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), JSON_LOCALIZABLE_PROPERTY);
        verify(importHandler, times(1)).registerClassLibraryImport(ctx.getClassName(), DOMAINS);
        verify(domainHandler, times(1)).registerUsedDomain(domain);
    }

    @Test
    public void testShouldNotTransformIfNotLocalizable()
    {
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

        Optional<PropertyContext> result = domainDecorator.decorateProperty(ctx);

        assertFalse(result.isPresent());

        verify(importHandler, never()).registerClassLibraryImport(anyString(), anyString());
        verify(domainHandler, never()).registerUsedDomain(anyString());
    }
}
